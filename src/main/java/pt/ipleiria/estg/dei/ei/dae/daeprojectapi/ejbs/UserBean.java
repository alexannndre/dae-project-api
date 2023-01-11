package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.User;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public User find(String vat) {
        return em.find(User.class, vat);
    }

    public User findOrFail(String vat) {
        var user = em.getReference(User.class, vat);
        Hibernate.initialize(user);
        return user;
    }

    public boolean canLogin(String vat, String password) {
        var user = find(vat);
        System.out.println("User: " + user);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }
}
