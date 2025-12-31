package org.openeye.authservice.dao.repositories;

import org.openeye.authservice.dao.entities.Role;
import org.openeye.authservice.enums.RoleLabel;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRoleId(String roleId);

    Role findByLabel(RoleLabel label);

    boolean existsByLabel(RoleLabel label);
}
