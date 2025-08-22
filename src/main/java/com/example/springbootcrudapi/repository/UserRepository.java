package com.example.springbootcrudapi.repository;

import com.example.springbootcrudapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository للتعامل مع بيانات المستخدمين
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * البحث عن مستخدم بواسطة اسم المستخدم
     */
    Optional<User> findByUsername(String username);

    /**
     * البحث عن مستخدم بواسطة الإيميل
     */
    Optional<User> findByEmail(String email);

    /**
     * البحث عن مستخدم بواسطة اسم المستخدم أو الإيميل
     */
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    /**
     * التحقق من وجود مستخدم بنفس اسم المستخدم
     */
    boolean existsByUsername(String username);

    /**
     * التحقق من وجود مستخدم بنفس الإيميل
     */
    boolean existsByEmail(String email);

    /**
     * البحث عن المستخدمين المفعلين
     */
    List<User> findByEnabledTrue();

    /**
     * البحث عن المستخدمين بواسطة الدور
     */
    List<User> findByRole(User.Role role);

    /**
     * البحث عن المستخدمين بواسطة الاسم الكامل (يحتوي على)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);
}
