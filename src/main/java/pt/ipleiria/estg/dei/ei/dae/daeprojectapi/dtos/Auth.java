package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class Auth implements Serializable {
    @NotBlank
    private String vat;

    @NotBlank
    private String password;

    public Auth() {}

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
