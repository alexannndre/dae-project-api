package pt.ipleiria.estg.dei.ei.dae.daeproject.entities;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllExperts",
                query = "SELECT e FROM Expert e ORDER BY e.name" // JPQL
        )
})
public class Expert extends User implements Serializable {

    public Expert() {
    }

    public Expert(String username, String password, String name, String email, String office) {
        super(username, password, name, email);
    }

}
