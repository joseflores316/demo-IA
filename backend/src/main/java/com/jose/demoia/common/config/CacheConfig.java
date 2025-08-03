package com.jose.demoia.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

/**
 * Configuración de caché optimizada para Railway con memoria limitada
 *
 * Características:
 * - Límite máximo de entradas por caché
 * - Expiración automática por tiempo
 * - Limpieza automática de memoria
 * - Métricas integradas
 *
 * Nota: Los países no se cachean ya que son datos de referencia que raramente cambian
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configuración optimizada para memoria limitada
        cacheManager.setCaffeine(Caffeine.newBuilder()
            // Máximo 300 entradas por caché (reducido sin países)
            .maximumSize(300)

            // Expira después de 20 minutos sin acceso
            .expireAfterAccess(Duration.ofMinutes(20))

            // Expira después de 1 hora desde creación
            .expireAfterWrite(Duration.ofHours(1))

            // Habilitar métricas para monitoreo
            .recordStats());

        // Definir cachés específicos (sin países) - usar Arrays.asList
        cacheManager.setCacheNames(Arrays.asList("actrices", "tiposEscena", "escenas"));

        return cacheManager;
    }

    /**
     * Configuración específica para caché de actrices
     * Menor tamaño porque incluyen imágenes URL
     */
    @Bean("actricesCacheManager")
    public CacheManager actricesCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("actrices");
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(100) // Menos entradas para actrices
            .expireAfterAccess(Duration.ofMinutes(15))
            .expireAfterWrite(Duration.ofMinutes(45))
            .recordStats());
        return cacheManager;
    }

    /**
     * Configuración para datos de escenas y tipos
     * Pueden mantenerse un tiempo moderado en caché
     */
    @Bean("escenasCacheManager")
    public CacheManager escenasCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(Arrays.asList("escenas", "tiposEscena"));
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(150)
            .expireAfterAccess(Duration.ofMinutes(30))
            .expireAfterWrite(Duration.ofHours(2))
            .recordStats());
        return cacheManager;
    }
}
