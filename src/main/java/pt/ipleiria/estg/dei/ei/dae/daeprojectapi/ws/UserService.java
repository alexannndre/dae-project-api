package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.CustomerDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.PaginatedDTOs;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.PolicyDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers.PolicyManager;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.requests.PageRequest;

import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/users")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})

public class UserService {
    @EJB
    private UserBean userBean;

    @PUT
    @Path("{vat}")
    public Response update(@PathParam("vat") String vat, UserDTO userDTO) {
        userBean.update(vat, userDTO.getName(), userDTO.getEmail());
        userDTO = UserDTO.from(userBean.findOrFail(vat));
        return Response.ok(userDTO).build();
    }

    @PUT
    @Path("{vat}/password")
    public Response updatePassword(@PathParam("vat") String vat, UserDTO userDTO) {
        userBean.updatePassword(vat, userDTO.getPassword());
        userDTO = UserDTO.from(userBean.findOrFail(vat));
        return Response.ok(userDTO).build();
    }
}
