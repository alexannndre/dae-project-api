package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.pojos.Policy;


import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PolicyManager {

    @EJB
    CustomerBean customerBean = new CustomerBean();

    private final String API_URL = "https://63beea09585bedcb36ba824f.mockapi.io/api";

    public List<Policy> policies;

    public PolicyManager() {
        this.policies = new LinkedList<>();
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    private void addPolicy(Policy policy){
        if(!policies.contains(policy))
            policies.add(policy);
    }

    private void removePolicy(Policy policy){
        policies.remove(policy);
    }

    public void retrievePolicies(){
        OkHttpClient client = new OkHttpClient();

        var url = String.format("%s/policies",API_URL);

        var request = new Request.Builder()
                .url(url)
                .build();

        try (var response = client.newCall(request).execute()) {
            var jsonStr = Objects.requireNonNull(response.body()).string();
            //Deserialize json
            JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
            jsonReader.readArray().forEach(jsonValue -> {
                var jsonObject = jsonValue.asJsonObject();
                var policy = new Policy();
                policy.setCustomer(customerBean.findOrFail(jsonObject.getString("customer_vat")));
                policy.setCode(jsonObject.getString("code"));
                policy.setCode(jsonObject.getString("code"));
                policy.setInsurerCompany(jsonObject.getString("insurer_company"));
                policy.setType(jsonObject.getString("type"));
                jsonObject.getJsonArray("covers").forEach(coverJsonValue -> policy.addCover(coverJsonValue.toString()));

                addPolicy(policy);
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
