package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDTO implements Serializable {
    private String nif, name, password, email;

    public CustomerDTO() {
    }

    public CustomerDTO(String nif, String name, String password, String email) {
        this.nif = nif;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
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
                customer.getNif(),
                customer.getName(),
                customer.getPassword(),
                customer.getEmail()
        );
    }

    public static List<CustomerDTO> from(List<Customer> customers) {
        return customers.stream().map(CustomerDTO::from).collect(Collectors.toList());
    }
}
