package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentBean {
    @PersistenceContext
    private EntityManager em;

    public Document create(String filepath, String filename, int occurrenceId) {
        var occurrence = em.find(Occurrence.class, occurrenceId);
        var document = new Document(filepath, filename, occurrence);
        em.persist(document);
        return document;
    }

    public Document find(Long id) {
        return em.find(Document.class, id);
    }

    public List<Document> getOccurrenceDocuments(int occurrenceId) {
        return (List<Document>) em.createNamedQuery("getOccurrenceDocuments")
                .setParameter("id", occurrenceId)
                .getResultList();
    }
}
