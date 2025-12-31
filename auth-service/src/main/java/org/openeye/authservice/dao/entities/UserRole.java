package org.openeye.authservice.dao.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(
        name = "user_roles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "roleId"})
)
@IdClass(UserRoleId.class)
public class UserRole {

    @Id
    @Column(name = "userId", nullable = false, length = 36)
    private String userId;

    @Id
    @Column(name = "roleId", nullable = false, length = 36)
    private String roleId;
}
