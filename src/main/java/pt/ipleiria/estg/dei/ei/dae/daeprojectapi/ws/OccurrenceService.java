package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.OccurrenceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.OccurrenceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("occurrences")
@Produces({APPLICATION_JSON})  // injects header “Content-Type: application/json”
@Consumes({APPLICATION_JSON})  // injects header “Accept: application/json”
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
public class OccurrenceService {
    //    @EJB
//    private CustomerBean studentBean;

    @EJB
    private OccurrenceBean occurrenceBean;

    //    @Context
//    private SecurityContext securityContext;

    private OccurrenceDTO toDTO(Occurrence occurrence) {
        return new OccurrenceDTO(
                occurrence.getId(),
                occurrence.getDescription(),
                occurrence.getStatus()
        );
    }

    private List<OccurrenceDTO> occurrencesToDTOs(List<Occurrence> occurrences) {
        return occurrences.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<OccurrenceDTO> getAllOccurrencesWS() {
        return occurrencesToDTOs(occurrenceBean.getAllOccurrences());
    }

    @GET
    @Path("{id}")
    public Response getOccurrenceDetailsWS(@PathParam("id") int id) {
        Occurrence occurrence = occurrenceBean.find(id);
        return Response.ok(toDTO(occurrence)).build();
    }
}
