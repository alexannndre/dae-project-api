package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ws;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ServiceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.ServiceBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Service;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("services")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
//@Authenticated
//@RolesAllowed({"Teacher", "Administrator"})
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
        return ServiceDTO.toServiceDTOs(serviceBean.getServicesByType(type).stream().filter(Service::isOfficialService).collect(Collectors.toList()));
    }

}
