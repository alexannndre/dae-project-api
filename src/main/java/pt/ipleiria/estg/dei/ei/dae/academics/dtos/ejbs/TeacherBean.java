package pt.ipleiria.estg.dei.ei.dae.academics.dtos.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TeacherBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public Teacher find(String username) {
        return em.find(Teacher.class, username);
    }

    public void create(String username, String password, String name, String email, String office) {
        Teacher teacher = new Teacher(username, hasher.hash(password), name, email, office);
        if (em.find(Teacher.class, username) != null)
            throw new IllegalArgumentException("Teacher already exists");
        em.persist(teacher);
    }

    public List<Teacher> getAllTeachers() {
        // remember, maps to: “SELECT t FROM Teacher t ORDER BY t.name”
        return (List<Teacher>) em.createNamedQuery("getAllTeachers").getResultList();
    }

    public void associateTeacherToSubject(String username, long subjectCode) throws MyEntityNotFoundException {
        Teacher teacher = em.find(Teacher.class, username);
        if (teacher == null)
            throw new MyEntityNotFoundException("ERROR_TEACHER_NOT_FOUND: " + username);
        Subject subject = em.find(Subject.class, subjectCode);
        if (subject == null)
            throw new MyEntityNotFoundException("ERROR_SUBJECT_NOT_FOUND: " + subjectCode);

        if (teacher.getSubjects().contains(subject)) {
            subject.removeTeacher(teacher);
            teacher.removeSubject(subject);
            return;
        }

        subject.addTeacher(teacher);
        teacher.addSubject(subject);
    }
}
