package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ExpertDTO implements Serializable {
    @NotNull
    private String nif, name, password, email;

    public ExpertDTO() {
    }

    public ExpertDTO(String nif, String name, String password, String email) {
        this.nif = nif;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
