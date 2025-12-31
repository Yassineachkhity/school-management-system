package org.openeye.authservice.dao.repositories;

import org.openeye.authservice.dao.entities.UserRole;
import org.openeye.authservice.dao.entities.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUserId(String userId);

    List<UserRole> findByRoleId(String roleId);

    boolean existsByUserIdAndRoleId(String userId, String roleId);

    void deleteByUserIdAndRoleId(String userId, String roleId);
}
