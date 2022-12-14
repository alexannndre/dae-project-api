package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Document;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentDTO implements Serializable {
    private Long id;
    private String filename;

    public DocumentDTO() {
    }

    public DocumentDTO(Long id, String filename) {
        this.id = id;
        this.filename = filename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public static DocumentDTO from(Document document) {
        return new DocumentDTO(document.getId(), document.getFilename());
    }

    public static List<DocumentDTO> from(List<Document> documents) {
        return documents.stream().map(DocumentDTO::from).collect(Collectors.toList());
    }
}
