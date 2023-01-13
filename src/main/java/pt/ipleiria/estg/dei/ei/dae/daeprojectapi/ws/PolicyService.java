package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.PolicyDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers.PolicyManager;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.*;

@Path("policies")
@Produces({APPLICATION_JSON})  // injects header “Content-Type: application/json”
@Consumes({APPLICATION_JSON})  // injects header “Accept: application/json”
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
public class PolicyService {
    //    @EJB
//    private CustomerBean studentBean;

    //    @Context
//    private SecurityContext securityContext;

    @EJB
    private OccurrenceBean occurrenceBean;
    @GET
    @Path("/")
    public List<PolicyDTO> all() {
        return PolicyDTO.from(PolicyManager.getAllPolicies());
    }

    @GET
    @Path("{code}")
    public Response get(@PathParam("code") String code) {
        var policy = PolicyManager.getPolicyByCode(code);
        if(policy==null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(PolicyDTO.from(policy)).build();
    }


    @GET
    @Path("{code}/occurrences")
    public Response getOccurrences(@PathParam("code") String code) {
        return Response.ok(OccurrenceDTO.from(occurrenceBean.getOccurrencesByPolicy(code))).build();
    }


}
