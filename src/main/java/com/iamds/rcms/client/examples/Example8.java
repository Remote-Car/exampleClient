package com.iamds.rcms.client.examples;

import com.iamds.rcms.client.baseClient.*;

import java.io.IOException;

public class Example8 {

    /*
    *** EXAMPLE 8 ***
    - This example shows how a communication via a windows web proxy works.
     */
    public static void example8(ApiEndpoint apiEndpoint, String email, String password, String proxyAddress, int proxyPort) {
        AuthServiceAbstract authService = new AuthServiceWinWebProxy(proxyAddress, proxyPort);

        System.out.println("--- EXAMPLE 8 ---");
        try {

            System.out.print("Retrieving authToken...");
            AuthToken authToken = authService.getAuthToken(apiEndpoint, email, password);
            System.out.println("Done!");

            System.out.print("isTokenStillValid?...");
            boolean isValid = AuthServiceAbstract.isTokenStillValid(authToken);
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
            authService.refreshAuthToken(apiEndpoint, authToken);
            System.out.println("Done!");

            System.out.print("Remaining token lifetime...");
            System.out.println(authToken.getExpiresAt()-System.currentTimeMillis()+" ms");

        } catch (RcmsClientException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("--- END OF EXAMPLE 8 ---");
    }

}
