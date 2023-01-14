package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ServiceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.UploadResultDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.ServiceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Service;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.helpers.CsvHelper;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
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
@Authenticated
public class ServiceService {

    @EJB
    private ServiceBean serviceBean;

    @GET
    @Path("/")
    public List<ServiceDTO> all() {
        return ServiceDTO.toServiceDTOs(serviceBean.getAllServices());
    }

    @GET
    @Path("{type}")
    public List<ServiceDTO> getByType(@PathParam("type") String type) {
        return ServiceDTO.toServiceDTOs(serviceBean.getServicesByType(type).stream().filter(Service::isOfficial).collect(Collectors.toList()));
    }

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    @Path("upload")
    public Response loadData(MultipartFormDataInput input) {
        List<ServiceDTO> list;
        try {
            list = CsvHelper.loadCsv(input, CsvHelper::toService);
        } catch (Exception e) {
            return Response.status(BAD_REQUEST).entity(new ErrorDTO(e.getMessage())).build();
        }

        int count = list.size(), success = 0, fail = 0;

        if (count == 0)
            return Response.status(BAD_REQUEST).entity(new ErrorDTO("No processable services were found in that file")).build();

        for (ServiceDTO serv : list) {
            try {
                serviceBean.create(serv);
                success++;
            } catch (EJBTransactionRolledbackException etre) {
                fail++;
            }
        }

        var msg = "";

        if (fail == 0)
            msg = String.format("Success! Created %d new services", count);
        else if (success == 0)
            msg = String.format("Failed! None of the %d processed services were created due to invalid id/vat(s)", count);
        else
            msg = String.format("%d services have been processed. %d were successfully created. Failed to import %d services due to invalid id/vat(s)", count, success, fail);

        return Response.ok().entity(new UploadResultDTO(count, success, fail, msg)).build();
    }
}
