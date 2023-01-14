package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.create.UserCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/users")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
@Authenticated
public class UserService {
    @EJB
    private UserBean userBean;

    @EJB
    private EmailBean emailBean;

    @Context
    private SecurityContext securityContext;

    @PUT
    @Path("/me")
    public Response updateMe(UserDTO userDTO) {
        var vat = securityContext.getUserPrincipal().getName();

        userBean.update(vat, userDTO.getName(), userDTO.getEmail());
        userDTO = UserDTO.from(userBean.findOrFail(vat));

        return Response.ok(userDTO).build();
    }

    @PUT
    @Path("/me/password")
    public Response updateMyPassword(UserCreateDTO userDTO) {
        var vat = securityContext.getUserPrincipal().getName();
        userBean.updatePassword(vat, userDTO.getPassword());
        return Response.ok().build();
    }

    @PUT
    @RolesAllowed({"Administrator"})
    @Path("{vat}")
    public Response updateUser(@PathParam("vat") String vat, UserDTO userDTO) {
        userBean.update(vat, userDTO.getName(), userDTO.getEmail());
        userDTO = UserDTO.from(userBean.findOrFail(vat));

        return Response.ok(userDTO).build();
    }

    @PUT
    @RolesAllowed({"Administrator"})
    @Path("{vat}/password")
    public Response updateUserPassword(@PathParam("vat") String vat, UserCreateDTO userDTO) {
        userBean.updatePassword(vat, userDTO.getPassword());
        userDTO = UserCreateDTO.from(userBean.findOrFail(vat));

        return Response.ok(userDTO).build();
    }

    // Emails
    @POST
    @Path("/{vat}/email/send")
    public Response sendEmail(@PathParam("vat") String vat, EmailDTO emailDTO) throws MessagingException {
        var user = userBean.findOrFail(vat);
        emailBean.send(user.getEmail(), emailDTO.getSubject(), emailDTO.getBody());
        return Response.ok().build();
    }

}
