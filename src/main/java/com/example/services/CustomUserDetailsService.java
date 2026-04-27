package com.example.services;

import com.example.entity.Role;
import com.example.entity.SecurityUser;
import com.example.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final JdbcTemplate jdbcTemplate;

    public CustomUserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            String userSql = "SELECT u.id, u.username, u.password, u.enabled FROM users u WHERE u.username = ?";

            User user;
            try {
                user = jdbcTemplate.queryForObject(userSql, new Object[]{username}, userRowMapper());
            } catch (Exception e) {
                throw new UsernameNotFoundException("User not found: " + username, e);
            }

            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            String rolesSql = "SELECT r.id, r.role_name FROM roles r " +
                    "JOIN user_roles ur ON r.id = ur.role_id " +
                    "WHERE ur.user_id = ?";

            List<Role> roles = jdbcTemplate.query(rolesSql, new Object[]{user.getId()}, roleRowMapper());
            user.setRoles(roles);

            return new SecurityUser(user);
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error loading user: " + username, e);
        }
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            return user;
        };
    }

    private RowMapper<Role> roleRowMapper() {
        return (rs, rowNum) -> {
            Role role = new Role();
            role.setId(rs.getLong("id"));
            role.setName(rs.getString("role_name"));
            return role;
        };
    }
}