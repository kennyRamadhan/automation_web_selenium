package com.kenny.automation.API;

import static io.restassured.RestAssured.given;

import com.aventstack.extentreports.ExtentTest;
import com.kenny.automation.Listeners.ApiLogHelper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * BaseTestAPI untuk JSONPlaceholder tanpa autentikasi.
 */
public class BaseTestAPIJsonPlaceholder {

    private final RequestSpecification spec;
    private final ApiLogHelper apiLogger;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public BaseTestAPIJsonPlaceholder(ExtentTest test) {
        this.apiLogger = new ApiLogHelper(test);

        this.spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    public Response get(String endpoint) {
        apiLogger.logRequest("GET", endpoint, null, null);

        Response response = given()
                .spec(spec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        apiLogger.logResponse(response);
        return response;
    }

    public Response post(String endpoint, String body) {
        apiLogger.logRequest("POST", endpoint, null, body);

        Response response = given()
                .spec(spec)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        apiLogger.logResponse(response);
        return response;
    }

    public Response put(String endpoint, String body) {
        apiLogger.logRequest("PUT", endpoint, null, body);

        Response response = given()
                .spec(spec)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();

        apiLogger.logResponse(response);
        return response;
    }

    public Response patch(String endpoint, String body) {
        apiLogger.logRequest("PATCH", endpoint, null, body);

        Response response = given()
                .spec(spec)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();

        apiLogger.logResponse(response);
        return response;
    }

    public Response delete(String endpoint) {
        apiLogger.logRequest("DELETE", endpoint, null, null);

        Response response = given()
                .spec(spec)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();

        apiLogger.logResponse(response);
        return response;
    }
}
