package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OccurrenceBean {
    @PersistenceContext
    private EntityManager em;

    public Occurrence create(int version, String description, String status) {
        var occurrence = new Occurrence(version, description, status);
        em.persist(occurrence);
        return occurrence;
    }

    public Occurrence find(int id) {
        return em.find(Occurrence.class, id);
    }

    public List<Occurrence> getAllOccurrences() {
        return (List<Occurrence>) em.createNamedQuery("getAllOccurrences").getResultList();
    }

}
