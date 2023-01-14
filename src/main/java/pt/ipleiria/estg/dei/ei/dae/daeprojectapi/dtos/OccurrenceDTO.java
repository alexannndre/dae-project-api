package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.enums.Status;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class OccurrenceDTO implements Serializable {
    @NotNull
    private Long id;

    private Long serviceId;
    @NotNull
    private String description, policy, customerVat;

    private String expertVat, repairerVat;

    @NotNull
    private Status status;

    public OccurrenceDTO() {
    }

    public OccurrenceDTO(Long id, String description, String policy, Status status, String customerVat, String expertVat, Long serviceId, String repairerVat) {
        this.id = id;
        this.description = description;
        this.policy = policy;
        this.status = status;
        this.customerVat = customerVat;
        this.expertVat = expertVat;
        this.serviceId = serviceId;
        this.repairerVat = repairerVat;
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

    public String getCustomerVat() {
        return customerVat;
    }

    public void setCustomerVat(String customerVat) {
        this.customerVat = customerVat;
    }

    public String getExpertVat() {
        return expertVat;
    }

    public void setExpertVat(String expertVat) {
        this.expertVat = expertVat;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getRepairerVat() {
        return repairerVat;
    }

    public void setRepairerVat(String repairerVat) {
        this.repairerVat = repairerVat;
    }

    public static OccurrenceDTO from(Occurrence occurrence) {
        return new OccurrenceDTO(
                occurrence.getId(),
                occurrence.getDescription(),
                occurrence.getPolicy(),
                occurrence.getStatus(),
                occurrence.getCustomer().getVat(),
                occurrence.getExpertVat(),
                occurrence.getServiceId(),
                occurrence.getRepairerVat()
        );
    }

    public static List<OccurrenceDTO> from(List<Occurrence> occurrences) {
        return occurrences.stream().map(OccurrenceDTO::from).collect(Collectors.toList());
    }
}
