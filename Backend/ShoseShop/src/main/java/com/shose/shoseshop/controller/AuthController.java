package com.shose.shoseshop.controller;

import com.shose.shoseshop.configuration.CustomUserDetails;
import com.shose.shoseshop.configuration.security.JWTToken;
import com.shose.shoseshop.configuration.security.TokenProvider;
import com.shose.shoseshop.controller.vm.LoginVM;
import com.shose.shoseshop.entity.RefreshToken;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    RefreshTokenService refreshTokenService;
    TokenProvider tokenProvider;
    UserRepository userRepository;
    AuthenticationManager authenticationManager;

    /**
     * Login endpoint - returns access token and refresh token
     */
    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(@Valid @RequestBody LoginVM loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createToken(authentication);
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        JWTToken jwtToken = new JWTToken(
                accessToken,
                refreshToken.getToken(),
                authentication.getAuthorities().toString()
        );

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(String requestRefreshToken) {
        if (requestRefreshToken == null || requestRefreshToken.isBlank()) {
            return ResponseEntity.badRequest().body("Missing refresh token");
        }

        return refreshTokenService.getByToken(requestRefreshToken)
                .flatMap(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    Long userId = refreshToken.getUserId();
                    User user = userRepository.findById(userId).orElse(null);
                    if (user == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
                    }

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            new CustomUserDetails(user.getId(), user.getUsername(), user.getRole()),
                            null,
                            List.of()
                    );

                    String newAccessToken = tokenProvider.createToken(authentication);
                    JWTToken jwtToken = new JWTToken(newAccessToken, requestRefreshToken, user.getRole().name());

                    return ResponseEntity.ok(jwtToken);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Refresh token is invalid or expired"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String jwt = resolveToken(request);
        if (jwt == null || jwt.isBlank()) {
            return ResponseEntity.badRequest().body("Missing access token");
        }

        Authentication authentication = tokenProvider.getAuthentication(jwt);
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Blacklist access token
        long ttlSeconds = tokenProvider.getTokenValidityInSeconds();
        tokenProvider.blacklistToken(jwt, ttlSeconds);

        // Xóa refresh token của user
        refreshTokenService.deleteByUserId(user.getId());

        // Xóa SecurityContext (tùy chọn, vì JWT là stateless)
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logout successful");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
