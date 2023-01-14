package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.PolicyDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers.PolicyManager;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("policies")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Administrator"})
public class PolicyService {
    @EJB
    private OccurrenceBean occurrenceBean;

    @GET
    @Path("/")
    public List<PolicyDTO> all() {
        return PolicyDTO.from(PolicyManager.getAllPolicies());
    }

    @GET
    @RolesAllowed({"Customer", "Repairer", "Expert"})
    @Path("{code}")
    public Response get(@PathParam("code") String code) {
        var policy = PolicyManager.getPolicyByCode(code);
        if (policy == null)
            return Response.status(NOT_FOUND).build();
        return Response.ok(PolicyDTO.from(policy)).build();
    }

    @GET
    @Path("{code}/occurrences")
    public Response getOccurrences(@PathParam("code") String code) {
        return Response.ok(OccurrenceDTO.from(occurrenceBean.getOccurrencesByPolicy(code))).build();
    }

}
