package com.ananas.exceltxml.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        File envFile = new File(".env");
        if (!envFile.exists()) {
            log.warn(".env fajl nije pronađen u root direktorijumu");
            return;
        }

        Map<String, Object> properties = new HashMap<>();
        
        try (Scanner scanner = new Scanner(envFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                
                // Preskačemo prazne linije i komentare
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Parsiranje KEY=VALUE formata
                int equalsIndex = line.indexOf('=');
                if (equalsIndex > 0) {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    
                    // Uklanjamo navodnike ako postoje
                    if ((value.startsWith("\"") && value.endsWith("\"")) ||
                        (value.startsWith("'") && value.endsWith("'"))) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    // Dodajemo samo ako već ne postoji u environment-u
                    if (!environment.containsProperty(key)) {
                        properties.put(key, value);
                        log.debug("Učitana varijabla iz .env: {}", key);
                    }
                }
            }
            
            if (!properties.isEmpty()) {
                environment.getPropertySources().addFirst(
                    new MapPropertySource("dotenv", properties)
                );
                log.info("Uspešno učitano {} varijabli iz .env fajla", properties.size());
            }
        } catch (FileNotFoundException e) {
            log.error("Greška pri učitavanju .env fajla", e);
        }
    }
}

