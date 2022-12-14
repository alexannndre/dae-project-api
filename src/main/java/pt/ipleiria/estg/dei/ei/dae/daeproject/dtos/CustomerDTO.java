package pt.ipleiria.estg.dei.ei.dae.daeproject.dtos;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CustomerDTO implements Serializable {
    @NotNull
    private int id;
    @NotNull
    private String name, password, email, taxNumber;

    public CustomerDTO() {
    }

    public CustomerDTO(int id, String name, String password, String email, String taxNumber) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.taxNumber = taxNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
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

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
}
