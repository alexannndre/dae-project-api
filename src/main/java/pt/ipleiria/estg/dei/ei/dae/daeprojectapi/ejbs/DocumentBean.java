package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentBean {
    @EJB
    private OccurrenceBean occurrenceBean;

    @EJB
    private UserBean userBean;

    @PersistenceContext
    private EntityManager em;

    public Document create(String filepath, String filename, Long occurrenceId, String vat) {
        var occurrence = occurrenceBean.findOrFail(occurrenceId);
        var user = userBean.findOrFail(vat);
        var document = new Document(filepath, filename, occurrence, user);

        em.persist(document);
        occurrence.addDocument(document);

        return document;
    }

    public Document find(Long id) {
        return em.find(Document.class, id);
    }

    public Document findOrFail(Long id) {
        var document = em.getReference(Document.class, id);
        Hibernate.initialize(document);
        return document;
    }

    public List<Document> getOccurrenceDocuments(Long occurrenceId) {
        return em.createNamedQuery("getOccurrenceDocuments", Document.class).setParameter("id", occurrenceId).getResultList();
    }
}
