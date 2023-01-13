package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceDTO implements Serializable {
    private long id;
    private String name;
    private String type;
    private String creatorVat;
    private boolean isOfficial;

    public ServiceDTO() {
    }

    public ServiceDTO(long id, String name, String type, String creatorVat, boolean isOfficial) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.creatorVat = creatorVat;
        this.isOfficial = isOfficial;
    }

    public String getCreatorVat() {
        return creatorVat;
    }

    public void setCreatorVat(String creatorVat) {
        this.creatorVat = creatorVat;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ServiceDTO toDTO(Service repairService) {
        return new ServiceDTO(
                repairService.getId(),
                repairService.getName(),
                repairService.getType(),
                repairService.getCreatorVat(),
                repairService.isOfficialService()
        );
    }

    public static List<ServiceDTO> toServiceDTOs(List<Service> repairServices) {
        return repairServices.stream().map(ServiceDTO::toDTO).collect(Collectors.toList());
    }
}
