package com.iamds.rcms.client.examples;

import com.iamds.rcms.client.baseClient.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Example4 {

    /*
    *** EXAMPLE 4 ***
    - This example retrieves a list of all user vehicles
    - The ID of the first vehicle of the vehicle list is then used for the next step
    - Retrieve vehicle history of one whole week for the vehicle ID from the previous step
    - As example result this Example prints out all the recorded 'state of charge'-values for the given week
     */
    public static void example4(ApiEndpoint apiEndpoint, String email, String password) {
        System.out.println("--- EXAMPLE 4 ---");
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

            System.out.print("Retrieve vehicleHistory of one week for the first vehicle in the vehicle list...");
            JSONObject firstV = (JSONObject)(vResp.jsonArray.get(0));
            params.clear();
            params.put("timeFrom", "2018-10-22T08:00:00.001Z");
            params.put("timeTo", "2018-10-28T23:59:59.999Z");
            ActionResponse singlevResp = AuthService.runAction(apiEndpoint, authToken, "v1/vehicle/"+firstV.get("id")+"/history", ActionTypes.GET, params, null);
            System.out.println("Done!");
            JSONArray vehicleHistory = (singlevResp.jsonArray);
            for(Object vehicleState : vehicleHistory) {
                JSONObject vState = (JSONObject) vehicleState;
                System.out.println("Vehicle SoC (" + vState.get("time") + "): " + vState.get("soc") );
            }

        } catch (RcmsClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--- END OF EXAMPLE 4 ---");
    }
}
