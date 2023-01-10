package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDTO implements Serializable {
    @NotNull
    private String nif, username, name, email, role;

    public UserDTO() {}

    public UserDTO(String nif, String username, String name, String email, String role) {
        this.nif = nif;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
