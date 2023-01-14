package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ServiceDTO;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static javax.persistence.LockModeType.OPTIMISTIC;
import static pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status.*;

@Stateless
public class OccurrenceBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    CustomerBean customerBean = new CustomerBean();
    @EJB
    ExpertBean expertBean;
    @EJB
    ServiceBean serviceBean;
    @EJB
    RepairerBean repairerBean;

    public Long create(String description, String policy, Status status, String customerVat) {
        var customer = customerBean.findOrFail(customerVat);
        var occurrence = new Occurrence(description, policy, status, customer);

        em.persist(occurrence);
        customer.addOccurrence(occurrence);
        return occurrence.getId();
    }

    public void create(String description, String policy, Status status, String customerVat, String expertVat) {
        var customer = customerBean.findOrFail(customerVat);
        var expert = expertBean.findOrFail(expertVat);
        var occurrence = new Occurrence(description, policy, status, customer);
        occurrence.setExpert(expert);
        em.persist(occurrence);
        customer.addOccurrence(occurrence);
    }

    public void create(String description, String policy, Status status, String customerVat, String expertVat, Long serviceId) {
        var customer = customerBean.findOrFail(customerVat);
        var expert = expertBean.findOrFail(expertVat);
        var occurrence = new Occurrence(description, policy, status, customer);
        occurrence.setExpert(expert);
        occurrence.setService(serviceBean.findOrFail(serviceId));
        em.persist(occurrence);
        customer.addOccurrence(occurrence);
    }

    public void create(String description, String policy, Status status, String customerVat, String expertVat, Long serviceId, String repairerVat) {
        var customer = customerBean.findOrFail(customerVat);
        var expert = expertBean.findOrFail(expertVat);
        var occurrence = new Occurrence(description, policy, status, customer);
        occurrence.setExpert(expert);
        occurrence.setService(serviceBean.findOrFail(serviceId));
        var repairer = repairerBean.findOrFail(repairerVat);
        occurrence.setRepairer(repairer);
        em.persist(occurrence);
        customer.addOccurrence(occurrence);
    }

    public Long create(String description, String policy, String customerVat) {
        return create(description, policy, PENDING, customerVat);
    }

    public Occurrence find(Long id) {
        return em.find(Occurrence.class, id);
    }

    public Occurrence findOrFail(Long id) {
        var occurrence = em.getReference(Occurrence.class, id);
        Hibernate.initialize(occurrence);
        return occurrence;
    }

    public List<Occurrence> getAllOccurrences() {
        return (List<Occurrence>) em.createNamedQuery("getAllOccurrences").getResultList();
    }

    public List<Occurrence> getAllPendingOccurrences() {
        return (List<Occurrence>) em.createNamedQuery("getAllPendingOccurrences").getResultList();
    }

    public List<Occurrence> getAllApprovedOccurrences() {
        return (List<Occurrence>) em.createNamedQuery("getAllApprovedOccurrences").getResultList();
    }

    public List<Occurrence> getAllRepairingOccurrences() {
        return (List<Occurrence>) em.createNamedQuery("getAllRepairingOccurrences").getResultList();
    }

    public void update(Long id, String description, String policy) {
        var occurrence = findOrFail(id);

        em.lock(occurrence, OPTIMISTIC);

        if (description != null && !description.isEmpty())
            occurrence.setDescription(description);

        if (policy != null && !policy.isEmpty())
            occurrence.setPolicy(policy);
    }

    public void remove(Long id) {
        var occurrence = findOrFail(id);
        for (Document document : occurrence.getDocuments())
            document.setOccurrence(null);
        em.remove(occurrence);
    }

    public void approve(Long id, String expertVat) {
        var occurrence = findOrFail(id);
        em.lock(occurrence, OPTIMISTIC);
        var expert = expertBean.findOrFail(expertVat);
        occurrence.approve(expert);
    }

    public void reject(Long id, String expertVat) {
        var occurrence = findOrFail(id);
        em.lock(occurrence, OPTIMISTIC);
        var expert = expertBean.findOrFail(expertVat);
        occurrence.reject(expert);
    }


    public List<Occurrence> getOccurrencesByPolicy(String policyCode) {
        return (List<Occurrence>) em.createNamedQuery("getAllOccurrencesByPolicy").setParameter("policy", policyCode).getResultList();
    }

    public void chooseService(Long id, ServiceDTO serviceDTO) {
        var occurrence = findOrFail(id);
        em.lock(occurrence, OPTIMISTIC);

        if (occurrence.getStatus() != APPROVED)
            throw new IllegalArgumentException("This occurrence is not approved for repair");

        var service = serviceBean.find(serviceDTO.getId());
        var pol = occurrence.getPolicyInstance();

        if (service == null) {
            service = serviceBean.create(serviceDTO.getName(), serviceDTO.getEmail(), pol.getType(), false);
            service.setCreatorVat(pol.getCustomer().getVat());
        }

        if (!service.getType().equals(pol.getType()))
            throw new IllegalArgumentException("This repair service is not compatible with this policy type");

        occurrence.setService(service);
        occurrence.setStatus(REPAIRING);
    }

    public void solve(Long id, String repairerVal) {
        var occurrence = findOrFail(id);
        em.lock(occurrence, OPTIMISTIC);

        // if (occurrence.getStatus() != Status.REPAIRING)
        //   throw new IllegalArgumentException("This occurrence is not repairing");
        // A verificação acima já é realizada no occurrence.solve()

        var repairer = repairerBean.findOrFail(repairerVal);
        occurrence.solve(repairer);
    }

}
