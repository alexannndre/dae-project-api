package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.security.PrivateKey;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDTO implements Serializable {
    @NotNull
    private String vat;

    @NotNull
    private String name;

    @NotNull
    private String email;

    public CustomerDTO() {
    }

    public CustomerDTO(String vat, String name, String email) {
        this.vat = vat;
        this.name = name;
        this.email = email;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static CustomerDTO from(Customer customer) {
        return new CustomerDTO(
                customer.getVat(),
                customer.getName(),
                customer.getEmail()
        );
    }

    public static List<CustomerDTO> from(List<Customer> customers) {
        return customers.stream().map(CustomerDTO::from).collect(Collectors.toList());
    }
}
