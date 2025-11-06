package com.kenny.automation.Listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.kenny.automation.Helper.JsonHelper;

import io.qameta.allure.Allure;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class ApiLogHelper {

    private final ExtentTest test;

    public ApiLogHelper(ExtentTest test) {
        this.test = test;
    }

    public void logRequest(String method, String endpoint, Headers headers, String requestBody) {
        // Extent
        ExtentTest requestNode = test.createNode(
                MarkupHelper.createLabel("Request Info", ExtentColor.BLACK).getMarkup()
        );

        if (headers != null && !headers.asList().isEmpty()) {
            requestNode.info("Headers: " + headers.toString());
        }

        if (requestBody != null && !requestBody.isEmpty()) {
            requestNode.info("Method: " + method);
            requestNode.info("Endpoint: " + endpoint);
            String pretty = JsonHelper.prettyPrint(requestBody);
            requestNode.info(MarkupHelper.createLabel("Request Body:", ExtentColor.BLUE));
            requestNode.info(MarkupHelper.createCodeBlock(pretty));
        }

        // Allure
        Allure.step("API Request: " + method + " " + endpoint, () -> {
            if (headers != null && !headers.asList().isEmpty()) {
                Allure.addAttachment("Headers", headers.toString());
            }
            if (requestBody != null && !requestBody.isEmpty()) {
                Allure.addAttachment("Request Body", requestBody);
            }
        });
    }

    public void logResponse(Response response) {
        // Extent
        ExtentTest responseNode = test.createNode(
                MarkupHelper.createLabel("Response Info", ExtentColor.BLACK).getMarkup()
        );

        responseNode.info("Status Code: " + response.getStatusCode());

        Headers headers = response.getHeaders();
        if (headers != null && !headers.asList().isEmpty()) {
            responseNode.info("Headers: " + headers.toString());
        }

        String body = response.getBody().asString();
        if (body != null && !body.isEmpty()) {
            String pretty = JsonHelper.prettyPrint(body);
            responseNode.info(MarkupHelper.createLabel("Response Body:", ExtentColor.GREEN));
            responseNode.info(MarkupHelper.createCodeBlock(pretty));
        }

        // Allure
        Allure.step("API Response", () -> {
            Allure.addAttachment("Status Code", String.valueOf(response.getStatusCode()));
            if (headers != null && !headers.asList().isEmpty()) {
                Allure.addAttachment("Headers", headers.toString());
            }
            if (body != null && !body.isEmpty()) {
                Allure.addAttachment("Response Body", new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
            }
        });
    }
}
