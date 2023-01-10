package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllCustomers",
                query = "SELECT c FROM Customer c ORDER BY c.name" // JPQL
        )
})
//@Table(name = "customers")
public class Customer extends User implements Serializable {
    @OneToMany(mappedBy = "customer")
    private List<Occurrence> occurrences;

    public Customer() {
        this.occurrences = new LinkedList<>();
    }

    public Customer(String nif, String name, String email, String password) {
        super(nif, name, email, password);
        this.occurrences = new LinkedList<>();
    }

    public List<Occurrence> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(List<Occurrence> occurrences) {
        this.occurrences = occurrences;
    }

    public void addOccurrence(Occurrence occurrence) {
        if (!this.occurrences.contains(occurrence))
            this.occurrences.add(occurrence);
    }
}
