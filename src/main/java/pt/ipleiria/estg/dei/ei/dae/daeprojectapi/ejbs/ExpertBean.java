package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Expert;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ExpertBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public Expert find(String vat) {
        var expert = em.find(Expert.class, vat);
        return expert;
    }

    public Expert findOrFail(String vat) {
        var expert = em.getReference(Expert.class, vat);
        Hibernate.initialize(expert);
        return expert;
    }

    public void create(String vat, String name, String email, String password) {
        if (existsVat(vat))
            throw new EntityExistsException("Expert with vat " + vat + " already exists.");

        if (existsEmail(email))
            throw new EntityExistsException("Expert with email " + email + " already exists.");

        if (vat == null || vat.isEmpty() || name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException("Expert must have a vat, name, email and password.");

        var expert = new Expert(vat, name, email, hasher.hash(password));
        em.persist(expert);
    }

    public List<Expert> getAll(int offset, int limit) {
        return em.createNamedQuery("getAllExperts", Expert.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public boolean existsVat(String vat) {
        var query = em.createQuery("SELECT COUNT (e.vat) FROM Expert e WHERE e.vat = :vat", Long.class);
        query.setParameter("vat", vat);
        return query.getSingleResult() > 0;
    }

    public boolean existsEmail(String email) {
        var query = em.createQuery("SELECT COUNT (e.email) FROM Expert e WHERE e.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    public Long count() {
        return em.createQuery("SELECT COUNT (*) FROM " + Expert.class.getSimpleName(), Long.class).getSingleResult();
    }

}
