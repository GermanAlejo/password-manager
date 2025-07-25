package com.passwordmanager.password_manager.model;

import com.passwordmanager.password_manager.security.JwtService;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Component
public class KeyCache {
    private final JwtService jwtService;
    // Stores: <CacheKey, CacheEntry>
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Cache expiration settings (30 minutes)
    private static final long EXPIRATION_MINUTES = 30;
    private static final long CLEANUP_INTERVAL_MINUTES = 5;

    public KeyCache(JwtService jwtService) {
        this.jwtService = jwtService;
        // Schedule periodic cache cleanup
        scheduler.scheduleAtFixedRate(
                this::cleanExpiredEntries,
                CLEANUP_INTERVAL_MINUTES,
                CLEANUP_INTERVAL_MINUTES,
                TimeUnit.MINUTES
        );
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
        cache.clear();
    }

    public void put(String userId, String authToken, SecretKey key) {
        String cacheKey = createCacheKey(userId, authToken);
        cache.put(cacheKey, new CacheEntry(key, Instant.now()));
    }

    public Optional<SecretKey> get(String userId, String authToken) {
        String cacheKey = createCacheKey(userId, authToken);
        CacheEntry entry = cache.get(cacheKey);

        if (entry != null && !isExpired(entry)) {
            return Optional.of(entry.key());
        }
        return Optional.empty();
    }

    public void remove(String userId, String authToken) {
        String cacheKey = createCacheKey(userId, authToken);
        cache.remove(cacheKey);
    }

    public void removeAllForUser(String userId) {
        cache.keySet().removeIf(key -> key.startsWith(userId + ":"));
    }

    private String createCacheKey(String userId, String authToken) {
        return userId + ":" + extractTokenId(authToken);
    }

    private String extractTokenId(String authToken) {
        return jwtService.extractTokenId(authToken);
    }

    private boolean isExpired(CacheEntry entry) {
        return entry.createdAt()
                .plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES)
                .isBefore(Instant.now());
    }

    private void cleanExpiredEntries() {
        cache.entrySet().removeIf(entry -> isExpired(entry.getValue()));
    }

    // Cache entry record
    private record CacheEntry(SecretKey key, Instant createdAt) {}
}