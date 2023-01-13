package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class Service {
    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String type;


    public Service() {
    }

    public Service(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
