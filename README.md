![](https://remote-car.com/wp-content/uploads/2018/07/remotecar_svg-1.png)

# Basic example API Client

This is a very basic(!) API Client for our **Remote-Car** API. The code includes several example API-Request scenarios. If you want to learn more about **Remote-Car**, you can have a look at Remote-Car.com.


# Requirements

To use this client you need to have a **tenant** account in our system. Each **tenant** has his own **client key** which is required to authenticate with our API.
Check out our **contact** section on Remote-Car.com, if you are interested in our service.


# Api Swagger documentation

We have a swagger API-Documentation hosted on: https://api.remote-car.com/doc/
This documentation includes all the available endpoints for our service. In this swagger documentation you will also find back-links to **examples** in this **Basic example API Client**.


# Getting started

To run this example client you will have to do the following:

 - Edit the `clientKey` variable in `/src/test/java/com/iamds/rcms/client/RunExample.java`
 - Add a User-Account (You can create several users, which belong to your tenant account)
 - Edit the `email` variable in `/src/test/java/com/iamds/rcms/client/RunExample.java` with the User email
 - Edit the `password` variable in `/src/test/java/com/iamds/rcms/client/RunExample.java` with the User password
 - Edit the `runList` array in `/src/test/java/com/iamds/rcms/client/RunExample.java` to define which examples should be executed


# List of examples

The following is a list of examples which are included in this client.

## Example 1

    /src/main/java/com/iamds/rcms/client/examples/Example1.java

This example shows some basic authentication routines which are required when talking with our API.

## Example 2

    /src/main/java/com/iamds/rcms/client/examples/Example2.java

This example retrieves a list of all user vehicles and then requests(in a second step) the first vehicle in the vehicle list including it's vehicleState. As an example the 'state of charge' value in % will show up in the console.
> **Note:** Have a closer look at the JSON response, there are much more values than the 'state of charge' value!

## Example 3

    /src/main/java/com/iamds/rcms/client/examples/Example3.java

The result/output of this example is a list of all vehicles/VIN numbers including the current 'State of Charge' value in %. Basically this example shows how to retrieve all vehicles including their current state at once.
> **Note:** Have a closer look at the JSON response, there are much more values than the 'state of charge' value!

## Example 4

    /src/main/java/com/iamds/rcms/client/examples/Example4.java

This example shows how to retrieve historic data of an vehicle. Specifically in this example all the 'state of charge' values in % are output to the console for a given week.

> **Note:** Have a closer look at the JSON response, there are much more values than the 'state of charge' value!

> **Note:** The api can only return data if the vehicle was already in our system for the given time period (timeFrom and timeTo).

## Example 5

    /src/main/java/com/iamds/rcms/client/examples/Example5.java

This example outputs all charging events for an electric vehicle. The output of a single charging event in this example include time, startSoC, endSoC.

> **Note:** Have a closer look at the JSON response, there are much more values than time, startSoC or endSoC!

## Example 6

    /src/main/java/com/iamds/rcms/client/examples/Example6.java

This example outputs all driving events for a vehicle. The output of a single driving event in this example include time, distance mileage!

> **Note:** Have a closer look at the JSON response, there are much more values than time, distance mileage!

## Example 7

    /src/main/java/com/iamds/rcms/client/examples/Example7.java

This example shows how to add and authenticate a new vehicle in our system.

> **Note:** Please have a closer look at this example if you want to manually(not using our web-portal) add vehicles to our system. You also have to edit additional parameters in `/src/test/java/com/iamds/rcms/client/RunExample.java` to run this example.



![](https://remote-car.com/wp-content/uploads/2018/07/remotecar_svg-1.png)