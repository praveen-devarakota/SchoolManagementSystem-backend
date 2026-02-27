package com.example.sms.master.entity;

import com.example.sms.enums.SchoolStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "schools",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "school_code")
        }
)
@Getter
@Setter
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_code", nullable = false, unique = true)
    private String schoolCode;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "db_name", nullable = false, updatable = false)
    private String dbName;

    @Column(name = "db_username", nullable = false)
    private String dbUsername;

    @Column(name = "db_password", nullable = false)
    private String dbPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchoolStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}