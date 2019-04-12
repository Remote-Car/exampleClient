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

public class Example3 {

    /*
    *** EXAMPLE 3 ***
    - This example retrieves all vehicles(including the ones with read-right only) and includes the complete vehicleState for all vehicles
    - The result/output of this example is a list VIN numbers including current 'State of Charge' value
     */
    public static void example3(ApiEndpoint apiEndpoint, String email, String password) {
        System.out.println("--- EXAMPLE 3 ---");
        try {
            HashMap<String, String> params = new HashMap<>();

            System.out.print("Retrieving authToken...");
            AuthToken authToken = AuthService.getAuthToken(apiEndpoint, email, password);
            System.out.println("Done!");

            System.out.print("Retrieving Vehicle List...");
            params.put("includeAccessGrantVehicles", "true");
            // we are settings all 'with*' to true here in order to retrieve all vehicle information and not just basic meta data
            params.put("withBase", "true");
            params.put("withRemoteFunctionsState", "true");
            params.put("withLockState", "true");
            params.put("withClimaState", "true");
            params.put("withDrivingState", "true");
            params.put("withGeoState", "true");
            params.put("withBatteryState", "true");
            params.put("includeAccessGrantVehicles", "true");
            ActionResponse vResp = AuthService.runAction(apiEndpoint, authToken, "v1/vehicle", ActionTypes.GET, params, null);
            System.out.println("Done!");

            for(Object v : vResp.jsonArray) {
                JSONObject vehicle = (JSONObject) v;
                String vin = (String) vehicle.get("vin");

                JSONObject baseState = (JSONObject) vehicle.get("base");
                if(baseState != null) {
                    Double soc = (Double) baseState.get("soc");
                    System.out.println("Vehicle(" + vin + "): " + soc + "%");
                }
            }

        } catch (RcmsClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--- END OF EXAMPLE 3 ---");
    }

}
