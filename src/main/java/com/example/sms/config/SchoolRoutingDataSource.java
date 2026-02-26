package com.example.sms.config;

import com.example.sms.util.SchoolContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Routes database connections dynamically
 * based on the current schoolCode stored in ThreadLocal.
 *
 * This is the core of multi-tenant (school-wise DB) routing.
 */
public class SchoolRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        String schoolCode = SchoolContextHolder.getSchoolCode();

        // ❌ Hard fail if routing context is missing
        // This prevents accidental writes to wrong school DB
        if (schoolCode == null || schoolCode.isBlank()) {
            throw new IllegalStateException(
                    "School code not set in SchoolContextHolder. " +
                            "Request must include /api/schools/{schoolCode}/..."
            );
        }

        // ✅ Used as key to select DataSource
        return schoolCode;
    }
}