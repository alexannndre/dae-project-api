package pt.ipleiria.estg.dei.ei.dae.academics.dtos.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

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

    public void create(String username, String password, String name, String email) {
        Administrator admin = new Administrator(username, hasher.hash(password), name, email);
        em.persist(admin);
    }
}
