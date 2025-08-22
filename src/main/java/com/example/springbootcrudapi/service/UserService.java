package com.example.springbootcrudapi.service;

import com.example.springbootcrudapi.entity.User;
import com.example.springbootcrudapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Service للتعامل مع عمليات المستخدمين
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * جلب جميع المستخدمين
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.debug("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * جلب مستخدم بواسطة ID
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        logger.debug("Fetching user by id: {}", id);
        return userRepository.findById(id);
    }

    /**
     * جلب مستخدم بواسطة اسم المستخدم
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        logger.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * جلب مستخدم بواسطة الإيميل
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * إنشاء مستخدم جديد
     */
    public User createUser(User user) {
        logger.debug("Creating new user: {}", user.getUsername());

        // التحقق من عدم وجود مستخدم بنفس اسم المستخدم
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("اسم المستخدم موجود بالفعل: " + user.getUsername());
        }

        // التحقق من عدم وجود مستخدم بنفس الإيميل
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("الإيميل موجود بالفعل: " + user.getEmail());
        }

        // تشفير كلمة المرور
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // حفظ المستخدم
        User savedUser = userRepository.save(user);
        logger.debug("User created successfully with id: {}", savedUser.getId());

        return savedUser;
    }

    /**
     * تحديث بيانات مستخدم
     */
    public User updateUser(Long id, User userDetails) {
        logger.debug("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود برقم: " + id));

        // تحديث البيانات (عدا كلمة المرور واسم المستخدم)
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            // التحقق من عدم وجود إيميل مكرر
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("الإيميل موجود بالفعل: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getFullName() != null) {
            user.setFullName(userDetails.getFullName());
        }

        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        if (userDetails.isEnabled() != user.isEnabled()) {
            user.setEnabled(userDetails.isEnabled());
        }

        User updatedUser = userRepository.save(user);
        logger.debug("User updated successfully: {}", updatedUser.getId());

        return updatedUser;
    }

    /**
     * تغيير كلمة المرور
     */
    public void changePassword(Long userId, String newPassword) {
        logger.debug("Changing password for user id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود برقم: " + userId));

        // تشفير كلمة المرور الجديدة
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        logger.debug("Password changed successfully for user: {}", user.getUsername());
    }

    /**
     * حذف مستخدم
     */
    public void deleteUser(Long id) {
        logger.debug("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("المستخدم غير موجود برقم: " + id);
        }

        userRepository.deleteById(id);
        logger.debug("User deleted successfully with id: {}", id);
    }

    /**
     * البحث عن المستخدمين بواسطة الاسم
     */
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String name) {
        logger.debug("Searching users by name: {}", name);
        return userRepository.findByFullNameContainingIgnoreCase(name);
    }

    /**
     * جلب المستخدمين المفعلين
     */
    @Transactional(readOnly = true)
    public List<User> getEnabledUsers() {
        logger.debug("Fetching enabled users");
        return userRepository.findByEnabledTrue();
    }

    /**
     * جلب المستخدمين حسب الدور
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(User.Role role) {
        logger.debug("Fetching users by role: {}", role);
        return userRepository.findByRole(role);
    }

    /**
     * تفعيل/إلغاء تفعيل مستخدم
     */
    public void toggleUserStatus(Long id) {
        logger.debug("Toggling user status for id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود برقم: " + id));

        user.setEnabled(!user.isEnabled());
        userRepository.save(user);

        logger.debug("User status toggled for user: {} - enabled: {}", user.getUsername(), user.isEnabled());
    }
}
