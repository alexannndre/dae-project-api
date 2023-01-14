package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.create;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.RepairerDTO;

import javax.validation.constraints.NotNull;

public class RepairerCreateDTO extends RepairerDTO {
    @NotNull
    private String password;

    public RepairerCreateDTO() {
    }

    public RepairerCreateDTO(String vat, String name, String email, String password) {
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
