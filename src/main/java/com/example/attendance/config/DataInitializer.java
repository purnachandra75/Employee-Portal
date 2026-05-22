package com.example.attendance.config;

import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import com.example.attendance.repository.RoleRepository;
import com.example.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize Roles
        if (roleRepository.findByName(Role.RoleType.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(Role.RoleType.ROLE_ADMIN));
        }

        if (roleRepository.findByName(Role.RoleType.ROLE_EMPLOYEE).isEmpty()) {
            roleRepository.save(new Role(Role.RoleType.ROLE_EMPLOYEE));
        }

        // Create default admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                    "admin",
                    "admin123",
                    "admin@company.com",
                    "Admin User"
            );

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName(Role.RoleType.ROLE_ADMIN)
                    .orElse(new Role(Role.RoleType.ROLE_ADMIN)));
            admin.setRoles(adminRoles);
            userRepository.save(admin);
        }

        // Create default employee users
        if (!userRepository.existsByUsername("emp1")) {
            User emp1 = new User(
                    "emp1",
                    "emp123",
                    "emp1@company.com",
                    "John Doe"
            );

            Set<Role> empRoles = new HashSet<>();
            empRoles.add(roleRepository.findByName(Role.RoleType.ROLE_EMPLOYEE)
                    .orElse(new Role(Role.RoleType.ROLE_EMPLOYEE)));
            emp1.setRoles(empRoles);
            userRepository.save(emp1);
        }

        if (!userRepository.existsByUsername("emp2")) {
            User emp2 = new User(
                    "emp2",
                    "emp123",
                    "emp2@company.com",
                    "Jane Smith"
            );

            Set<Role> empRoles = new HashSet<>();
            empRoles.add(roleRepository.findByName(Role.RoleType.ROLE_EMPLOYEE)
                    .orElse(new Role(Role.RoleType.ROLE_EMPLOYEE)));
            emp2.setRoles(empRoles);
            userRepository.save(emp2);
        }
    }
}
