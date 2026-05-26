package com.example.attendance.service;

import com.example.attendance.exception.ForbiddenException;
import com.example.attendance.exception.ResourceNotFoundException;
import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public boolean hasRole(User user, Role.RoleType roleType) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName() == roleType);
    }

    public void requireRole(String username, Role.RoleType roleType) {
        User user = getUserByUsername(username);
        if (!hasRole(user, roleType)) {
            throw new ForbiddenException("Access denied: requires " + roleType.name());
        }
    }

    public void requireAdmin(String username) {
        requireRole(username, Role.RoleType.ROLE_ADMIN);
    }

    public void requireEmployee(String username) {
        requireRole(username, Role.RoleType.ROLE_EMPLOYEE);
    }
}
