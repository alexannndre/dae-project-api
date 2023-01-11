package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CustomerBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public Customer find(String nif) {
        var customer = em.find(Customer.class, nif);
        return customer;
    }

    public Customer findOrFail(String nif) {
        var customer = em.getReference(Customer.class, nif);
        Hibernate.initialize(customer);
        return customer;
    }

    public Customer findWithOccurrences(String nif) {
        var customer = em.find(Customer.class, nif);
        Hibernate.initialize(customer.getOccurrences());
        return customer;
    }

    public boolean exists(String nif) {
        var query = em.createQuery("SELECT COUNT (c.nif) FROM Customer c WHERE c.nif = :nif", Long.class);
        query.setParameter("nif", nif);
        return query.getSingleResult() > 0;
    }

    public List<Customer> getAll(int offset, int limit) {
        return em.createNamedQuery("getAllCustomers", Customer.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long count() {
        return em.createQuery("SELECT COUNT (*) FROM " + Customer.class.getSimpleName(), Long.class).getSingleResult();
    }

    public void create(String nif, String name, String email, String password) {
        if (exists(nif))
            throw new EntityExistsException("Customer with nif " + nif + " already exists.");

        if (nif == null || nif.isEmpty() || name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException("Customer must have a nif, name, email and password.");

        var customer = new Customer(nif, name, email, hasher.hash(password));
        em.persist(customer);
    }

    public Occurrence getOccurrence(String nif) {
        return em.find(Occurrence.class, nif);
    }

    public List<Occurrence> getOccurrences(String nif) {
        var occurrences = findOrFail(nif).getOccurrences();
        Hibernate.initialize(occurrences);
        return occurrences;
    }
}
