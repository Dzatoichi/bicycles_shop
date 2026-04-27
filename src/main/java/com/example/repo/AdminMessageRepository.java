package com.example.repo;

import com.example.entity.Bicycle;
import com.example.jms.AdminMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AdminMessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdminMessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AdminMessage> rowMapper = (rs, rowNum) -> {
        AdminMessage message = new AdminMessage();
        message.setId(rs.getLong("id"));
        message.setOperation(rs.getString("operation"));
        message.setUsername(rs.getString("username"));
        message.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        Long bicycleId = rs.getLong("bicycle_id");
        if (bicycleId != null && bicycleId > 0) {
            Bicycle bicycle = new Bicycle();
            bicycle.setId(bicycleId);
            message.setBicycle(bicycle);
        }
        return message;
    };

    public void save(AdminMessage message) {
        String sql = "INSERT INTO admin_messages (operation, bicycle_id, username, timestamp) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                message.getOperation(),
                message.getBicycle() != null ? message.getBicycle().getId() : null,
                message.getUsername(),
                message.getTimestamp());
    }

    public List<AdminMessage> findAllOrderByTimestampDesc() {
        String sql = "SELECT * FROM admin_messages ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<AdminMessage> findByOperationOrderByTimestampDesc(String operation) {
        String sql = "SELECT * FROM admin_messages WHERE operation = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, rowMapper, operation);
    }

    public List<AdminMessage> findByUsernameOrderByTimestampDesc(String username) {
        String sql = "SELECT * FROM admin_messages WHERE username = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, rowMapper, username);
    }

    public AdminMessage findById(Long id) {
        String sql = "SELECT * FROM admin_messages WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM admin_messages WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}