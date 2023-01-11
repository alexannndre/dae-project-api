package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ExpertDTO implements Serializable {
    @NotNull
    private String vat, name, password, email;

    public ExpertDTO() {
    }

    public ExpertDTO(String vat, String name, String password, String email) {
        this.vat = vat;
        this.name = name;
        this.password = password;
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
