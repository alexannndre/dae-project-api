package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ErrorDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ServiceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.ServiceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("services")
@Produces({APPLICATION_JSON})  // injects header “Content-Type: application/json”
@Consumes({APPLICATION_JSON})  // injects header “Accept: application/json”
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
public class ServiceService {
    //    @EJB
//    private CustomerBean studentBean;

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
        return ServiceDTO.toServiceDTOs(serviceBean.getServicesByType(type));
    }

}
