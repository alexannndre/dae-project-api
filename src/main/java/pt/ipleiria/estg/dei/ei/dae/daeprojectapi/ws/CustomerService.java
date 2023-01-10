package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.PaginatedDTOs;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.requests.PageRequest;

import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/customers")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
public class CustomerService {
    @EJB
    private CustomerBean customerBean;

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
    @Path("{nif}")
    public Response get(@PathParam("nif") String nif) {
        return Response.ok(CustomerDTO.from(customerBean.findOrFail(nif))).build();
    }

    @GET
    @Path("{nif}/occurrences")
    public Response getOccurrences(@PathParam("nif") String nif) throws MyEntityNotFoundException {
        return Response.ok(OccurrenceDTO.from(customerBean.getOccurrences(nif))).build();
    }
}
