package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static helpers.ApiHelper.getUserToken;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.FileUtils.readStringFromFile;

public class RegressInTests {

    @BeforeEach
    void beforeEach() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    void successSingleUserTest() {
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .log().body()
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    void successLoginTest() {
        String data = "{" +
                "    \"email\": \"eve.holt@reqres.in\"," +
                "    \"password\": \"cityslicka\"" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .log().body()
                .body("token", is(notNullValue()));
    }

    @Test
    void successLoginWithLogTest() {
        String data = "{" +
                "    \"email\": \"eve.holt@reqres.in\"," +
                "    \"password\": \"cityslicka\"" +
                "}";

        Response response =
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/login")
                .then()
                .statusCode(200).extract().response();

        System.out.println(response.asString());
    }

    @Test
    void successLoginWithDataFromResourcesTest() {
        String data = readStringFromFile("src/test/resources/regress_in_login_data.json");

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .log().body()
                .body("token", is(notNullValue()));
    }

    @Test
    void successTokenTest() {
        String token = getUserToken();

        System.out.println(token);
    }

}
