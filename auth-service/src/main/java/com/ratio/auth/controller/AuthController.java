package com.ratio.auth.controller;

import com.ratio.auth.dto.LoginRequest;
import com.ratio.auth.dto.SignupRequest;
import com.ratio.auth.dto.VerifyRequest;
import com.ratio.auth.model.User;
import com.ratio.auth.repository.UserRepository;
import com.ratio.auth.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        User user = new User(request.getFullName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        if (request.getCompany() != null) user.setCompany(request.getCompany());
        if (request.getTeamSize() != null) user.setTeamSize(request.getTeamSize());
        user.setVerificationCode(code);
        user.setVerified(false);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "verificationCode", code,  // Return code so frontend can send via EmailJS
                "message", "Verification code generated"
        ));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody VerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }
        if (user.isVerified()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Already verified"));
        }
        if (!user.getVerificationCode().equals(request.getCode())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid code"));
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "message", "Email verified successfully"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }
        if (!user.isVerified()) {
            return ResponseEntity.status(403).body(Map.of("error", "Please verify your email first"));
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of("token", token, "email", user.getEmail(), "fullName", user.getFullName()));
    }
}