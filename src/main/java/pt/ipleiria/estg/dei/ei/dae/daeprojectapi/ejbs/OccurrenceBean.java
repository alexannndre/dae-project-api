package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static javax.persistence.LockModeType.OPTIMISTIC;

@Stateless
public class OccurrenceBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    CustomerBean customerBean = new CustomerBean();

    public void create(String description, Status status, String customerNif) {
        var customer = customerBean.findOrFail(customerNif);
        var occurrence = new Occurrence(description, status, customer);

        em.persist(occurrence);
        customer.addOccurrence(occurrence);
    }

    public Occurrence find(Long id) {
        return em.find(Occurrence.class, id);
    }

    public Occurrence findOrFail(Long id) {
        var occurrence = em.getReference(Occurrence.class, id);
        Hibernate.initialize(occurrence);
        return occurrence;
    }

    public List<Occurrence> getAllOccurrences() {
        return (List<Occurrence>) em.createNamedQuery("getAllOccurrences").getResultList();
    }

    // TODO: Deve ser possível alterar o customer associado?
    public void update(Long id, String description, Status status) {
        var occurrence = findOrFail(id);

        em.lock(occurrence, OPTIMISTIC);

        if (description != null && !description.isEmpty())
            occurrence.setDescription(description);

        if (status != null)
            occurrence.setStatus(status);
    }

}
