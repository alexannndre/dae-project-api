package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.Entity;

@Entity
public class Repairer extends User {

    public Repairer() {
    }

    public Repairer(String vat, String name, String email, String password) {
        super(vat, name, email, password);
    }
}
