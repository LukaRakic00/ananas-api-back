package com.ananas.exceltxml.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DatabaseConfig {

    @Value("${SUPABASE_DB_URL:}")
    private String dbUrl;

    @Value("${SUPABASE_DB_HOST:}")
    private String dbHost;

    @Value("${SUPABASE_DB_PORT:5432}")
    private String dbPort;

    @Value("${SUPABASE_DB_NAME:postgres}")
    private String dbName;

    @Value("${SUPABASE_DB_USERNAME:postgres}")
    private String dbUsername;

    @Value("${SUPABASE_DB_PASSWORD:}")
    private String dbPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        String url;
        
        // Ako je kompletan URL definisan, koristimo ga
        if (dbUrl != null && !dbUrl.isEmpty()) {
            // Proveravamo da li već ima jdbc: prefix
            if (dbUrl.startsWith("jdbc:postgresql://")) {
                url = dbUrl;
                log.info("Koristi se kompletan URL iz .env fajla (sa jdbc: prefixom)");
            } else {
                // Dodajemo jdbc: prefix ako ga nema
                url = "jdbc:postgresql://" + dbUrl;
                log.info("Dodat jdbc: prefix URL-u iz .env fajla");
            }
        } else {
            // Inače sastavljamo URL od pojedinačnih delova
            url = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
            log.info("URL sastavljen od pojedinačnih delova iz .env fajla: host={}, port={}, database={}", dbHost, dbPort, dbName);
        }

        log.info("Konekcija na bazu: {}", url.replaceAll(":[^:@]+@", ":****@"));
        log.info("Username: {}", dbUsername);

        // Konfiguracija za PostgreSQL pooler - onemogućava prepared statement caching
        // što rešava problem sa "prepared statement already exists"
        String jdbcUrl = url;
        if (!jdbcUrl.contains("?")) {
            jdbcUrl += "?";
        } else {
            jdbcUrl += "&";
        }
        jdbcUrl += "prepareThreshold=0"; // Onemogućava prepared statements

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(dbUsername)
                .password(dbPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

