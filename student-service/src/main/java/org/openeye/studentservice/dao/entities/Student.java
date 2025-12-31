package org.openeye.studentservice.dao.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Table(name = "students")
public class Student {

    @Id
    @Column(nullable = false, length = 36)
    private String studentId;

    @Column(nullable = false, unique = true, length = 36)
    private String userId;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 20)
    private String apogeCode;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(nullable = false)
    private LocalDate admissionDate;

    @Column(nullable = false)
    private Integer gradeLevel;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 36)
    private String departementId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
