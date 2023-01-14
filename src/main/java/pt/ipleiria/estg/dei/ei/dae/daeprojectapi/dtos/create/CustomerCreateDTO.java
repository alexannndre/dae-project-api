package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.create;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos.CustomerDTO;

import javax.validation.constraints.NotNull;

public class CustomerCreateDTO extends CustomerDTO {
    @NotNull
    private String password;

    public CustomerCreateDTO() {
    }

    public CustomerCreateDTO(String vat, String name, String email, String password) {
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
