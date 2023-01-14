package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ServiceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ServiceBean {
    @PersistenceContext
    private EntityManager em;

    public void create(ServiceDTO servDto){
        Service ss = this.create(servDto.getName(), servDto.getEmail(), servDto.getType(), servDto.isOfficial());
        ss.setCreatorVat(servDto.getCreatorVat());
    }

    public Service create(String name, String email, String type) {
        return create(name, email, type, true);
    }

    public Service create(String name, String email, String type, boolean isOfficial) {
        var service = new Service(name, email, type, isOfficial);

        em.persist(service);
        return service;
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
