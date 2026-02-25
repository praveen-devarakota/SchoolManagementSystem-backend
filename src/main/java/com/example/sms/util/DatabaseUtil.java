package com.example.sms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseUtil {

    @Autowired
    private DataSource dataSource;

    public void createDatabase(String dbName) {
        String sql = "CREATE DATABASE " + dbName;

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database: " + dbName, e);
        }
    }

    public void runSchema(String dbName) {
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute("USE " + dbName);

            InputStream inputStream =
                    getClass().getClassLoader().getResourceAsStream("schema-school.sql");

            String schemaSql = new String(inputStream.readAllBytes());

            for (String query : schemaSql.split(";")) {
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize schema for " + dbName, e);
        }
    }
}
