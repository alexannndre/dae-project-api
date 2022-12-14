package pt.ipleiria.estg.dei.ei.dae.daeproject.dtos;

import java.io.Serializable;

public class CustomerDTO implements Serializable {
    private int id;
    private String username;
    private String password;
    private String email;
    private String taxNumber;

    public CustomerDTO() {
    }

    public CustomerDTO(int id, String username, String password, String email, String taxNumber) {
        this.id = id;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
