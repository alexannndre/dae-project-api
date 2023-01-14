package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers.PolicyManager;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.requests.PageRequest;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/customers")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
public class CustomerService {
    @EJB
    private CustomerBean customerBean;

    @EJB
    private OccurrenceBean occurrenceBean;

    @EJB
    private EmailBean emailBean;

    @GET
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
    @Path("{vat}")
    public Response get(@PathParam("vat") String vat) {
        return Response.ok(CustomerDTO.from(customerBean.findOrFail(vat))).build();
    }

    @GET
    @Path("{vat}/policies")
    public Response getByVat(@PathParam("vat") String vat) {
        return Response.ok(PolicyDTO.from(PolicyManager.getPoliciesByVat(vat))).build();
    }

    @POST
    @Path("/")
    public Response create(CustomerDTO customerDTO) {
        customerBean.create(
                customerDTO.getVat(),
                customerDTO.getName(),
                customerDTO.getEmail(),
                customerDTO.getPassword()
        );

        var customer = customerBean.findOrFail(customerDTO.getVat());
        return Response.status(CREATED).entity(CustomerDTO.from(customer)).build();
    }

    // Occurrences
    @GET
    @Path("{vat}/occurrences")
    public Response getOccurrences(@PathParam("vat") String vat) {
        return Response.ok(OccurrenceDTO.from(customerBean.getOccurrences(vat))).build();
    }

    @POST
    @Path("/{vat}/occurrences")
    public Response createOccurrence(@PathParam("vat") String vat, OccurrenceDTO occurrenceDTO) {
        var occurrenceId = occurrenceBean.create(occurrenceDTO.getDescription(), occurrenceDTO.getPolicy(), vat);
        var dto = OccurrenceDTO.from(occurrenceBean.findOrFail(occurrenceId));
        return Response.status(CREATED).entity(dto).build();
    }

    // Emails
    @POST
    @Path("/{vat}/email/send")
    public Response sendEmail(@PathParam("vat") String vat, EmailDTO emailDTO) throws MessagingException {
        var customer = customerBean.findOrFail(vat);
        emailBean.send(customer.getEmail(), emailDTO.getSubject(), emailDTO.getBody());
        return Response.ok().build();
    }

}
