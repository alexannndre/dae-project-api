package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class OccurrenceDTO implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String description, policy;

    @NotNull
    private Status status;

    public OccurrenceDTO() {
    }

    public OccurrenceDTO(Long id, String description, String policy, Status status) {
        this.id = id;
        this.description = description;
        this.policy = policy;
        this.status = status;
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

    public static OccurrenceDTO from(Occurrence occurrence) {
        return new OccurrenceDTO(
                occurrence.getId(),
                occurrence.getDescription(),
                occurrence.getPolicy(),
                occurrence.getStatus()
        );
    }

    public static List<OccurrenceDTO> from(List<Occurrence> occurrences) {
        return occurrences.stream().map(OccurrenceDTO::from).collect(Collectors.toList());
    }
}
