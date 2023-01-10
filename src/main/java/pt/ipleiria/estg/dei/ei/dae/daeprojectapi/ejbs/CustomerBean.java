package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CustomerBean {
    @PersistenceContext
    private EntityManager em;

    public Customer find(String nif) {
        Customer customer = em.find(Customer.class, nif);
//        if (customer == null)
//            throw new MyEntityNotFoundException("Customer with nif " + nif + " not found.");
        return customer;
    }

    public Customer findWithOccurrences(String nif) {
        Customer customer = em.find(Customer.class, nif);
//        if (customer == null)
//            throw new MyEntityNotFoundException("Customer with nif " + nif + " not found.");
        Hibernate.initialize(customer.getOccurrences());
        return customer;
    }

    public boolean exists(String nif) {
        var query = em.createQuery("SELECT COUNT (c.nif) FROM Customer c WHERE c.nif = :nif", Long.class);
        query.setParameter("nif", nif);
        return (Long) query.getSingleResult() > 0;
    }

    public void create(String nif, String name, String email, String password) {
//        if (exists(nif))
//            throw new MyEntityExistsException("Customer with nif " + nif + " already exists.");
        try {
            Customer customer = new Customer(nif, name, email, password);
            em.persist(customer);
        } catch (Exception e) {
//            throw new MyConstraintViolationException(e);
        }
    }
}
