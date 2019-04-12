package com.iamds.rcms.client.examples;

import com.iamds.rcms.client.baseClient.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Example5 {

    /*
    *** EXAMPLE 5 ***
    - This example retrieves a list of all user vehicles
    - The ID of the first vehicle of the vehicle list is then used for the next step
    - Retrieve all charging events for the vehicle ID from the previous step
    - As example result this Example prints out all charging events with time, start SoC, end SoC
     */
    public static void example5(ApiEndpoint apiEndpoint, String email, String password) {
        System.out.println("--- EXAMPLE 5 ---");
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

            System.out.print("Retrieve all chargingEvents for the first vehicle in the vehicle list...");
            JSONObject firstV = (JSONObject)(vResp.jsonArray.get(0));
            params.clear();
            ActionResponse singlevResp = AuthService.runAction(apiEndpoint, authToken, "v1/vehicle/"+firstV.get("id")+"/chargingEvents", ActionTypes.GET, params, null);
            System.out.println("Done!");
            JSONArray chargingEvents = (singlevResp.jsonArray);
            for(Object chargingEvent : chargingEvents) {
                JSONObject cEvent = (JSONObject) chargingEvent;
                System.out.println("Charged at " + cEvent.get("starttime") + " from " + cEvent.get("startsoc") + "% to " + cEvent.get("endsoc") +"%");
            }

        } catch (RcmsClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--- END OF EXAMPLE 5 ---");
    }
}
