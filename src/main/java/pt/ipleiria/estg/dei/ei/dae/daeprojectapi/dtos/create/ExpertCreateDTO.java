package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.create;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.ExpertDTO;

import javax.validation.constraints.NotNull;

public class ExpertCreateDTO extends ExpertDTO {
    @NotNull
    private String password;

    public ExpertCreateDTO() {
    }

    public ExpertCreateDTO(String vat, String name, String email, String password) {
        super(vat, name, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
