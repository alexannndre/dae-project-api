package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllExperts",
                query = "SELECT e FROM Expert e ORDER BY e.name" // JPQL
        )
})
//@Table(name = "experts")
public class Expert extends User implements Serializable {

    public Expert() {
    }

    public Expert(String nif, String name, String email, String password) {
        super(nif, name, email, password);
    }

}
