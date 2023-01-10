package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentDTO implements Serializable {
    private Long id;
    private String filename;
    private String filepath;
    private Long occurrenceId;

    public DocumentDTO() {
    }

    public DocumentDTO(Long id, String filename, String filepath, Long occurrenceId) {
        this.id = id;
        this.filename = filename;
        this.filepath = filepath;
        this.occurrenceId = occurrenceId;
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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Long getOccurrenceId() {
        return occurrenceId;
    }

    public void setOccurrenceId(Long occurrenceId) {
        this.occurrenceId = occurrenceId;
    }

    public static DocumentDTO from(Document document) {
        return new DocumentDTO(document.getId(), document.getFilename(), document.getFilepath(), document.getOccurrence().getId());
    }

    public static List<DocumentDTO> from(List<Document> documents) {
        return documents.stream().map(DocumentDTO::from).collect(Collectors.toList());
    }
}
