package pt.ipleiria.estg.dei.ei.dae.daeproject.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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

    public Customer() {
    }

    public Customer(int id, String name, String email, String password, int taxNumber) {
        super(id, name, password, email);
        this.taxNumber = taxNumber;
    }

    public int getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(int taxNumber) {
        this.taxNumber = taxNumber;
    }
}
