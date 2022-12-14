package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.User;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

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

    public User find(String username) throws MyEntityNotFoundException {
        var user = em.find(User.class, username);
        if (user == null)
            throw new MyEntityNotFoundException("User with username " + username + " not found.");
        return user;
    }

    public User findOrFail(String username) {
        var user = em.getReference(User.class, username);
        Hibernate.initialize(user);
        return user;
    }

    public boolean canLogin(String username, String password) throws MyEntityNotFoundException {
        var user = find(username);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }
}
