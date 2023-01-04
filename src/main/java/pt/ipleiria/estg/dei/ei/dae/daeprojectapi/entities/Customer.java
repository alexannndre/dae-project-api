package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@Table(name = "customers")
public class Customer extends User implements Serializable {
    @NotNull
    private int taxNumber;

    //    @OneToMany(mappedBy = "customer")
//    private List<Occurrence> occurrences;

    public Customer() {
//        this.occurrences = new LinkedList<>();
    }

    public Customer(int id, String name, String email, String password, int taxNumber) {
        super(id, name, password, email);
        this.taxNumber = taxNumber;
//        this.occurrences = new LinkedList<>();
    }

    public int getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(int taxNumber) {
        this.taxNumber = taxNumber;
    }
}
