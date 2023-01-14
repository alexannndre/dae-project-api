package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.LAZY;

@Entity
@NamedQueries(value = {
        @NamedQuery(
                name = "getAllCustomers",
                query = "SELECT c FROM Customer c ORDER BY c.name"
        ),
})
public class Customer extends User implements Serializable {
    @OneToMany(mappedBy = "customer", fetch = LAZY, cascade = REMOVE)
    private List<Occurrence> occurrences;

    public Customer() {
        this.occurrences = new LinkedList<>();
    }

    public Customer(String vat, String name, String email, String password) {
        super(vat, name, email, password);
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
