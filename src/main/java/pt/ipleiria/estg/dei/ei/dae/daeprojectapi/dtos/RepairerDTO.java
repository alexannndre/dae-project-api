package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Expert;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Repairer;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class RepairerDTO implements Serializable {
    @NotNull
    private String vat;

    @NotNull
    private String name;

    @NotNull
    private String email;

    public RepairerDTO() {
    }

    public RepairerDTO(String vat, String name, String email) {
        this.vat = vat;
        this.name = name;
        this.email = email;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
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

    public static RepairerDTO from(Repairer repairer) {
        return new RepairerDTO(
                repairer.getVat(),
                repairer.getName(),
                repairer.getEmail()
        );
    }

    public static List<RepairerDTO> from(List<Repairer> repairers) {
        return repairers.stream().map(RepairerDTO::from).collect(Collectors.toList());
    }
}
