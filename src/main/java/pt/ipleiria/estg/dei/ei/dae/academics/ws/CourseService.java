package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.CourseBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("courses") // relative url web path for this service
@Produces({APPLICATION_JSON})  // injects header “Content-Type: application/json”
@Consumes({APPLICATION_JSON})  // injects header “Accept: application/json”
public class CourseService {
    @EJB
    private CourseBean courseBean;

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/")  // means: the relative url path is “/api/courses/”
    public List<CourseDTO> getAllCoursesWS() {
        return toDTOs(courseBean.getAllCourses());
    }

    // converts an entity Course to a DTO Course class
    private CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getCode(),
                course.getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<CourseDTO> toDTOs(List<Course> courses) {
        return courses.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @POST
    @Path("/")
    @Authenticated
    @RolesAllowed({"Administrator"})
    public Response create(CourseDTO courseDTO) {
        courseBean.create(
                courseDTO.getCode(),
                courseDTO.getName()
        );
        Course newCourse = courseBean.find(courseDTO.getCode());
        if (newCourse == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).entity(toDTO(newCourse)).build();
    }

}
