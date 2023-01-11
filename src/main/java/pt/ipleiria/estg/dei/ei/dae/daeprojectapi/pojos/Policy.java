package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.pojos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Occurrence;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Policy implements Serializable {

    private String code;

    private String insurerCompany;

    private Status status;

    private Customer customer;

    private List<Occurrence> occurrences;

    public Policy() {
        this.occurrences = new LinkedList<>();
    }

    public Policy(String code, String insurerCompany, Status status, Customer customer) {
        this.code = code;
        this.insurerCompany = insurerCompany;
        this.status = status;
        this.customer = customer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInsurerCompany() {
        return insurerCompany;
    }

    public void setInsurerCompany(String insurerCompany) {
        this.insurerCompany = insurerCompany;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public void removeOccurrence(Occurrence occurrence) {
        this.occurrences.remove(occurrence);
    }
}
