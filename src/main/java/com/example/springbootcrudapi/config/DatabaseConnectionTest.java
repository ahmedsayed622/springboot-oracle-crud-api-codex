package com.example.springbootcrudapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        testDatabaseConnection();
    }

    private void testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ Oracle Database Connection Test Successful!");
            System.out.println("📊 Database URL: " + connection.getMetaData().getURL());
            System.out.println("👤 Database User: " + connection.getMetaData().getUserName());
            System.out.println("🔧 Database Product: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("📋 Database Version: " + connection.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            System.err.println("❌ Oracle Database Connection Test Failed!");
            System.err.println("💡 Error: " + e.getMessage());
            System.err.println("🔍 Please check:");
            System.err.println("   - Oracle database is running on localhost:1521");
            System.err.println("   - Service name 'orcl12c' is correct");
            System.err.println("   - Username 'HR' and password '123' are correct");
            System.err.println("   - Oracle JDBC driver is properly loaded");
        }
    }
}
