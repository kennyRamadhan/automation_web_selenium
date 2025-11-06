package com.kenny.automation.API;

import org.json.JSONObject;

import com.aventstack.extentreports.ExtentTest;

import io.restassured.response.Response;

/**
 * MasterAPI wrapper untuk mengakses endpoint /posts pada JSONPlaceholder
 */
public class MasterAPIJsonPlaceholder {

    private final BaseTestAPIJsonPlaceholder apiClient;

    public MasterAPIJsonPlaceholder(ExtentTest test) {
        this.apiClient = new BaseTestAPIJsonPlaceholder(test);
    }

    public Response getPost(int id) {
        return apiClient.get("/posts/" + id);
    }

    public Response getAllPosts() {
        return apiClient.get("/posts");
    }

    public Response createPost(String title, String body, int userId) {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("body", body);
        json.put("userId", userId);
        return apiClient.post("/posts", json.toString());
    }

    public Response updatePost(int id, String title, String body, int userId) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        json.put("body", body);
        json.put("userId", userId);
        return apiClient.put("/posts/" + id, json.toString());
    }

    public Response patchPost(int id, String title) {
        JSONObject json = new JSONObject();
        json.put("title", title);
        return apiClient.patch("/posts/" + id, json.toString());
    }

    public Response deletePost(int id) {
        return apiClient.delete("/posts/" + id);
    }
}
