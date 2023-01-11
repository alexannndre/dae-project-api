package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.pojos.Policy;


import java.util.LinkedList;
import java.util.List;

public class PolicyManager {

    public List<Policy> policies;

    public PolicyManager() {
        this.policies = new LinkedList<>();
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void addPolicy(Policy policy){
        if(!policies.contains(policy))
            policies.add(policy);
    }

    public void removePolicy(Policy policy){
        policies.remove(policy);
    }
}
