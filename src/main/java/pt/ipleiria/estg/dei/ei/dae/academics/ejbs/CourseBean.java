package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class CourseBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long code, String name) {
        Course course = new Course(code, name);
        em.persist(course);
    }

    public List<Course> getAllCourses() {
        // remember, maps to: “SELECT c FROM Course c ORDER BY c.name”
        return (List<Course>) em.createNamedQuery("getAllCourses").getResultList();
    }

    public Course find(long code) {
        return em.find(Course.class, code);
    }

}
