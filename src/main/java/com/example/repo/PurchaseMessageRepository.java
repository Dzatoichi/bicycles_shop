package com.example.repo;

import com.example.entity.Bicycle;
import com.example.jms.PurchaseMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PurchaseMessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public PurchaseMessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PurchaseMessage> rowMapper = (rs, rowNum) -> {
        PurchaseMessage message = new PurchaseMessage();
        message.setId(rs.getLong("id"));
        message.setUsername(rs.getString("username"));
        message.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        // Загружаем полный объект Bicycle
        Long bicycleId = rs.getLong("bicycle_id");
        if (bicycleId != null && bicycleId > 0) {
            Bicycle bicycle = new Bicycle();
            bicycle.setId(bicycleId);
            message.setBicycle(bicycle);
        }
        return message;
    };

    public void save(PurchaseMessage message) {
        String sql = "INSERT INTO purchase_messages (bicycle_id, username, timestamp) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                message.getBicycle() != null ? message.getBicycle().getId() : null,
                message.getUsername(),
                message.getTimestamp());
    }

    public List<PurchaseMessage> findAllOrderByTimestampDesc() {
        String sql = "SELECT * FROM purchase_messages ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<PurchaseMessage> findByUsernameOrderByTimestampDesc(String username) {
        String sql = "SELECT * FROM purchase_messages WHERE username = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, rowMapper, username);
    }

    public PurchaseMessage findById(Long id) {
        String sql = "SELECT * FROM purchase_messages WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM purchase_messages WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<PurchaseMessage> findByBicycleId(Long bicycleId) {
        String sql = "SELECT * FROM purchase_messages WHERE bicycle_id = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, rowMapper, bicycleId);
    }
}