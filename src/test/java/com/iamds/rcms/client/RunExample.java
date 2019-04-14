package com.iamds.rcms.client;

import com.iamds.rcms.client.baseClient.ApiEndpoint;
import com.iamds.rcms.client.baseClient.EnergyTypes;
import com.iamds.rcms.client.baseClient.OemTypes;
import com.iamds.rcms.client.examples.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.iamds.rcms.client.Credentials.*;


public class RunExample
{
    // Put the example you want to run in the array below
    private final List<String> runList = Arrays.asList(
            "example1",
            "example2"
    );

    @Test
    public void shouldAnswerWithTrue()
    {
        ApiEndpoint apiEndpoint = new ApiEndpoint("https://api.remote-car.com/api/", CLIENT_KEY);

        if(runList.contains("example1"))
            Example1.example1(apiEndpoint, EMAIL, PASSWORD);


        if(runList.contains("example2"))
            Example2.example2(apiEndpoint, EMAIL, PASSWORD);


        if(runList.contains("example3"))
            Example3.example3(apiEndpoint, EMAIL, PASSWORD);


        if(runList.contains("example4"))
            Example4.example4(apiEndpoint, EMAIL, PASSWORD);


        if(runList.contains("example5"))
            Example5.example5(apiEndpoint, EMAIL, PASSWORD);


        if(runList.contains("example6"))
            Example6.example6(apiEndpoint, EMAIL, PASSWORD);


        if(runList.contains("example7")) {
            String oemLoginEmail = "emailOfYourTeslaLogin@example.com";
            String oemLoginPassword = "passwordOfYourTeslaAccount";
            OemTypes oemType = OemTypes.TESLA;
            EnergyTypes energyType = EnergyTypes.BATTERY;
            String vin = "WJD2Z3JK43722H"; // Just an example
            Example7.example7(apiEndpoint, EMAIL, PASSWORD, oemLoginEmail, oemLoginPassword, oemType, vin, energyType);
        }

        if(runList.contains("example8")) {
            Example8.example8(apiEndpoint, EMAIL, PASSWORD, PROXY_ADDRESS, PROXY_PORT);
        }
    }
}
