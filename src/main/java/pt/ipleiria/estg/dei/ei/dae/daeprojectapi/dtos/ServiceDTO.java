package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceDTO implements Serializable {
    private long id;
    private String name;
    private String type;

    public ServiceDTO() {
    }

    public ServiceDTO(long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public static ServiceDTO toDTO(ServiceDTO repairService) {
        return new ServiceDTO(
                repairService.getId(),
                repairService.getName(),
                repairService.getType()
        );
    }

    public static List<ServiceDTO> toServiceDTOs(List<ServiceDTO> repairServices) {
        return repairServices.stream().map(ServiceDTO::toDTO).collect(Collectors.toList());
    }
}
