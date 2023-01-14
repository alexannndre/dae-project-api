package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.enums.Status;
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

    public Customer find(String vat) {
        var customer = em.find(Customer.class, vat);
        return customer;
    }

    public Customer findOrFail(String vat) {
        var customer = em.getReference(Customer.class, vat);
        Hibernate.initialize(customer);
        return customer;
    }

    public Customer findWithOccurrences(String vat) {
        var customer = em.find(Customer.class, vat);
        Hibernate.initialize(customer.getOccurrences());
        return customer;
    }

    public boolean existsVat(String vat) {
        var query = em.createQuery("SELECT COUNT (c.vat) FROM Customer c WHERE c.vat = :vat", Long.class);
        query.setParameter("vat", vat);
        return query.getSingleResult() > 0;
    }

    public boolean existsEmail(String email) {
        var query = em.createQuery("SELECT COUNT (c.email) FROM Customer c WHERE c.email = :email", Long.class);
        query.setParameter("email", email);
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

    public void create(String vat, String name, String email, String password) {
        if (existsVat(vat))
            throw new EntityExistsException("Customer with vat " + vat + " already exists.");

        if (existsEmail(email))
            throw new EntityExistsException("Customer with email " + email + " already exists.");

        if (vat == null || vat.isEmpty() || name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException("Customer must have a vat, name, email and password.");

        var customer = new Customer(vat, name, email, hasher.hash(password));
        em.persist(customer);
    }

    public Occurrence getOccurrence(String vat) {
        return em.find(Occurrence.class, vat);
    }

    public List<Occurrence> getOccurrences(String vat) {
        var occurrences = findOrFail(vat).getOccurrences();
        Hibernate.initialize(occurrences);
        return occurrences;
    }

    public List<Occurrence> getOccurrencesByStatus(String vat, Status status) {
        var occurrences = findOrFail(vat).getOccurrences();
        Hibernate.initialize(occurrences);
        occurrences.removeIf(occurrence -> !occurrence.getStatus().equals(status));
        return occurrences;
    }
}
