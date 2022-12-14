package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

@Singleton
@Startup
public class ConfigBean {
    @EJB
    StudentBean studentBean = new StudentBean();
    @EJB
    TeacherBean teacherBean = new TeacherBean();
    @EJB
    AdministratorBean administratorBean = new AdministratorBean();
    @EJB
    CourseBean courseBean = new CourseBean();
    @EJB
    SubjectBean subjectBean = new SubjectBean();
    @EJB
    DocumentBean documentBean = new DocumentBean();

    private static final Logger LOGGER = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void PopulateDB() throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        System.out.println("Hello Java EE!");

        courseBean.create(1234, "Computer Science");
        courseBean.create(5678, "Mathematics");

        subjectBean.create(1234, "Programming", courseBean.find(1234), 1, 1);
        subjectBean.create(5678, "Algebra", courseBean.find(5678), 1, 1);
        subjectBean.create(9012, "Calculus", courseBean.find(5678), 1, 1);
        subjectBean.create(3456, "Databases", courseBean.find(1234), 1, 1);
        subjectBean.create(7890, "Operating Systems", courseBean.find(1234), 1, 1);

        studentBean.create("joao", "12345", "João", "joao@mail.com", 1234);
        studentBean.create("maria", "12345", "Maria", "maria@mail.com", 5678);
        studentBean.create("pedro", "12345", "Pedro", "pedro@mail.com", 1234);

        try {
            studentBean.create("joao", "12345", "João", "joao@mail.com", 1234);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

        try {
            studentBean.create("marta", "12345", "Marta", "marta@mail.com", 9999);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

        studentBean.enrollStudentInSubject("joao", 1234);
        studentBean.enrollStudentInSubject("joao", 5678);
        studentBean.enrollStudentInSubject("maria", 5678);
        studentBean.enrollStudentInSubject("maria", 9012);
        studentBean.enrollStudentInSubject("maria", 3456);
        studentBean.enrollStudentInSubject("pedro", 1234);
        studentBean.enrollStudentInSubject("pedro", 9012);

        teacherBean.create("joaquim", "12345", "Joaquim", "joaquim@mail.com", "A1");
        teacherBean.create("mariana", "23456", "Mariana", "mariana@mail.com", "B2");
        teacherBean.create("miguel", "34567", "Miguel", "miguel@mail.com", "C3");

        teacherBean.associateTeacherToSubject("joaquim", 1234);
        teacherBean.associateTeacherToSubject("joaquim", 5678);
        teacherBean.associateTeacherToSubject("mariana", 9012);
        teacherBean.associateTeacherToSubject("mariana", 3456);
        teacherBean.associateTeacherToSubject("miguel", 7890);

        administratorBean.create("admin", "12345", "Admin", "admin@admin.com");
        administratorBean.create("root", "12345", "Root", "root@root.com");

        documentBean.create("/opt/jboss/uploads/joao/doc2.txt", "doc2.txt", "joao");
    }
}
