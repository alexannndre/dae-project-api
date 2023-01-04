package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllOccurrences",
                query = "SELECT o FROM Occurrence o" // JPQL
        )
})
@Table(name = "occurrences")
public class Occurrence extends Versionable implements Serializable {
    @Id
    private int id;
    @NotNull
    private String description, status;
    @OneToMany(mappedBy = "occurrence")
    private List<Document> documents;

    public Occurrence() {
        this.documents = new LinkedList<>();
    }

    public Occurrence(int id, String description, String status) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.documents = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
