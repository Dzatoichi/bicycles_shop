package com.example.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(String username, String password, String roleName) {
        // Проверяем существование пользователя
        if (userExists(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        // Создаем пользователя
        jdbcTemplate.update(
                "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)",
                username, passwordEncoder.encode(password), true);

        // Получаем ID пользователя
        Long userId = jdbcTemplate.queryForObject(
                "SELECT id FROM users WHERE username = ?", Long.class, username);

        // Получаем ID роли
        Long roleId = jdbcTemplate.queryForObject(
                "SELECT id FROM roles WHERE role_name = ?", Long.class, roleName);

        // Связываем пользователя с ролью
        jdbcTemplate.update(
                "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)",
                userId, roleId);
    }

    public boolean userExists(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?", Integer.class, username);
        return count != null && count > 0;
    }
}