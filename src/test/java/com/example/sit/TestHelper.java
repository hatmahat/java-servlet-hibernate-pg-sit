package com.example.sit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.java_spring_hibernate_pg_sit.JavaSpringHibernatePgSitApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class TestHelper {

    private Connection connection;
    private ConfigurableApplicationContext context;  // To store the running application context
    public CloseableHttpClient httpClient;
    public static final String BASE_URL = "http://localhost:8080";
    private WireMockServer wireMockServer;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public void setUpOnce() throws Exception {
        // Start WireMock server on port 8089
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8089));
        wireMockServer.start();
   
        // Set up WireMock stubs for external API
        setUpWireMockStubs();
    }

    public void setUp() throws Exception {
        // Start the Spring Boot application 
        context = SpringApplication.run(JavaSpringHibernatePgSitApplication.class);

        // Set up the database connection
        connection = DriverManager.getConnection( // make sure the database is for SIT only
                url, username, password);

        // Set up HttpClient
        httpClient = HttpClients.createDefault();

        // Seed the database with some initial data
        seedDatabase();
    }

    public void tearDown() throws Exception {
        // Delete all data
        deleteAllData();

        // Shut down the Spring Boot application after the test
        if (context != null) {
            context.close();
        }

        if (connection != null) {
            connection.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    private void setUpWireMockStubs() {
        WireMock.configureFor("localhost", 8089);
        WireMock.stubFor(WireMock.get("/todos/1")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withBody("{ \"userId\": 1, \"id\": 1, \"title\": \"delectus aut autem\", \"completed\": false }")
                        .withHeader("Content-Type", "application/json")));
    }

    private void seedDatabase() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO users (name, email, membership_status) VALUES ('John Doe', 'john@example.com', 'VIP');");
        stmt.executeUpdate("INSERT INTO users (name, email, membership_status) VALUES ('Jane Smith', 'jane@example.com', 'NONE');");
        stmt.close();
    }

    private void deleteAllData() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM users;");
        stmt.executeUpdate("ALTER SEQUENCE users_id_seq RESTART WITH 1;");
        stmt.close();
    }
    
}
