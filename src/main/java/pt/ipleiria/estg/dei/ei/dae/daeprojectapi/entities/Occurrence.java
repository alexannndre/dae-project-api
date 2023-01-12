package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "occurrences")
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllOccurrences",
                query = "SELECT o FROM Occurrence o"
        ),
        @NamedQuery(
                name = "getCustomerOccurrences",
                query = "SELECT o FROM Occurrence o WHERE o.customer.vat = :vat"
        ),
        @NamedQuery(
                name = "getAllPendingOccurrences",
                query = "SELECT o FROM Occurrence o WHERE o.status = 'PENDING'"
        ),
        @NamedQuery(
                name = "getAllOccurrencesByPolicy",
                query = "SELECT o FROM Occurrence o WHERE o.policy=:policy"
        )
})
public class Occurrence extends Versionable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String description, policy;

    @NotNull
    @Enumerated(STRING)
    private Status status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "occurrence")
    private List<Document> documents;

    public Occurrence() {
        this.documents = new LinkedList<>();
    }

    public Occurrence(String description, String policy, Status status, Customer customer) {
        this.description = description;
        this.policy = policy;
        this.status = status;
        this.customer = customer;
        this.documents = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocument(Document document) {
        if (!this.documents.contains(document))
            this.documents.add(document);
    }

    public void removeDocument(Document document) {
        this.documents.remove(document);
    }

    public void approve(){
        if(this.status == Status.PENDING)
            this.status = Status.APPROVED;
        else
            throw new IllegalStateException("Occurrence is not pending");
    }

    public void reject(){
        if(this.status == Status.PENDING)
            this.status = Status.REJECTED;
        else
            throw new IllegalStateException("Occurrence is not pending");
    }
}
