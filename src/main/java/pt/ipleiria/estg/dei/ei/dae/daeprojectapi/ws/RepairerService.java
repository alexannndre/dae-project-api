package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.RepairerBean;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/repairers")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
public class RepairerService {
    @EJB
    private RepairerBean repairerBean;

    @EJB
    private EmailBean emailBean;

    // Emails
    @POST
    @Path("/{vat}/email/send")
    public Response sendEmail(@PathParam("vat") String vat, EmailDTO emailDTO) throws MessagingException {
        var repairer = repairerBean.findOrFail(vat);
        emailBean.send(repairer.getEmail(), emailDTO.getSubject(), emailDTO.getBody());
        return Response.ok().build();
    }

}
