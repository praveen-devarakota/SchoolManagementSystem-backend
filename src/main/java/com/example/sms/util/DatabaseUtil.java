package com.example.sms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseUtil {

    @Autowired
    private DataSource dataSource;

    /**
     * Create a new school database
     */
    public void createDatabase(String dbName) {
        String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to create database: " + dbName, e
            );
        }
    }

    /**
     * Run schema for newly created school database
     */
    public void runSchema(String dbName) {
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute("USE " + dbName);

            InputStream inputStream =
                    getClass().getClassLoader()
                            .getResourceAsStream("schema-school.sql");

            if (inputStream == null) {
                throw new RuntimeException("schema-school.sql not found in resources");
            }

            String schemaSql = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            for (String query : schemaSql.split(";")) {
                if (!query.trim().isEmpty()) {
                    stmt.execute(query);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to initialize schema for " + dbName, e
            );
        }
    }

    /**
     * Update school metadata inside school DB
     * (Used when school code or name changes)
     */
    public void updateSchoolMetadata(
            String dbName,
            String schoolCode,
            String schoolName) {

        String sql = "UPDATE school_profile " +
                "SET school_code = '" + schoolCode + "', " +
                "school_name = '" + schoolName + "'";

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute("USE " + dbName);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update school metadata for " + dbName, e
            );
        }
    }

    /**
     * Permanently delete school database
     * (Called only after school is INACTIVE)
     */
    public void dropDatabase(String dbName) {
        String sql = "DROP DATABASE IF EXISTS " + dbName;

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to drop database: " + dbName, e
            );
        }
    }
}