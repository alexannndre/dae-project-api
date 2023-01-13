package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class RepairServiceDTO implements Serializable {
    private long id;
    private String name;
    private String description;
    private Occurrence occurrence;

    public RepairServiceDTO() {
    }

    public RepairServiceDTO(long id, String name, String description, Occurrence occurrence) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.occurrence = occurrence;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public static RepairServiceDTO toDTO(RepairServiceDTO repairService) {
        return new RepairServiceDTO(
                repairService.getId(),
                repairService.getName(),
                repairService.getDescription(),
                repairService.getOccurrence()
        );
    }

    public static List<RepairServiceDTO> toRepairServiceDTOs(List<RepairServiceDTO> repairServices) {
        return repairServices.stream().map(RepairServiceDTO::toDTO).collect(Collectors.toList());
    }
}
