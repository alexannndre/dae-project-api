package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SubjectBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long code, String name, Course course, int courseYear, int scholarYear) {
        Subject subject = new Subject(code, name, course, courseYear, scholarYear);
        em.persist(subject);
    }

    public List<Subject> getAllSubjects() {
        // remember, maps to: “SELECT s FROM Subject s ORDER BY s.course, s.scholar_year DESC, s.name”
        return (List<Subject>) em.createNamedQuery("getAllSubjects").getResultList();
    }

    public Subject find(long code) {
        return em.find(Subject.class, code);
    }
}
