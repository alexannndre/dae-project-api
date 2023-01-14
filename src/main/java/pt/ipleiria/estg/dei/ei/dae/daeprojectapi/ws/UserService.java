package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.create.UserCreateDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers.PolicyManager;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.requests.PageRequest;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/users")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
@Authenticated
public class UserService {
    @EJB
    private UserBean userBean;

    @EJB
    private EmailBean emailBean;

    @PUT
    @Path("{vat}")
    public Response update(@PathParam("vat") String vat, UserDTO userDTO) {
        userBean.update(vat, userDTO.getName(), userDTO.getEmail());
        userDTO = UserDTO.from(userBean.findOrFail(vat));
        return Response.ok(userDTO).build();
    }

    @PUT
    @Path("{vat}/password")
    public Response updatePassword(@PathParam("vat") String vat, UserCreateDTO userDTO) {
        userBean.updatePassword(vat, userDTO.getPassword());
        userDTO = UserCreateDTO.from(userBean.findOrFail(vat));
        return Response.ok(userDTO).build();
    }

    // Emails
    @POST
    @RolesAllowed({"Administrator", "Customer", "Expert", "Repairer"})
    @Path("/{vat}/email/send")
    public Response sendEmail(@PathParam("vat") String vat, EmailDTO emailDTO) throws MessagingException {
        var user = userBean.findOrFail(vat);
        emailBean.send(user.getEmail(), emailDTO.getSubject(), emailDTO.getBody());
        return Response.ok().build();
    }

}
