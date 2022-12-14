package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("subjects")
@Produces({APPLICATION_JSON})
@Consumes({APPLICATION_JSON})
public class SubjectService {
    @EJB
    private SubjectBean subjectBean;

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

    // converts an entity Student to a DTO Student class
    private StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getUsername(),
                student.getPassword(),
                student.getName(),
                student.getEmail(),
                student.getCourse().getName(),
                student.getCourse().getCode()
        );
    }

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

    // converts an entire list of entities into a list of DTOs
    private List<SubjectDTO> toDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<StudentDTO> studentsToDTOs(List<Student> students) {
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<TeacherDTO> teachersToDTOs(List<Teacher> teachers) {
        return teachers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<SubjectDTO> getAllSubjectsWS() {
        return toDTOs(subjectBean.getAllSubjects());
    }

    @GET
    @Path("/{code}")
    public Response getSubjectByCodeWS(@PathParam("code") long code) {
        Subject subject = subjectBean.find(code);
        if (subject == null)
            return Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_SUBJECT").build();
        return Response.ok(toDTO(subject)).build();
    }

    @GET
    @Path("/{code}/students")
    public Response getStudentsBySubjectCodeWS(@PathParam("code") long code) {
        Subject subject = subjectBean.find(code);
        if (subject == null)
            return Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_SUBJECT").build();
        return Response.ok(studentsToDTOs(subject.getStudents())).build();
    }

    @GET
    @Path("/{code}/teachers")
    public Response getTeachersBySubjectCodeWS(@PathParam("code") long code) {
        Subject subject = subjectBean.find(code);
        if (subject == null)
            return Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_SUBJECT").build();
        return Response.ok(teachersToDTOs(subject.getTeachers())).build();
    }
}
