package pt.ipleiria.estg.dei.ei.dae.daeprojectapi.managers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.ejbs.CustomerBean;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.entities.Customer;
import pt.ipleiria.estg.dei.ei.dae.daeprojectapi.pojos.Policy;


import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Singleton
public class PolicyManager {

    private static final String API_URL = "https://63beea09585bedcb36ba824f.mockapi.io/api";

    private static void processJsonValue(List<Policy> policies, CustomerBean cb, JsonValue jsonValue) {
        var jsonObject = jsonValue.asJsonObject();
        var policy = new Policy();
        System.out.println(jsonObject.getString("customer_vat"));
        policy.setCustomer(cb.findOrFail(jsonObject.getString("customer_vat")));
        policy.setCode(jsonObject.getString("code"));
        policy.setCode(jsonObject.getString("code"));
        policy.setInsurerCompany(jsonObject.getString("insurer_company"));
        policy.setType(jsonObject.getString("type"));
        jsonObject.getJsonArray("covers").forEach(coverJsonValue -> policy.addCover(coverJsonValue.toString()));
        System.out.println("Policy: " + policy.getCode() + " " + jsonValue);
        policies.add(policy);
    }

    private static List<Policy> retrievePolicies(String args, CustomerBean cb){
        List<Policy> policies = new LinkedList<>();

        OkHttpClient client = new OkHttpClient();

        var url = String.format("%s/policies?%s",API_URL,args);

        var request = new Request.Builder()
                .url(url)
                .build();

        try (var response = client.newCall(request).execute()) {
            var jsonStr = Objects.requireNonNull(response.body()).string();
            //Deserialize json
            JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
            jsonReader.readArray().forEach(jsonValue -> processJsonValue(policies, cb, jsonValue));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return policies;
    }

    public static List<Policy> getAllPolicies(CustomerBean cb){
        return retrievePolicies("", cb);
    }

    public static List<Policy> getPoliciesByVat(CustomerBean cb, String vat){
        return retrievePolicies("customer_vat="+vat, cb);
    }

    public static List<Policy> getPoliciesByCode(CustomerBean cb, String code){ return retrievePolicies("code="+code, cb); }
}
