package com.iamds.rcms.client.examples;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.iamds.rcms.client.baseClient.ActionResponse;
import com.iamds.rcms.client.baseClient.ActionTypes;
import com.iamds.rcms.client.baseClient.ApiEndpoint;
import com.iamds.rcms.client.baseClient.AuthService;
import com.iamds.rcms.client.baseClient.AuthToken;
import com.iamds.rcms.client.baseClient.RcmsClientException;

public class Example2 {

    /*
    *** EXAMPLE 2 ***
    - This example retrieves a list of all user vehicles and then requests(in a second step) the vehicle including vehicleState for the first vehicle in the list of vehicles
     */
    public static void example2(ApiEndpoint apiEndpoint, String email, String password) {
        System.out.println("--- EXAMPLE 2 ---");
        try {
            HashMap<String, String> params = new HashMap<>();

            System.out.print("Retrieving authToken...");
            AuthToken authToken = AuthService.getAuthToken(apiEndpoint, email, password);
            System.out.println("Done!");

            System.out.print("Retrieving Vehicle List...");
            // Uncomment following line, if you want to include the vehicles the user has been granted read rights to.
            //params.put("includeAccessGrantVehicles", "true");
            ActionResponse vResp = AuthService.runAction(apiEndpoint, authToken, "v1/vehicle", ActionTypes.GET, params, null);
            System.out.println("Done!");
            System.out.println("Response "+vResp.httpCode+": "+vResp.jsonArray.toJSONString());

            System.out.print("Retrieving first vehicle(including vehicle state) from previous response...");
            JSONObject firstV = (JSONObject)(vResp.jsonArray.get(0));
            params.clear();
            params.put("vehicleid", ((Long)firstV.get("id"))+"");
            // we are settings 'withVehicleState' to true here in order to retrieve all vehicle information and not just basic meta data
            params.put("withVehicleState", "true");
            ActionResponse singlevResp = AuthService.runAction(apiEndpoint, authToken, "v1/vehicle", ActionTypes.GET, params, null);
            System.out.println("Done!");
            System.out.println("Response "+singlevResp.httpCode+": "+singlevResp.jsonObject.toJSONString());
            JSONObject vehicleState = (JSONObject)(singlevResp.jsonObject.get("vehicleState"));
            System.out.println("Vehicle SoC: " + vehicleState.get("soc") );

        } catch (RcmsClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--- END OF EXAMPLE 2 ---");
    }

}
