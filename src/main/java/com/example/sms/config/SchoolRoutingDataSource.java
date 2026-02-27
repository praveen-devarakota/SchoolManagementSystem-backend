package com.example.sms.config;

import com.example.sms.util.SchoolContextHolder;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SchoolRoutingDataSource extends AbstractRoutingDataSource {

    private final Map<String, DataSource> cache = new ConcurrentHashMap<>();
    private final DataSource masterDataSource;

    public SchoolRoutingDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;

        // 🔴 REQUIRED FOR HIBERNATE BOOTSTRAP
        setDefaultTargetDataSource(masterDataSource);
        setTargetDataSources(Map.of("BOOT", masterDataSource));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return SchoolContextHolder.getSchoolCode();
    }

    @Override
    protected DataSource determineTargetDataSource() {

        String schoolCode = SchoolContextHolder.getSchoolCode();

        // ✅ IMPORTANT: During startup (no request yet)
        if (schoolCode == null) {
            return masterDataSource;
        }

        return cache.computeIfAbsent(
                schoolCode,
                this::createSchoolDataSource
        );
    }

    private DataSource createSchoolDataSource(String schoolCode) {
        try (Connection con = masterDataSource.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(
                    "SELECT db_name FROM schools " +
                            "WHERE school_code = '" + schoolCode + "'"
            );

            if (!rs.next()) {
                throw new RuntimeException(
                        "No DB configured for school: " + schoolCode);
            }

            String dbName = rs.getString("db_name");

            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/" + dbName);
            ds.setUsername("root");
            ds.setPassword("minnu@443");
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

            System.out.println("✅ Routing school " + schoolCode + " → " + dbName);

            return ds;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create datasource for " + schoolCode, e);
        }
    }
}