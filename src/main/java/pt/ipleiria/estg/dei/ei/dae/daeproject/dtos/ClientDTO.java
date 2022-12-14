package pt.ipleiria.estg.dei.ei.dae.daeproject.dtos;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.DocumentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ClientDTO implements Serializable {
    private String username, password, name, email, courseName;

    public ClientDTO() {
    }

    public ClientDTO(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
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
}
