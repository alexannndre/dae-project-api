package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class StatusDTO implements Serializable {

    private Status status;

    public StatusDTO() {}

    public StatusDTO(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static StatusDTO from(Status status) {
        return new StatusDTO(status);
    }

    public static List<StatusDTO> from(List<Status> statuses) {
        return statuses.stream().map(StatusDTO::from).collect(Collectors.toList());
    }
}
