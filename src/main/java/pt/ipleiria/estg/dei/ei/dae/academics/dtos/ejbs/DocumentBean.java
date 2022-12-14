package pt.ipleiria.estg.dei.ei.dae.academics.dtos.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DocumentBean {
    @PersistenceContext
    private EntityManager em;

    public Document create(String filepath, String filename, String username) {
        var student = em.find(Student.class, username);
        var document = new Document(filepath, filename, student);
        em.persist(document);
        return document;
    }

    public Document find(Long id) {
        return em.find(Document.class, id);
    }

    public List<Document> getStudentDocuments(String username) {
        return (List<Document>) em.createNamedQuery("getStudentDocuments")
                .setParameter("username", username)
                .getResultList();
    }
}
