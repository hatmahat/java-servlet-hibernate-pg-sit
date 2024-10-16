package com.example.sit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.java_spring_hibernate_pg_sit.JavaSpringHibernatePgSitApplication;

public class TestHelper {

    private Connection connection;
    private ConfigurableApplicationContext context;  // To store the running application context
    public CloseableHttpClient httpClient;
    public static final String BASE_URL = "http://localhost:8080";

    public void setUp() throws Exception {
        // Start the Spring Boot application 
        context = SpringApplication.run(JavaSpringHibernatePgSitApplication.class);

        // Set up the database connection
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5333/java_sit", "myuser", "mypassword");

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
