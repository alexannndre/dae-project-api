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

    private String type;

    private List<String> covers;

    private Customer customer;

    private List<Occurrence> occurrences;

    private boolean repairable;

    public Policy() {
        this.occurrences = new LinkedList<>();
        this.covers = new LinkedList<>();
    }

    public Policy(String code, String insurerCompany, String type, Customer customer, boolean repairable) {
        this();
        this.code = code;
        this.insurerCompany = insurerCompany;
        this.type = type;
        this.customer = customer;
        this.repairable = repairable;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<String> getCovers() {
        return covers;
    }

    public void setCovers(List<String> covers) {
        this.covers = covers;
    }
    public void addCover(String cover) {
        if (!this.covers.contains(cover))
            this.covers.add(cover);
    }

    public void removeCover(String cover) {
        this.covers.remove(cover);
    }

    public boolean isRepairable() {
        return repairable;
    }

    public void setRepairable(boolean repairable) {
        this.repairable = repairable;
    }
}
