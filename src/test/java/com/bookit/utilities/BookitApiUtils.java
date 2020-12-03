package com.bookit.utilities;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookitApiUtils {

    public static String generateToken(String email,String password){

        Response response = given().queryParam("email", email).and().queryParam("password", password)
                .when().get(ConfigurationReader.get("qa2_api_uri")+"/sign");

        String accessToken = response.path("accessToken");

        String finalToken = "Bearer " + accessToken;

        return finalToken;
    }
}
