package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.TeacherBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("teachers")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
public class TeacherService {
    @EJB
    private TeacherBean teacherBean;

    // converts an entity Teacher to a DTO Teacher class
    private TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getUsername(),
                teacher.getPassword(),
                teacher.getName(),
                teacher.getEmail(),
                teacher.getOffice()
        );
    }

    // converts an entity Subject to a DTO Subject class
    private SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
                subject.getCode(),
                subject.getName(),
                subject.getCourse().getCode(),
                subject.getCourse().getName(),
                subject.getCourseYear(),
                subject.getScholarYear()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<TeacherDTO> toDTOs(List<Teacher> teachers) {
        return teachers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<TeacherDTO> getAllTeachersWS() {
        return toDTOs(teacherBean.getAllTeachers());
    }

    @GET
    @Path("{username}")
    public Response getTeacherDetailsWS(@PathParam("username") String username) {
        Teacher teacher = teacherBean.find(username);
        if (teacher != null)
            return Response.ok(toDTO(teacher)).build();
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
    }

    @GET
    @Path("{username}/subjects")
    public Response getTeacherSubjectsWS(@PathParam("username") String username) {
        Teacher teacher = teacherBean.find(username);
        if (teacher == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        List<SubjectDTO> dtos = subjectsToDTOs(teacher.getSubjects());
        return Response.ok(dtos).build();
    }

    @POST
    @Path("/")
    public Response createTeacherWS(TeacherDTO teacherDTO) {
        teacherBean.create(
                teacherDTO.getUsername(),
                teacherDTO.getPassword(),
                teacherDTO.getName(),
                teacherDTO.getEmail(),
                teacherDTO.getOffice()
        );
        Teacher newTeacher = teacherBean.find(teacherDTO.getUsername());
        if (newTeacher == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("{username}/subjects/{subjectCode}")
    public Response associateTeacherToSubject(@PathParam("username") String username, @PathParam("subjectCode") long subjectCode) throws MyEntityNotFoundException {
        teacherBean.associateTeacherToSubject(username, subjectCode);
        return Response.status(Response.Status.CREATED).build();
    }
}
