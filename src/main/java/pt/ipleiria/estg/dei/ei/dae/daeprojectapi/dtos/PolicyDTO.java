package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.dtos;

import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.pojos.Policy;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class PolicyDTO implements Serializable {
    @NotNull
    private String code;

    @NotNull
    private String insurerCompany;

    @NotNull
    private String type;

    @NotNull
    private List<String> covers;

    @NotNull
    private boolean repairable;

    public PolicyDTO() {
    }

    public PolicyDTO(String code, String insurerCompany, String type, List<String> covers, boolean repairable) {
        this.code = code;
        this.insurerCompany = insurerCompany;
        this.type = type;
        this.covers = covers;
        this.repairable = repairable;
    }

    public boolean isRepairable() {
        return repairable;
    }

    public void setRepairable(boolean repairable) {
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

    public List<String> getCovers() {
        return covers;
    }

    public void setCovers(List<String> covers) {
        this.covers = covers;
    }

    public static PolicyDTO from(Policy policy) {
        return new PolicyDTO(
                policy.getCode(),
                policy.getInsurerCompany(),
                policy.getType(),
                policy.getCovers(),
                policy.isRepairable()
        );
    }

    public static List<PolicyDTO> from(List<Policy> policies) {
        return policies.stream().map(PolicyDTO::from).collect(Collectors.toList());
    }
}
