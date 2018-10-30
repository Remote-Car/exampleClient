package com.iamds.rcms.client.examples;

import com.iamds.rcms.client.baseClient.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Example7 {

    /*
    *** EXAMPLE 7 ***
    - This example shows how to add a new vehicle to the api
     */
    public static void example7(ApiEndpoint apiEndpoint, String email, String password, String oemLoginEmail, String oemLoginPassword, OemTypes oemType, String vin, EnergyTypes energyType) {
        System.out.println("--- EXAMPLE 7 ---");
        try {
            HashMap<String, String> params = new HashMap<>();

            // STEP 0: Login
            System.out.print("Retrieving authToken...");
            AuthToken authToken = AuthService.getAuthToken(apiEndpoint, email, password);
            System.out.println("Done!");


            // STEP 1: Retrieve all already added OEM-Logins from the API
            ActionResponse apiResponse = AuthService.runAction(apiEndpoint, authToken, "v1/oemapilogin", ActionTypes.GET, params, null);
            if(apiResponse.httpCode != 200)
                throw new Exception("Error(STEP 1) retrieving OEM-Login-List: "+apiResponse.jsonObject.toJSONString());


            // STEP 1.1: Check if the OEM-Login for the Vehicle we are adding is already in the system
            Integer finalOemApiLoginId = null;
            for (Object tmp : apiResponse.jsonArray) {
                JSONObject oemApiLogin = (JSONObject) tmp;
                if (oemApiLogin.get("credentialsuser").equals(oemLoginEmail) && oemApiLogin.get("oemapitype").equals(oemType.name())) {
                    finalOemApiLoginId = ((Long) oemApiLogin.get("id")).intValue();
                    System.out.println("OEM-Login is already in system... we do not need to add it a second time");
                    break;
                }
            }

            // STEP 2: Add OEM-Login (Only required if the Login is not already present in the system)
            if (finalOemApiLoginId == null) {
                System.out.println("OEM-Login is not in system... adding new OEM-Login...");
                params.clear();
                params.put("credentialsuser", oemLoginEmail);
                params.put("credentials2", oemLoginPassword);
                params.put("oemapitype", oemType.name());
                apiResponse = AuthService.runAction(apiEndpoint, authToken, "v1/oemapilogin", ActionTypes.POST, params, null);
                if(apiResponse.httpCode != 200)
                    throw new Exception("Error(STEP 2) add OEM-Login: "+apiResponse.jsonObject.toJSONString());
                finalOemApiLoginId = ((Long) apiResponse.jsonObject.get("id")).intValue();
            }

            // STEP 3: Retrieve vehicle-list for OEM-Login
            params.clear();
            params.put("oemapiloginid", finalOemApiLoginId + "");
            params.put("includeVehicles", "true");
            apiResponse = AuthService.runAction(apiEndpoint, authToken, "v1/oemapilogin", ActionTypes.GET, params, null);
            if(apiResponse.httpCode != 200)
                throw new Exception("Error(STEP 3) retrieve OEM-Login vehicle list: "+apiResponse.jsonObject.toJSONString());

            // STEP 3.1: Check if the vehicle we want to add is part of the OEM-Login and is not already added to the system
            JSONObject oemApiVehicleData = null;
            JSONArray oemApiVehicles = (JSONArray) apiResponse.jsonObject.get("apiVehicleList");
            for (Object tmp : oemApiVehicles) {
                JSONObject oemApiVehicle = (JSONObject) tmp;
                if (oemApiVehicle.get("vin").equals(vin)) {
                    if ((Boolean) oemApiVehicle.get("isAlreadyInSystem") == true) {
                        throw new Exception("Error: Vehicle is already present in our system");
                    } else if ((Boolean) oemApiVehicle.get("notVerifiedInOemSystem") == true) {
                        throw new Exception("Error: Vehicle is not verified in your OEM(" + oemType.name() + ")-Account");
                    } else {
                        oemApiVehicleData = oemApiVehicle;
                        break;
                    }
                }
            }
            if (oemApiVehicleData == null)
                throw new Exception("Error: The vehicle VIN was not found in the OEM-Api-Login.");

            // STEP 4: add vehicle
            params.clear();
            params.put("oemapiloginid", finalOemApiLoginId + "");
            params.put("name", oemApiVehicleData.get("name") + "");
            params.put("oemvehicleid", oemApiVehicleData.get("oemApiId") + "");
            params.put("vin", oemApiVehicleData.get("vin") + "");
            params.put("energytype", energyType.name());
            params.put("oemtype", oemType.name());
            apiResponse = AuthService.runAction(apiEndpoint, authToken, "v1/vehicle", ActionTypes.POST, params, null);
            if(apiResponse.httpCode != 200)
                throw new Exception("Error(STEP 4) add vehicle: "+apiResponse.jsonObject.toJSONString());

            // STEP 4.1: Print out add vehicle response (inlcudes ID)
            System.out.println("Response: "+apiResponse.jsonObject.toJSONString());


        } catch (RcmsClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--- END OF EXAMPLE 7 ---");
    }
}
