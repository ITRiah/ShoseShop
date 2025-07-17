package com.shose.shoseshop.service;

import com.shose.shoseshop.entity.RefreshToken;
import com.shose.shoseshop.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${security.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidity;

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        deleteByUserId(userId);
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenValidity));
        token.setToken(UUID.randomUUID().toString());

        refreshTokenRepository.save(token);
        redisTemplate.opsForValue().set(
                "refresh_token:" + userId + ":" + token.getToken(),
                token.getToken(),
                refreshTokenValidity,
                TimeUnit.SECONDS
        );

        return token;
    }

    public Optional<RefreshToken> getByToken(String token) {
        String redisKeyPattern = "refresh_token:*:" + token;
        Set<String> keys = redisTemplate.keys(redisKeyPattern);
        if (!keys.isEmpty()) {
            String redisToken = redisTemplate.opsForValue().get(keys.iterator().next());
            if (redisToken != null) {
                return refreshTokenRepository.findByToken(token);
            }
        }
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            deleteByToken(token.getToken());
            return Optional.empty();
        }
        return Optional.of(token);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
        Set<String> keys = redisTemplate.keys("refresh_token:" + userId + ":*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
        Set<String> keys = redisTemplate.keys("refresh_token:*:" + token);
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}

