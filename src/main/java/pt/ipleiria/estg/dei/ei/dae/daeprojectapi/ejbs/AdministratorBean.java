package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AdministratorBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public void create(String vat, String name, String email, String password) {
        var admin = new Administrator(vat, name, email, hasher.hash(password));
        em.persist(admin);
    }

    public Administrator find(String vat) {
        return em.find(Administrator.class, vat);
    }

    public Administrator findOrFail(String vat) {
        var admin = em.getReference(Administrator.class, vat);
        Hibernate.initialize(admin);
        return admin;
    }
}
