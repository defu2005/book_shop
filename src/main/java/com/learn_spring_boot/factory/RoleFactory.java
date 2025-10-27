package com.learn_spring_boot.factory;

import com.learn_spring_boot.entity.Role;
import com.learn_spring_boot.enums.ERole;
import com.learn_spring_boot.exception.RoleNotFoundException;
import com.learn_spring_boot.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {
    public final RoleRepository roleRepository;
    public RoleFactory(RoleRepository roleRepository){
        this.roleRepository=roleRepository;
    }
    public Role getInstance(String role) throws RoleNotFoundException {
        try {
            ERole eRole = ERole.valueOf(role.toUpperCase());
            return roleRepository.findByName(eRole);
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundException("Invalid role: " + role);
        }
    }

}

