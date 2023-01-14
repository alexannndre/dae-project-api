package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import jakarta.annotation.security.RolesAllowed;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ServiceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.ServiceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Service;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.helpers.CsvHelper;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("services")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
public class ServiceService {

    @EJB
    private ServiceBean serviceBean;

    @EJB
    private EmailBean emailBean;

    @GET
    @Path("/")
    public List<ServiceDTO> all() {
        return ServiceDTO.toServiceDTOs(serviceBean.getAllServices());
    }

    @GET
    @Path("{type}")
    public List<ServiceDTO> getByType(@PathParam("type") String type) {
        return ServiceDTO.toServiceDTOs(serviceBean.getServicesByType(type).stream().filter(Service::isOfficialService).collect(Collectors.toList()));
    }

    // Emails
    @POST
    @Path("/{id}/email/send")
    public Response sendEmail(@PathParam("id") Long id, EmailDTO emailDTO) throws MessagingException {
        var service = serviceBean.findOrFail(id);
        emailBean.send(service.getEmail(), emailDTO.getSubject(), emailDTO.getBody());
        return Response.ok().build();
    }

    @POST
    @Path("upload")
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    @Authenticated
    @RolesAllowed({"Administrator"})
    public Response loadData(MultipartFormDataInput input) {
        List<ServiceDTO> list;
        try{
            list = CsvHelper.loadCsv(input, CsvHelper::toService);
        }catch(Exception e){
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }

        int count=list.size(),success=0,fail=0;

        if(count==0)
            return Response.status(BAD_REQUEST).entity(new ErrorDTO("No processable services were found in that file")).build();

        for (ServiceDTO serv : list){
            try{
                serviceBean.create(serv);
                success++;
            }catch(EJBTransactionRolledbackException etre){
                fail++;
            }
        }

        var msg = "";
        if(fail==0)
            msg = String.format("Success! Created %d new services", count);
        else if(success==0)
            msg = String.format("Failed! None of the %d processed services were created due to invalid id/vat(s)", count);
        else
            msg = String.format("%d services have been processed. %d were successfully created. Failed to import %d services due to invalid id/vat(s)",count, success,fail);
        return Response.ok(msg).build();
    }
}
