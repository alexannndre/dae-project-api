package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Status;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    public Occurrence find(int id) {
        return em.find(Occurrence.class, id);
    }

    public List<Occurrence> getAllOccurrences() {
        return (List<Occurrence>) em.createNamedQuery("getAllOccurrences").getResultList();
    }

}
