package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public Student find(String username) throws MyEntityNotFoundException {
        Student student = em.find(Student.class, username);
        if (student == null)
            throw new MyEntityNotFoundException("Student with username " + username + " not found.");
        return student;
    }

    public Student findWithSubjects(String username) throws MyEntityNotFoundException {
        Student student = em.find(Student.class, username);
        if (student == null)
            throw new MyEntityNotFoundException("Student with username " + username + " not found.");
        Hibernate.initialize(student.getSubjects());
        return student;
    }

    public boolean exists(String username) {
        Query query = em.createQuery(
                "SELECT COUNT(s.username) FROM Student s WHERE s.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0;
    }

    public void create(String username, String password, String name, String email, long courseCode) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        if (exists(username))
            throw new MyEntityExistsException("Student with username " + username + " already exists.");
        Course course = em.find(Course.class, courseCode);
        if (course == null)
            throw new MyEntityNotFoundException("ERROR_COURSE_NOT_FOUND: " + courseCode);
        try {
            Student student = new Student(username, hasher.hash(password), name, email, course);
            course.addStudent(student);
            em.persist(student);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Student> getAllStudents() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Student>) em.createNamedQuery("getAllStudents").getResultList();
    }

    public void update(String username, String password, String name, String email, long courseCode) throws MyEntityNotFoundException {
        Student student = em.find(Student.class, username);

        if (student == null)
            throw new MyEntityNotFoundException("ERROR_STUDENT_NOT_FOUND: " + username);

        em.lock(student, LockModeType.OPTIMISTIC);

        if (password != null && !password.isEmpty())
            student.setPassword(password);
        if (name != null && !name.isEmpty())
            student.setName(name);
        if (email != null && !email.isEmpty())
            student.setEmail(email);
        if (!Objects.equals(student.getCourse().getCode(), courseCode)) {
            Course course = em.find(Course.class, courseCode);
            if (course == null)
                throw new MyEntityNotFoundException("ERROR_COURSE_NOT_FOUND: " + courseCode);
            student.setCourse(course);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Student student = em.find(Student.class, username);
        if (student == null)
            throw new MyEntityNotFoundException("ERROR_STUDENT_NOT_FOUND: " + username);
        for (Subject subject : student.getSubjects())
            subject.removeStudent(student);
        student.getCourse().removeStudent(student);
        em.remove(student);
    }

    public void enrollStudentInSubject(String username, long subjectCode) throws MyEntityNotFoundException {
        Student student = em.find(Student.class, username);
        if (student == null)
            throw new MyEntityNotFoundException("ERROR_STUDENT_NOT_FOUND: " + username);

        Subject subject = em.find(Subject.class, subjectCode);
        if (subject == null)
            throw new MyEntityNotFoundException("ERROR_SUBJECT_NOT_FOUND: " + subjectCode);

        if (student.getSubjects().contains(subject)) {
            subject.removeStudent(student);
            student.removeSubject(subject);
            return;
        }

        subject.addStudent(student);
        student.addSubject(subject);
    }

    public List<Subject> getStudentSubjects(String username) throws MyEntityNotFoundException {
        Student student = em.find(Student.class, username);
        if (student == null)
            throw new MyEntityNotFoundException("ERROR_STUDENT_NOT_FOUND: " + username);
        Hibernate.initialize(student.getSubjects());
        return student.getSubjects();
    }
}
