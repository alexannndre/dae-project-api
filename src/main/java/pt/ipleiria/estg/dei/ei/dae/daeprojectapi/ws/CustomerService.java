package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.PaginatedDTOs;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.PolicyDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.create.CustomerCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers.PolicyManager;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.requests.PageRequest;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/customers")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
@Authenticated
public class CustomerService {
    @EJB
    private CustomerBean customerBean;

    @EJB
    private OccurrenceBean occurrenceBean;

    @GET
    @RolesAllowed({"Administrator", "Repairer", "Expert"})
    @Path("/")
    public Response all(@BeanParam @Valid PageRequest pageRequest) {
        var count = customerBean.count();

        if (pageRequest.getOffset() > count)
            return Response.ok(new PaginatedDTOs<>(count)).build();

        var customers = customerBean.getAll(pageRequest.getOffset(), pageRequest.getLimit());
        var paginatedDTO = new PaginatedDTOs<>(CustomerDTO.from(customers), count, pageRequest.getOffset());

        return Response.ok(paginatedDTO).build();
    }

    @GET
    @RolesAllowed({"Administrator"})
    @Path("{vat}")
    public Response get(@PathParam("vat") String vat) {
        return Response.ok(CustomerDTO.from(customerBean.findOrFail(vat))).build();
    }

    @POST
    @RolesAllowed({"Administrator"})
    @Path("/")
    public Response create(CustomerCreateDTO customerDTO) {
        customerBean.create(
                customerDTO.getVat(),
                customerDTO.getName(),
                customerDTO.getEmail(),
                customerDTO.getPassword()
        );

        var customer = customerBean.findOrFail(customerDTO.getVat());
        return Response.status(CREATED).entity(CustomerDTO.from(customer)).build();
    }

    // Policies
    @GET
    @RolesAllowed({"Administrator", "Customer"})
    @Path("{vat}/policies")
    public Response getPolicies(@PathParam("vat") String vat) {
        return Response.ok(PolicyDTO.from(PolicyManager.getPoliciesByVat(vat))).build();
    }


    // Occurrences
    @GET
    @RolesAllowed({"Administrator", "Customer", "Repairer", "Expert"})
    @Path("{vat}/occurrences")
    public Response getOccurrences(@PathParam("vat") String vat) {
        return Response.ok(OccurrenceDTO.from(customerBean.getOccurrences(vat))).build();
    }

    @GET
    @RolesAllowed({"Administrator", "Customer"})
    @Path("{vat}/occurrences/status/{status}")
    public Response getOccurrencesByStatus(@PathParam("vat") String vat, @PathParam("status") Status status) {
        var occurrences = customerBean.getOccurrencesByStatus(vat, status);
        return Response.ok(OccurrenceDTO.from(occurrences)).build();
    }

    @POST
    @RolesAllowed({"Administrator", "Customer"})
    @Path("/{vat}/occurrences")
    public Response createOccurrence(@PathParam("vat") String vat, OccurrenceDTO occurrenceDTO) {
        var occurrenceId = occurrenceBean.create(occurrenceDTO.getDescription(), occurrenceDTO.getPolicy(), vat);
        var dto = OccurrenceDTO.from(occurrenceBean.findOrFail(occurrenceId));
        return Response.status(CREATED).entity(dto).build();
    }
}
