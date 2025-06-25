package com.shose.shoseshop.controller;

import com.shose.shoseshop.configuration.CustomUserDetails;
import com.shose.shoseshop.configuration.security.JWTToken;
import com.shose.shoseshop.configuration.security.TokenProvider;
import com.shose.shoseshop.controller.vm.LoginVM;
import com.shose.shoseshop.entity.RefreshToken;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.RefreshTokenService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * Refresh access token using refresh token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String requestRefreshToken = body.get("refreshToken");

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
}
