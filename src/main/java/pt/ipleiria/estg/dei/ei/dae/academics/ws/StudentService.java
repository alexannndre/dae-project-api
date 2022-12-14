package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

@Path("students") // relative url web path for this service
@Produces({APPLICATION_JSON})  // injects header “Content-Type: application/json”
@Consumes({APPLICATION_JSON})  // injects header “Accept: application/json”
@Authenticated
@RolesAllowed({"Teacher", "Administrator"})
public class StudentService {
    @EJB
    private StudentBean studentBean;
    @EJB
    private EmailBean emailBean;

    @Context
    private SecurityContext securityContext;

    // converts an entity Student to a DTO Student class
    private StudentDTO toDTONoSubjects(Student student) {
        return new StudentDTO(
                student.getUsername(),
                student.getPassword(),
                student.getName(),
                student.getEmail(),
                student.getCourse().getName(),
                student.getCourse().getCode()
        );
    }

    private StudentDTO toDTO(Student student) {
        StudentDTO dto = toDTONoSubjects(student);
        dto.setSubjects(student.getSubjects().stream().map(this::toDTO).collect(Collectors.toList()));
        return dto;
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
    private List<StudentDTO> toDTOsNoSubjects(List<Student> students) {
        return students.stream().map(this::toDTONoSubjects).collect(Collectors.toList());
    }

    private List<StudentDTO> toDTOs(List<Student> students) {
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/")  // means: the relative url path is “/api/students/”
    public List<StudentDTO> getAllStudentsWS() {
        return toDTOsNoSubjects(studentBean.getAllStudents());
    }

    @POST
    @Path("/")
    public Response create(StudentDTO studentDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );
//        Student student = studentBean.find(studentDTO.getUsername());
        return Response.status(CREATED).build();
    }

    @GET
    @Authenticated
    @RolesAllowed({"Student"})
    @Path("{username}")
    public Response getStudentDetailsWS(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (!principal.getName().equals(username))
            return Response.status(FORBIDDEN).build();

        Student student = studentBean.findWithSubjects(username);
        return Response.ok(toDTO(student)).build();
    }

    @GET
    @Authenticated
    @RolesAllowed({"Student", "Teacher", "Administrator"})
    @Path("{username}/subjects")
    public Response getStudentSubjectsWS(@PathParam("username") String username) throws MyEntityNotFoundException {
        var principal = securityContext.getUserPrincipal();

        if (!principal.getName().equals(username) && !securityContext.isUserInRole("Teacher") && !securityContext.isUserInRole("Administrator"))
            return Response.status(FORBIDDEN).build();

        List<Subject> subjects = studentBean.getStudentSubjects(username);
        return Response.ok(subjectsToDTOs(subjects)).build();
    }

    @PUT
    @Path("{username}")
    public Response update(@PathParam("username") String username, StudentDTO studentDTO) throws MyEntityNotFoundException {
        studentBean.update(
                username,
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );
        return Response.ok().build();
    }

    @DELETE
    @Path("{username}")
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException {
        studentBean.delete(username);
        return Response.ok().build();
    }

    @POST
    @Path("{username}/subjects/{subjectCode}")
    public Response enrollStudentInSubjectWS(@PathParam("username") String username, @PathParam("subjectCode") long subjectCode) throws MyEntityNotFoundException {
        studentBean.enrollStudentInSubject(username, subjectCode);
        return Response.status(CREATED).build();
    }

    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email) throws MyEntityNotFoundException, MessagingException {
        Student student = studentBean.find(username);
        if (student == null)
            throw new MyEntityNotFoundException("Student with username '" + username + "' not found in our records.");
        emailBean.send(student.getEmail(), email.getSubject(), email.getMessage());
        return Response.status(OK).entity("E-mail sent").build();
    }
}
