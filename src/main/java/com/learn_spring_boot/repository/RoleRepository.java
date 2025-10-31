package com.learn_spring_boot.repository;

import com.learn_spring_boot.entity.Role;
import com.learn_spring_boot.enums.ERole;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends SoftDeleteRepository<Role, Integer> {
    Role findByName(ERole name);
}
