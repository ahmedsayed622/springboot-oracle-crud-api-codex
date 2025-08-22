package com.example.springbootcrudapi.controller;

import com.example.springbootcrudapi.entity.User;
import com.example.springbootcrudapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * User Controller للتعامل مع عمليات المستخدمين
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * جلب جميع المستخدمين
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.debug("Request to get all users");

        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error getting all users: {}", e.getMessage());
            throw new RuntimeException("خطأ في جلب المستخدمين: " + e.getMessage());
        }
    }

    /**
     * جلب مستخدم بواسطة ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        logger.debug("Request to get user by id: {}", id);

        try {
            Optional<User> user = userService.getUserById(id);

            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "غير موجود");
                response.put("message", "المستخدم غير موجود برقم: " + id);
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            logger.error("Error getting user by id {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * جلب مستخدم بواسطة اسم المستخدم
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        logger.debug("Request to get user by username: {}", username);

        try {
            Optional<User> user = userService.getUserByUsername(username);

            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "غير موجود");
                response.put("message", "المستخدم غير موجود باسم: " + username);
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            logger.error("Error getting user by username {}: {}", username, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * إنشاء مستخدم جديد (للمطورين فقط)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        logger.debug("Request to create new user: {}", user.getUsername());

        try {
            User createdUser = userService.createUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "تم إنشاء المستخدم بنجاح");
            response.put("user", createdUser);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(201).body(response);

        } catch (RuntimeException e) {
            logger.error("Error creating user {}: {}", user.getUsername(), e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في إنشاء المستخدم");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(400).body(response);

        } catch (Exception e) {
            logger.error("Unexpected error creating user {}: {}", user.getUsername(), e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * تحديث بيانات مستخدم
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        logger.debug("Request to update user with id: {}", id);

        try {
            User updatedUser = userService.updateUser(id, userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "تم تحديث المستخدم بنجاح");
            response.put("user", updatedUser);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            logger.error("Error updating user {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في تحديث المستخدم");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(400).body(response);

        } catch (Exception e) {
            logger.error("Unexpected error updating user {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * حذف مستخدم
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.debug("Request to delete user with id: {}", id);

        try {
            userService.deleteUser(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "تم حذف المستخدم بنجاح");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            logger.error("Error deleting user {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في حذف المستخدم");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(400).body(response);

        } catch (Exception e) {
            logger.error("Unexpected error deleting user {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * البحث عن المستخدمين بواسطة الاسم
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        logger.debug("Request to search users by name: {}", name);

        try {
            List<User> users = userService.searchUsersByName(name);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error searching users by name {}: {}", name, e.getMessage());
            throw new RuntimeException("خطأ في البحث عن المستخدمين: " + e.getMessage());
        }
    }

    /**
     * جلب المستخدمين المفعلين
     */
    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<User>> getEnabledUsers() {
        logger.debug("Request to get enabled users");

        try {
            List<User> users = userService.getEnabledUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error getting enabled users: {}", e.getMessage());
            throw new RuntimeException("خطأ في جلب المستخدمين المفعلين: " + e.getMessage());
        }
    }

    /**
     * تفعيل/إلغاء تفعيل مستخدم
     */
    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        logger.debug("Request to toggle status for user with id: {}", id);

        try {
            userService.toggleUserStatus(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "تم تغيير حالة المستخدم بنجاح");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            logger.error("Error toggling user status {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في تغيير حالة المستخدم");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(400).body(response);

        } catch (Exception e) {
            logger.error("Unexpected error toggling user status {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }
}
