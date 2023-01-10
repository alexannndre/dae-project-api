package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CustomerDTO implements Serializable {
    private String nif, name, password, email;
    private List<OccurrenceDTO> occurrences;

    public CustomerDTO() {
        this.occurrences = new LinkedList<>();
    }

    public CustomerDTO(String nif, String name, String password, String email) {
        this.nif = nif;
        this.name = name;
        this.password = password;
        this.email = email;
        this.occurrences = new LinkedList<>();
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

    public List<OccurrenceDTO> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(List<OccurrenceDTO> occurrences) {
        this.occurrences = occurrences;
    }
}
