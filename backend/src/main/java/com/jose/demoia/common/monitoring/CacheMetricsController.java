package com.jose.demoia.common.monitoring;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para monitorear métricas de caché
 */
@RestController
@RequestMapping("/actuator/cache")
public class CacheMetricsController {

    private final CacheManager cacheManager;

    public CacheMetricsController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("/stats")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();

        // Obtener estadísticas de todos los cachés
        cacheManager.getCacheNames().forEach(cacheName -> {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache) {
                CaffeineCache caffeineCache = (CaffeineCache) cache;
                Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                CacheStats cacheStats = nativeCache.stats();

                Map<String, Object> cacheInfo = new HashMap<>();
                cacheInfo.put("size", nativeCache.estimatedSize());
                cacheInfo.put("hitCount", cacheStats.hitCount());
                cacheInfo.put("missCount", cacheStats.missCount());
                cacheInfo.put("hitRate", String.format("%.2f%%", cacheStats.hitRate() * 100));
                cacheInfo.put("evictionCount", cacheStats.evictionCount());
                cacheInfo.put("loadTime", cacheStats.totalLoadTime());

                stats.put(cacheName, cacheInfo);
            }
        });

        return stats;
    }

    @GetMapping("/summary")
    public Map<String, Object> getCacheSummary() {
        Map<String, Object> summary = new HashMap<>();

        long totalSize = 0;
        long totalHits = 0;
        long totalMisses = 0;
        double totalMemoryMB = 0;

        for (String cacheName : cacheManager.getCacheNames()) {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache) {
                CaffeineCache caffeineCache = (CaffeineCache) cache;
                Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                CacheStats cacheStats = nativeCache.stats();

                totalSize += nativeCache.estimatedSize();
                totalHits += cacheStats.hitCount();
                totalMisses += cacheStats.missCount();

                // Estimación aproximada de memoria (cada entrada ~1KB promedio)
                totalMemoryMB += (nativeCache.estimatedSize() * 0.001);
            }
        }

        summary.put("totalEntries", totalSize);
        summary.put("totalHits", totalHits);
        summary.put("totalMisses", totalMisses);
        summary.put("overallHitRate",
            totalHits + totalMisses > 0 ?
            String.format("%.2f%%", ((double) totalHits / (totalHits + totalMisses)) * 100) :
            "0.00%");
        summary.put("estimatedMemoryMB", String.format("%.2f MB", totalMemoryMB));

        return summary;
    }

    @GetMapping("/clear")
    public Map<String, String> clearAllCaches() {
        Map<String, String> result = new HashMap<>();

        cacheManager.getCacheNames().forEach(cacheName -> {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                result.put(cacheName, "cleared");
            }
        });

        result.put("status", "All caches cleared successfully");
        return result;
    }
}
