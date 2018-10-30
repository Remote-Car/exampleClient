package com.iamds.rcms.client.examples;

import com.iamds.rcms.client.baseClient.ApiEndpoint;
import com.iamds.rcms.client.baseClient.AuthService;
import com.iamds.rcms.client.baseClient.AuthToken;
import com.iamds.rcms.client.baseClient.RcmsClientException;

import java.io.IOException;

public class Example1 {

    /*
    *** EXAMPLE 1 ***
    - This example shows authentication some functionality
    - For examle: Login, IsTokenStillValid, RemainingTokenLifetime and RefreshAuthToken.
     */
    public static void example1(ApiEndpoint apiEndpoint, String email, String password) {
        System.out.println("--- EXAMPLE 1 ---");
        try {

            System.out.print("Retrieving authToken...");
            AuthToken authToken = AuthService.getAuthToken(apiEndpoint, email, password);
            System.out.println("Done!");

            System.out.print("isTokenStillValid?...");
            boolean isValid = AuthService.isTokenStillValid(authToken);
            System.out.println(""+isValid);

            System.out.print("Remaining token lifetime...");
            System.out.println(authToken.getExpiresAt()-System.currentTimeMillis()+" ms");

            System.out.print("Wait 5 seconds...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Done!");

            System.out.print("Remaining token lifetime...");
            System.out.println(authToken.getExpiresAt()-System.currentTimeMillis()+" ms");

            System.out.print("Refresh authToken...");
            AuthService.refreshAuthToken(apiEndpoint, authToken);
            System.out.println("Done!");

            System.out.print("Remaining token lifetime...");
            System.out.println(authToken.getExpiresAt()-System.currentTimeMillis()+" ms");

        } catch (RcmsClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--- END OF EXAMPLE 1 ---");
    }

}
