package com.bookit.step_definitions;

import com.bookit.pages.SelfPage;
import com.bookit.utilities.BookitApiUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.Assert.*;

public class userStepDefs {

    Logger logger = Logger.getLogger(String.valueOf(userStepDefs.class));

    String token;
    Response response;
    String globalEmail;

    @Given("I logged Bookit api using {string} and {string}")
    public void i_logged_Bookit_api_using_and(String email, String password) {

        logger.info("Get Token");
       token = BookitApiUtils.generateToken(email,password);
       globalEmail = email;//we want to use the email which comes from parameter in other implements in this class.

    }

    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {

        logger.info("Get info");
        response = given().accept(ContentType.JSON).and().header("Authorization", token)//request part header method not response part

                .when().get( ConfigurationReader.get("qa2_api_uri") + "/api/users/me" );

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {

        assertEquals(expectedStatusCode, response.statusCode());
    }


    @Then("the information about current user from api and database should match")
    public void the_information_about_current_user_from_api_and_database_should_match() {

        //DATABASE-API

        //get info from database
        Map<String, Object> dbInfo = DBUtils.getRowMap("select id,firstname,lastname,role from users\n" +
                "where email='"+globalEmail+"'");
        long expectedID = (Long) dbInfo.get("id");
        String expectedFirstName = (String) dbInfo.get("firstname");
        String expectedLastName = (String) dbInfo.get("lastname");
        String expectedRole = (String) dbInfo.get("role");

        //get info from api
        JsonPath jsonPath = response.jsonPath();
        long actualId = jsonPath.getLong("id");
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");


        //compare db-api
        assertEquals(expectedID,actualId);
        assertEquals(expectedFirstName,actualFirstName);
        assertEquals(expectedLastName,actualLastName);
        assertEquals(expectedRole,actualRole);
    }

    @Then("UI,API and DATABASE user information must match")
    public void ui_API_and_DATABASE_user_information_must_match() {

        //DATABASE-API

        //get info from database
        Map<String, Object> dbInfo = DBUtils.getRowMap("select id,firstname,lastname,role from users\n" +
                "where email='"+globalEmail+"'");
        long expectedID = (Long) dbInfo.get("id");
        String expectedFirstName = (String) dbInfo.get("firstname");
        String expectedLastName = (String) dbInfo.get("lastname");
        String expectedRole = (String) dbInfo.get("role");

        //get info from api
        JsonPath jsonPath = response.jsonPath();
        long actualId = jsonPath.getLong("id");
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");


        //compare db-api
        assertEquals(expectedID,actualId);
        assertEquals(expectedFirstName,actualFirstName);
        assertEquals(expectedLastName,actualLastName);
        assertEquals(expectedRole,actualRole);

        //get info from UI
        SelfPage selfPage = new SelfPage();
        String actualUIfullName = selfPage.name.getText();
        String actualUIrole = selfPage.role.getText();

        //DB vs UI
        String expectedFullName = expectedFirstName+" "+expectedLastName;
        assertEquals(expectedFullName,actualUIfullName);
        assertEquals(expectedRole,actualUIrole);

        //API vs UI
        String expectedAPIFullName = actualFirstName+" "+actualLastName;//it was actual against db that's why names are actual
        assertEquals(expectedAPIFullName,actualUIfullName);
        assertEquals(actualRole,actualUIrole);//expected part is api role it was actual against db that's why name is actual



    }

}
