package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Expert;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Repairer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class RepairerBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public Repairer find(String vat) {
        return em.find(Repairer.class, vat);
    }

    public Repairer findOrFail(String vat) {
        var repairer = em.getReference(Repairer.class, vat);
        Hibernate.initialize(repairer);
        return repairer;
    }

    public void create(String vat, String name, String email, String password) {
        if (existsVat(vat))
            throw new EntityExistsException("Repairer with vat " + vat + " already exists.");

        if (existsEmail(email))
            throw new EntityExistsException("Repairer with email " + email + " already exists.");

        if (vat == null || vat.isEmpty() || name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException("Repairer must have a vat, name, email and password.");

        var repairer = new Repairer(vat, name, email, hasher.hash(password));
        em.persist(repairer);
    }

    public boolean existsVat(String vat) {
        var query = em.createQuery("SELECT COUNT (r.vat) FROM Repairer r WHERE r.vat = :vat", Long.class);
        query.setParameter("vat", vat);
        return query.getSingleResult() > 0;
    }

    public boolean existsEmail(String email) {
        var query = em.createQuery("SELECT COUNT (r.email) FROM Repairer r WHERE r.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    public Long count() {
        return em.createQuery("SELECT COUNT (*) FROM " + Repairer.class.getSimpleName(), Long.class).getSingleResult();
    }

}
