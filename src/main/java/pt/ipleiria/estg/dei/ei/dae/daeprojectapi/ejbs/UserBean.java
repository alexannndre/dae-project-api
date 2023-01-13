package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.User;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import static javax.persistence.LockModeType.OPTIMISTIC;

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
        return user != null && user.getPassword().equals(hasher.hash(password));
    }

    public void update(String vat, String name, String email) {
        var user = findOrFail(vat);

        em.lock(user, OPTIMISTIC);

        if (name != null && !name.isEmpty())
            user.setName(name);

        if (email != null && !email.isEmpty())
            user.setEmail(email);
    }

    public void updatePassword(String vat, String password) {
        var user = findOrFail(vat);
        user.setPassword(hasher.hash(password));
    }
}
