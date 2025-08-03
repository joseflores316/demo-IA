package com.jose.demoia.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.Arrays;

/**
 * Configuración de caché optimizada para Railway con memoria limitada
 *
 * Características:
 * - Límite máximo de entradas por caché
 * - Expiración automática por tiempo
 * - Métricas integradas
 *
 * Nota: Los países no se cachean ya que son datos de referencia que raramente cambian
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configuración optimizada para memoria limitada
        cacheManager.setCaffeine(Caffeine.newBuilder()
            // Máximo 300 entradas por caché total
            .maximumSize(300)

            // Expira después de 20 minutos sin acceso
            .expireAfterAccess(Duration.ofMinutes(20))

            // Expira después de 1 hora desde creación
            .expireAfterWrite(Duration.ofHours(1))

            // Habilitar métricas para monitoreo
            .recordStats());

        // Definir todos los cachés en un solo manager
        cacheManager.setCacheNames(Arrays.asList("actrices", "tiposEscena", "escenas"));

        return cacheManager;
    }
}
