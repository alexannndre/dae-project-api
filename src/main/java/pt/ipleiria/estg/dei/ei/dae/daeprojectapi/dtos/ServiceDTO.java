package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Service;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceDTO implements Serializable {
    @NotNull
    private long id;
    
    @NotNull
    private String name;
    
    @NotNull
    private String email;
    
    @NotNull
    private String type;
    
    private String creatorVat;
    
    private boolean isOfficial;

    public ServiceDTO() {
    }

    public ServiceDTO(long id, String name, String email, String type, String creatorVat, boolean isOfficial) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                repairService.getEmail(),
                repairService.getType(),
                repairService.getCreatorVat(),
                repairService.isOfficial()
        );
    }

    public static List<ServiceDTO> toServiceDTOs(List<Service> repairServices) {
        return repairServices.stream().map(ServiceDTO::toDTO).collect(Collectors.toList());
    }
}
