package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import model.SelenoidStatusModel;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class SelenoidTests {

    @Test
    void successStatusTest() {
        given().when()
                .get("https://user1:1234@selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void successStatusWithBasicAuthTest() {
        given()
                .auth().basic("user1","1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void unSuccessStatusTest() {
        given().when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void successStatusResponseTest() {
        Response response = given()
                .auth().basic("user1","1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.toString()); // bad log - io.restassured.internal.RestAssuredResponseImpl@ed41518
        System.out.println(response.asString()); // {"value":{"message":"Selenoid 98f495722e60da4b35c14814bae240fe6ec75abc built at 2020-09-02_11:14:20AM","ready":true}}
    }

    @Test
    void successStatusResponseWithLogTest() {
        given()
                .auth().basic("user1","1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();

        /* here we get a better parsed log
        {
            "value": {
                "message": "Selenoid 98f495722e60da4b35c14814bae240fe6ec75abc built at 2020-09-02_11:14:20AM",
                "ready": true
            }
        }
         */
    }

    @Test
    void successStatusReadyTest() {
         Boolean result = given()
                .auth().basic("user1","1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .path("value.ready");

//        assertTrue(result);
        assertThat(result, is(true));
    }

    @Test
    void successStatusReadyInRequestTest() {
        given()
                .auth().basic("user1","1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    @Test
    void successStatusReadyAsModelTest() {
        SelenoidStatusModel selenoidStatusModel = given()
                .auth().basic("user1","1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(SelenoidStatusModel.class);

        String message = selenoidStatusModel.getValue().getMessage();
        assertThat(message, containsString("Selenoid"));
    }

    @Test
    void successStatusReadyWithSpecTest() {
        given()
                .auth().basic("user1","1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .log().body()
                .spec(responseSpec);
    }

    private final ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectBody("value.ready", is(true))
            .expectBody("value.message", is(notNullValue()))
            .expectStatusCode(200)
            .build();
}
