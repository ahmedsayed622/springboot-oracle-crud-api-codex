package com.example.springbootcrudapi.controller;

import com.example.springbootcrudapi.dto.JwtResponse;
import com.example.springbootcrudapi.dto.LoginRequest;
import com.example.springbootcrudapi.dto.RegisterRequest;
import com.example.springbootcrudapi.entity.User;
import com.example.springbootcrudapi.security.JwtUtil;
import com.example.springbootcrudapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller للتعامل مع عمليات المصادقة
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * تسجيل دخول المستخدم
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.debug("Login attempt for user: {}", loginRequest.getUsernameOrEmail());

        try {
            // إجراء المصادقة
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()));

            // تعيين المصادقة في Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // الحصول على تفاصيل المستخدم
            User userDetails = (User) authentication.getPrincipal();

            // إنشاء JWT Token
            String jwt = jwtUtil.generateToken(userDetails);

            logger.debug("Login successful for user: {}", userDetails.getUsername());

            // إنشاء الاستجابة
            JwtResponse jwtResponse = new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getFullName(),
                    userDetails.getRole().name());

            return ResponseEntity.ok(jwtResponse);

        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {} - Error: {}",
                    loginRequest.getUsernameOrEmail(), e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "فشل في المصادقة");
            response.put("message", "اسم المستخدم أو كلمة المرور غير صحيحة");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * تسجيل مستخدم جديد
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        logger.debug("Register attempt for user: {}", registerRequest.getUsername());

        try {
            // إنشاء كائن User جديد
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(registerRequest.getPassword()); // سيتم تشفيرها في UserService
            newUser.setFullName(registerRequest.getFullName());
            newUser.setRole(User.Role.USER); // مستخدم عادي بشكل افتراضي
            newUser.setEnabled(true);

            // إنشاء المستخدم
            User createdUser = userService.createUser(newUser);

            logger.debug("User registered successfully: {}", createdUser.getUsername());

            // إنشاء JWT Token تلقائياً بعد التسجيل
            String jwt = jwtUtil.generateToken(createdUser);

            // إنشاء الاستجابة
            JwtResponse jwtResponse = new JwtResponse(
                    jwt,
                    createdUser.getId(),
                    createdUser.getUsername(),
                    createdUser.getEmail(),
                    createdUser.getFullName(),
                    createdUser.getRole().name());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "تم تسجيل المستخدم بنجاح");
            response.put("user", jwtResponse);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(201).body(response);

        } catch (RuntimeException e) {
            logger.error("Registration failed for user: {} - Error: {}",
                    registerRequest.getUsername(), e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "فشل في التسجيل");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(400).body(response);

        } catch (Exception e) {
            logger.error("Unexpected error during registration for user: {} - Error: {}",
                    registerRequest.getUsername(), e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", "حدث خطأ غير متوقع أثناء التسجيل");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * تسجيل خروج المستخدم
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        logger.debug("Logout request received");

        // مسح Security Context
        SecurityContextHolder.clearContext();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "تم تسجيل الخروج بنجاح");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    /**
     * التحقق من صحة Token
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        logger.debug("Token validation request received");

        try {
            // إزالة "Bearer " من بداية Token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // التحقق من صحة Token
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);

                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", username);
                response.put("message", "Token صحيح");
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("message", "Token غير صحيح");
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.status(401).body(response);
            }

        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "خطأ في التحقق من Token");
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * جلب معلومات المستخدم الحالي
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        logger.debug("Get current user request received");

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();

                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("fullName", user.getFullName());
                response.put("role", user.getRole().name());
                response.put("enabled", user.isEnabled());
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "غير مصرح");
                response.put("message", "المستخدم غير مصادق عليه");
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.status(401).body(response);
            }

        } catch (Exception e) {
            logger.error("Error getting current user: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }
}
