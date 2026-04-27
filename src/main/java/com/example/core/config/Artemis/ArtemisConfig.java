package com.example.core.config.Artemis;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import jakarta.jms.ConnectionFactory;

@Configuration
@EnableJms
public class ArtemisConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        // Для ActiveMQ Artemis
        return new ActiveMQConnectionFactory(
                "tcp://localhost:61616",
                "admin",
                "admin"
        );

        // ИЛИ для встроенного брокера (работает без внешнего сервера)
        // return new ActiveMQConnectionFactory("vm://0");
    }
}