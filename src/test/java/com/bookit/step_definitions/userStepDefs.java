package com.bookit.step_definitions;

import com.bookit.utilities.BookitApiUtils;
import com.bookit.utilities.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class userStepDefs {

    String token;
    Response response;

    @Given("I logged Bookit api using {string} and {string}")
    public void i_logged_Bookit_api_using_and(String email, String password) {

       token = BookitApiUtils.generateToken(email,password);

    }

    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {

        response = given().accept(ContentType.JSON).and().header("Authorization", token)//request part header method not response part

                .when().get( ConfigurationReader.get("qa2_api_uri") + "/api/users/me" );

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {

        Assert.assertEquals(expectedStatusCode, response.statusCode());
    }

}
