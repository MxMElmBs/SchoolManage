package com.defitech.GestUni.controller;

import com.defitech.GestUni.dto.authDto.AuthResponse;
import com.defitech.GestUni.dto.authDto.CreateUserRequest;
import com.defitech.GestUni.dto.authDto.SigninRequest;
import com.defitech.GestUni.models.Bases.Utilisateur;
import com.defitech.GestUni.service.Auth.AuthServiceImpl;
import com.defitech.GestUni.service.Auth.IAuthService;
import com.defitech.GestUni.service.utils.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthServiceImpl authServiceImpl;
    @Autowired
    private JwtServiceImpl jwtService;

//    @PostMapping("/createUser")
//    public ResponseEntity<String> signupUser(@RequestBody CreateUserRequest signupRequest) {
//        try {
//            Utilisateur newUser = authServiceImpl.signup(signupRequest);
//            String responseMessage = "Signup successful! Please check your email to see your account.";
//            return ResponseEntity.ok(responseMessage);
//        } catch (IllegalStateException e) {
//            String responseMessage = e.getMessage();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
//        }
//    }

    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        try {
            String username = jwtService.extractUserName(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenExpired(refreshToken)) {
                return ResponseEntity.status(403).body("Refresh token expired");
            }

            String newToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(newToken);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid refresh token");
        }
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> signin(@RequestBody SigninRequest signinRequest) {
        AuthResponse response = authService.signin(signinRequest);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/confirm")
//    public ResponseEntity<String> confirmUser(@RequestBody ConfirmationRequest request) {
//        System.out.println("Received email: " + request.getEmail() + ", confirmationCode: " + request.getConfirmationCode());
//        boolean result = authServiceImpl.confirmUser(request.getEmail(), request.getConfirmationCode());
//        if (result) {
//            return ResponseEntity.ok("User confirmed successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Confirmation failed. Invalid email or confirmation code.");
//        }
//    }
//
//    @PostMapping("/password-change/initiate")
//    public ResponseEntity<String> initiatePasswordChange(@RequestParam String email) {
//        try {
//            authServiceImpl.initiatePasswordChange(email);
//            return ResponseEntity.ok("Password change initiated. Check your email for the confirmation code.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error initiating password change: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/password-change/confirm")
//    public ResponseEntity<String> confirmPasswordChange(@RequestBody PasswordChangeRequest passwordChangeRequest) {
//        try {
//            boolean result = authServiceImpl.confirmPasswordChange(passwordChangeRequest);
//            if (result) {
//                return ResponseEntity.ok("Password changed successfully.");
//            } else {
//                return ResponseEntity.badRequest().body("Password change failed. Invalid confirmation code or email.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error confirming password change: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/resend-confirmation")
//    public ResponseEntity<String> resendConfirmationCode(@RequestBody @Validated ResendConfirmationCodeRequest request) {
//        try {
//            authServiceImpl.resendConfirmationCode(request.getEmail());
//            return ResponseEntity.ok("Confirmation code resent.");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

}
