package com.defitech.GestUni.service.Auth;

import com.defitech.GestUni.config.InvalidCredentialsException;
import com.defitech.GestUni.config.UserNotFoundException;
import com.defitech.GestUni.dto.authDto.AuthResponse;
import com.defitech.GestUni.dto.authDto.RefreshTokenRequest;
import com.defitech.GestUni.dto.authDto.SigninRequest;
import com.defitech.GestUni.models.Bases.Utilisateur;
import com.defitech.GestUni.repository.UtilisateurRepository;
import com.defitech.GestUni.service.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {


    private final UtilisateurRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


//    @Transactional
//    public Utilisateur signup(CreateUserRequest signUpRequest) {
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            throw new IllegalStateException("Email déjà utilisé.");
//        }
//        String generatedPassword = PasswordGenerator.generateRandomPassword();
//
//        Utilisateur user = new Utilisateur();
//        user.setEmail(signUpRequest.getEmail());
//        user.setPassword(new BCryptPasswordEncoder().encode(generatedPassword));
//        user.setRole(UserRole.valueOf(UserRole.DE.name()));
//
//        userRepository.save(user);
//        return user;
//    }
//
//    @Override
//    public String signupUser(CreateUserRequest signupRequest) {
//        return null;
//    }
//

    @Override
    public AuthResponse signin(SigninRequest signinRequest) {
        try {
            // Authentifie l'utilisateur par nom d'utilisateur et mot de passe
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signinRequest.getUsername(),
                            signinRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // Chercher l'utilisateur dans la base de données par nom d'utilisateur
        Utilisateur user = userRepository.findByUsername(signinRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + signinRequest.getUsername()));

        // Vérifier si le rôle correspond
        if (!user.getRole().name().equalsIgnoreCase(signinRequest.getRole())) {
            throw new InvalidCredentialsException("Invalid role for this user");
        }

        // Générer un token JWT pour l'utilisateur
        Long userId = user.getIdUser();
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        long expiresAt = System.currentTimeMillis() + 1000 * 60 * 60 * 10; // 10 heures
        String role = user.getAuthorities().iterator().next().getAuthority();

        return buildAuthResponse(user, userId, jwt, refreshToken, expiresAt, role);
    }

    private AuthResponse buildAuthResponse(Utilisateur user, Long userId, String jwt, String refreshToken, long expiresAt, String role) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(userId);
        authResponse.setJwt(jwt);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setExpiresAt(expiresAt);
        authResponse.setUsername(user.getUsername());
        authResponse.setRole(role);
        authResponse.setMessage("Authentication successful");
        return authResponse;
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getRefreshtoken());
        Utilisateur user = userRepository.findByUsername(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (jwtService.isTokenValid(refreshTokenRequest.getRefreshtoken(), user)) {
            String jwt = jwtService.generateToken(user);
            long expiresAt = System.currentTimeMillis() + 1000 * 60 * 60 * 10; // 10 hours
            String role = user.getRole().toString();


            Long userId = user.getIdUser();

            return buildAuthResponse(user, userId, jwt, refreshTokenRequest.getRefreshtoken(), expiresAt, role);
        }

        throw new IllegalArgumentException("Invalid refresh token");
    }

    @Override
    public Optional<Utilisateur> findByUtilisateur(String username) {
        return userRepository.findByUsername(username);
    }
}
