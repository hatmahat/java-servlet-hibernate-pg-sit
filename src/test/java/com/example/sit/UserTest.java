package com.example.sit;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.junit.jupiter.api.TestInstance;


import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest extends TestHelper {

    @BeforeAll
    public void setUpOnce() throws Exception {
        System.setProperty("external.api.url", "http://localhost:8089/todos/1");
        super.setUpOnce();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void listUsers_SuccessCase_ReturnUsersList() throws Exception { 
        HttpGet request = new HttpGet(BASE_URL + "/users");
        CloseableHttpResponse response = httpClient.execute(request);

        // Verify the response status
        assertEquals(200, response.getCode());

        // Parse and verify the response body (could use a JSON parser)
        String responseBody = new String(response.getEntity().getContent().readAllBytes());

        String expectedResponse = """
        [
            {"id":1,"name":"John Doe","email":"john@example.com","membershipStatus":"VIP"},
            {"id":2,"name":"Jane Smith","email":"jane@example.com","membershipStatus":"NONE"}
        ]
        """;

        JSONAssert.assertEquals(expectedResponse, responseBody, false); // false means strict mode off (ignores order and spaces)
    }

    @Test
    public void saveUser_SuccessCase_ReturnSuccessString() throws Exception {
        HttpPost request = new HttpPost(BASE_URL + "/addUser?name=Alice&email=alice@example.com&type=VIP");
        CloseableHttpResponse response = httpClient.execute(request);

        // Verify the response status
        assertEquals(200, response.getCode());

        // Verify the response body
        String responseBody = new String(response.getEntity().getContent().readAllBytes());
        assertEquals("User added successfully!", responseBody);
    }

    @Test
    public void calculateDiscount_SuccessCase_GetUserDiscount() throws Exception {
        HttpGet request = new HttpGet(BASE_URL + "/discount?id=1&purchaseAmount=150.0");
        CloseableHttpResponse response = httpClient.execute(request);

        // Verify the response status
        assertEquals(200, response.getCode());

        // Parse and verify the response body
        String responseBody = new String(response.getEntity().getContent().readAllBytes());
        assertEquals("User gets a discount of 20.0% on a purchase of $150.0", responseBody);
    }

    @Test
    public void calculateDiscount_FailedCase_UserNotFound() throws Exception {
        HttpGet request = new HttpGet(BASE_URL + "/discount?id=999&purchaseAmount=150.0");
        CloseableHttpResponse response = httpClient.execute(request);

        // Verify the response status (expecting 404 since user not found)
        assertEquals(404, response.getCode());

        // Verify the response body
        String responseBody = new String(response.getEntity().getContent().readAllBytes());
        assertEquals("User with ID 999 not found", responseBody);
    }

    @Test
    public void fetchTodo_SuccessCase_ReturnTodoObject() throws Exception {
        // Make the request to the /todo endpoint
        HttpGet request = new HttpGet(BASE_URL + "/todo");

        CloseableHttpResponse response = httpClient.execute(request);

        // Verify the response status
        assertEquals(200, response.getCode());

        // Parse the response body
        String responseBody = new String(response.getEntity().getContent().readAllBytes());

        // Expected response from WireMock stub
        String expectedResponse = """
        {
            "userId": 1,
            "id": 1,
            "title": "delectus aut autem",
            "completed": false
        }
        """;

        // Assert the response body using JSONAssert for deep equality comparison
        JSONAssert.assertEquals(expectedResponse, responseBody, false); // False means non-strict mode
    }
}

