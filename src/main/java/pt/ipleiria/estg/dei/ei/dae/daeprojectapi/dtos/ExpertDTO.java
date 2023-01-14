package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Expert;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ExpertDTO implements Serializable {
    @NotNull
    private String vat;

    @NotNull
    private String name;

    @NotNull
    private String email;

    public ExpertDTO() {
    }

    public ExpertDTO(String vat, String name, String email) {
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

    public static ExpertDTO from(Expert expert) {
        return new ExpertDTO(
                expert.getVat(),
                expert.getName(),
                expert.getEmail()
        );
    }

    public static List<ExpertDTO> from(List<Expert> experts) {
        return experts.stream().map(ExpertDTO::from).collect(Collectors.toList());
    }
}
