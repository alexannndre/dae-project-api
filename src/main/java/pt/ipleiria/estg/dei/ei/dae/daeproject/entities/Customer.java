package pt.ipleiria.estg.dei.ei.dae.daeproject.entities;


import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllCustomers",
                query = "SELECT c FROM Customer c ORDER BY c.name" // JPQL
        )
})
public class Customer extends User implements Serializable {

    public Customer() {
    }

    public Customer(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
    }
}
