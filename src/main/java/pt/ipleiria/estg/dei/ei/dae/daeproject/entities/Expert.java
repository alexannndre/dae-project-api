package pt.ipleiria.estg.dei.ei.dae.daeproject.entities;

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
@Table(name = "experts")
public class Expert extends User implements Serializable {

    public Expert() {
    }

    public Expert(int id, String name, String email, String password) {
        super(id, password, name, email);
    }

}
