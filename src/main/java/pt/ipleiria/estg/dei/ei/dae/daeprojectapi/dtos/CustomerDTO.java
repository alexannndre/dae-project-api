package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDTO implements Serializable {
    private String vat, name, password, email;

    public CustomerDTO() {
    }

    public CustomerDTO(String vat, String name, String password, String email) {
        this.vat = vat;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                customer.getPassword(),
                customer.getEmail()
        );
    }

    public static List<CustomerDTO> from(List<Customer> customers) {
        return customers.stream().map(CustomerDTO::from).collect(Collectors.toList());
    }
}
