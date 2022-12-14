package pt.ipleiria.estg.dei.ei.dae.daeproject.entities;

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

    private int numeroFiscal;

    public Customer() {
    }

    public Customer(int id, String name, String email, String password, int numeroFiscal) {
        super(id, name, password, email);
        this.numeroFiscal = numeroFiscal;
    }

    public int getNumeroFiscal() {
        return numeroFiscal;
    }

    public void setNumeroFiscal(int numeroFiscal) {
        this.numeroFiscal = numeroFiscal;
    }
}
