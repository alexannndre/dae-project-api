package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import java.util.Objects;

public class RepairService {
    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;


    @NotNull
    private Occurrence occurrence;

    public RepairService() {
    }

    public RepairService(Long id, String name, String description, Occurrence occurrence) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.occurrence = occurrence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }
}
