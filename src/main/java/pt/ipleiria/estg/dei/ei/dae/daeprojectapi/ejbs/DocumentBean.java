package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentBean {
    @EJB
    private OccurrenceBean occurrenceBean;

    @PersistenceContext
    private EntityManager em;

    public Document create(String filepath, String filename, Long occurrenceId) {
        var occurrence = occurrenceBean.findOrFail(occurrenceId);
        var document = new Document(filepath, filename, occurrence);

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
