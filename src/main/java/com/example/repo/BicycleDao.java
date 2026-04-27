package com.example.repo;

import com.example.entity.Bicycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class BicycleDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BicycleDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Обновляем SQL для поддержки флага purchased
    public List<Bicycle> findAll() {
        return jdbcTemplate.query("SELECT * FROM bicycle WHERE purchased = false",
                new BeanPropertyRowMapper<>(Bicycle.class));
    }

    public List<Bicycle> findAllIncludingPurchased() {
        return jdbcTemplate.query("SELECT * FROM bicycle",
                new BeanPropertyRowMapper<>(Bicycle.class));
    }

    public int insert(Bicycle bicycle) {
        // Способ 1: Без KeyHolder (проще)
        String sql = "INSERT INTO bicycle (model, producer, producing_country, gears_num, cost, purchased) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try {
            Long generatedId = jdbcTemplate.queryForObject(sql, Long.class,
                    bicycle.getModel(),
                    bicycle.getProducer(),
                    bicycle.getProducingCountry(),
                    bicycle.getGearsNum(),
                    bicycle.getCost(),
                    false);

            if (generatedId != null) {
                bicycle.setId(generatedId);
                return 1;
            }
            return 0;
        } catch (Exception e) {
            System.err.println("Error in insert: " + e.getMessage());
            return insertWithoutReturning(bicycle);
        }
    }

    // Способ 2: Альтернативный без RETURNING
    private int insertWithoutReturning(Bicycle bicycle) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO bicycle (model, producer, producing_country, gears_num, cost, purchased) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, bicycle.getModel());
            ps.setString(2, bicycle.getProducer());
            ps.setString(3, bicycle.getProducingCountry());
            ps.setInt(4, bicycle.getGearsNum());
            ps.setInt(5, bicycle.getCost());
            ps.setBoolean(6, false);
            return ps;
        }, keyHolder);

        // Правильное получение ключа
        if (result > 0 && keyHolder.getKeys() != null && !keyHolder.getKeys().isEmpty()) {
            Number key = (Number) keyHolder.getKeys().get("id");
            if (key != null) {
                bicycle.setId(key.longValue());
            }
        }

        return result;
    }

    // Способ 3: Самый надежный для PostgreSQL
    public int insertSafe(Bicycle bicycle) {
        String sql = "INSERT INTO bicycle (model, producer, producing_country, gears_num, cost, purchased) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Сначала вставляем без ID
        int result = jdbcTemplate.update(sql,
                bicycle.getModel(),
                bicycle.getProducer(),
                bicycle.getProducingCountry(),
                bicycle.getGearsNum(),
                bicycle.getCost(),
                false);

        // Затем получаем последний ID отдельным запросом
        if (result > 0) {
            Long lastId = jdbcTemplate.queryForObject(
                    "SELECT lastval()", Long.class);
            if (lastId != null) {
                bicycle.setId(lastId);
            }
        }

        return result;
    }


    public int markAsPurchased(Long id) {
        return jdbcTemplate.update("UPDATE bicycle SET purchased = true WHERE id = ?", id);
    }

    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM bicycle WHERE id = ?", id);
    }

    public int update(Long id, Bicycle bicycle) {
        return jdbcTemplate.update(
                "UPDATE bicycle SET model = ?, producer = ?, producing_country = ?, gears_num = ?, cost = ? WHERE id = ?",
                bicycle.getModel(), bicycle.getProducer(), bicycle.getProducingCountry(),
                bicycle.getGearsNum(), bicycle.getCost(), id
        );
    }

    public Bicycle findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM bicycle WHERE id = ?",
                new BeanPropertyRowMapper<>(Bicycle.class), id
        );
    }

    public List<Bicycle> find_cost(Integer cost) {
        return jdbcTemplate.query(
                "SELECT * FROM bicycle WHERE cost > ? AND purchased = false",
                new BeanPropertyRowMapper<>(Bicycle.class), cost
        );
    }
}