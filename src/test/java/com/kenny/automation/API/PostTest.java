package com.kenny.automation.API;

import org.testng.annotations.Test;
import org.testng.Assert;
import com.aventstack.extentreports.ExtentTest;
import com.kenny.automation.Listeners.ExtentNode;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;

/**
 * Test class untuk endpoint /posts JSONPlaceholder
 */
public class PostTest {

	@Epic("API Tests")
	@Feature("GET SINGLE")
    @Test
    public void testGetSinglePost() {
        ExtentTest test = ExtentNode.getNode();
        MasterAPIJsonPlaceholder api = new MasterAPIJsonPlaceholder(test);

        Response response = api.getPost(1);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getInt("id") == 1);
    }

	@Epic("API Tests")
	@Feature("GET ALL")
    @Test
    public void testGetAllPosts() {
        ExtentTest test = ExtentNode.getNode();
        MasterAPIJsonPlaceholder api = new MasterAPIJsonPlaceholder(test);

        Response response = api.getAllPosts();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getList("$").size() >= 100);
    }

	@Epic("API Tests")
	@Feature("CREATE")
    @Test
    public void testCreatePost() {
        ExtentTest test = ExtentNode.getNode();
        MasterAPIJsonPlaceholder api = new MasterAPIJsonPlaceholder(test);

        Response response = api.createPost("foo", "bar", 1);
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.jsonPath().getString("title"), "foo");
    }

	
	@Epic("API Tests")
	@Feature("DELETE")
    @Test
    public void testUpdatePost() {
        ExtentTest test = ExtentNode.getNode();
        MasterAPIJsonPlaceholder api = new MasterAPIJsonPlaceholder(test);

        Response response = api.updatePost(1, "foo", "bar", 1);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("title"), "foo");
    }

	
	@Epic("API Tests")
	@Feature("PATCH")
    @Test
    public void testPatchPost() {
        ExtentTest test = ExtentNode.getNode();
        MasterAPIJsonPlaceholder api = new MasterAPIJsonPlaceholder(test);

        Response response = api.patchPost(1, "patched title");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("title"), "patched title");
    }

	
	@Epic("API Tests")
	@Feature("DELETE")
    @Test
    public void testDeletePost() {
        ExtentTest test = ExtentNode.getNode();
        MasterAPIJsonPlaceholder api = new MasterAPIJsonPlaceholder(test);

        Response response = api.deletePost(1);
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
