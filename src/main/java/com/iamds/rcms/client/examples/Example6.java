package com.iamds.rcms.client.examples;

import com.iamds.rcms.client.baseClient.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Example6 {

    /*
    *** EXAMPLE 6 ***
    - This example retrieves a list of all user vehicles
    - The ID of the first vehicle of the vehicle list is then used for the next step
    - Retrieve all driving events for the vehicle ID from the previous step
    - As example result this Example prints out all driving events with starttime, distance
     */
    public static void example6(ApiEndpoint apiEndpoint, String email, String password) {
        System.out.println("--- EXAMPLE 6 ---");
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

            System.out.print("Retrieve all drivingEvents for the first vehicle in the vehicle list...");
            JSONObject firstV = (JSONObject)(vResp.jsonArray.get(0));
            params.clear();
            params.put("vehicleid", ((Long)firstV.get("id"))+"");
            ActionResponse singlevResp = AuthService.runAction(apiEndpoint, authToken, "v1/vehicle/drivingEvents", ActionTypes.GET, params, null);
            System.out.println("Done!");
            JSONArray drivingEvents = (singlevResp.jsonArray);
            for(Object drivingEvent : drivingEvents) {
                JSONObject dEvent = (JSONObject) drivingEvent;
                System.out.println("Drove " + ((Long)dEvent.get("endmileage")-(Long)dEvent.get("startmileage")) + "km at " + dEvent.get("starttime"));
            }

        } catch (RcmsClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--- END OF EXAMPLE 6 ---");
    }
}
