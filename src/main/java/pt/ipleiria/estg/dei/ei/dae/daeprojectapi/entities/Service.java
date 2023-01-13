package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "services")
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllServices",
                query = "SELECT s FROM Service s"
        ),
        @NamedQuery(
                name = "getServicesByType",
                query = "SELECT s FROM Service s WHERE s.type=:type"
        )
})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String type;


    public Service() {
    }

    public Service(String name, String type) {
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
