package org.openeye.inscriptionservice.dao.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "inscriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"studentId", "sectionId"})
)
public class Inscription {

    @Id
    @Column(nullable = false, length = 36)
    private String enrollmentId;

    @Column(nullable = false, length = 36)
    private String studentId;

    @Column(nullable = false, length = 36)
    private String sectionId;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    private Double grade;

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
