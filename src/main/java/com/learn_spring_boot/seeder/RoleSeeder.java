package com.learn_spring_boot.seeder;

import com.learn_spring_boot.entity.Role;
import com.learn_spring_boot.enums.ERole;
import com.learn_spring_boot.repository.RoleRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleSeeder {
    private final RoleRepository roleRepository;
    public RoleSeeder(RoleRepository roleRepository){
        this.roleRepository=roleRepository;
    }
    @EventListener
    @Transactional
    public void LoadRoles(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent()==null){
            List<ERole> roles = Arrays.stream(ERole.values()).toList();
            for(ERole erole: roles) {
                if (roleRepository.findByName(erole)==null) {
                    roleRepository.save(new Role(erole));
                }
            }
        }
    }
}

