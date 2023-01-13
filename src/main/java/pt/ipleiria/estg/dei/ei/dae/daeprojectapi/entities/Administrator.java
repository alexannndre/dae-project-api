package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.Entity;

@Entity
public class Administrator extends User {

    public Administrator() {
    }

    public Administrator(String vat, String name, String email, String password) {
        super(vat, name, email, password);
    }
}
