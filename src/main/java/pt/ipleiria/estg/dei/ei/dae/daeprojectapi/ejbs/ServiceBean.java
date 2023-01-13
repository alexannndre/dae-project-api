package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Repairer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Service;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static javax.persistence.LockModeType.OPTIMISTIC;
import static pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status.PENDING;

@Stateless
public class ServiceBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    RepairerBean repairerBean;

    public void create(String name, String type) {
        var service = new Service(name, type);

        em.persist(service);
    }

    public Service find(Long id) {
        return em.find(Service.class, id);
    }

    public Service findOrFail(Long id) {
        var service = em.getReference(Service.class, id);
        Hibernate.initialize(service);
        return service;
    }

    public List<Service> getAllServices() {
        return (List<Service>) em.createNamedQuery("getAllServices").getResultList();
    }

    public List<Service> getServicesByType(String type) {
        return (List<Service>) em.createNamedQuery("getServicesByType").setParameter("type", type).getResultList();
    }

}