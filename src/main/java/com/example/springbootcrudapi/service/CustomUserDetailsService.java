package com.example.springbootcrudapi.service;

import com.example.springbootcrudapi.entity.User;
import com.example.springbootcrudapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Details Service للتعامل مع تحميل بيانات المستخدم للمصادقة
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.debug("Loading user by username or email: {}", usernameOrEmail);

        // البحث عن المستخدم بواسطة اسم المستخدم أو الإيميل
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> {
                    logger.error("User not found with username or email: {}", usernameOrEmail);
                    return new UsernameNotFoundException("المستخدم غير موجود: " + usernameOrEmail);
                });

        logger.debug("User found: {}", user.getUsername());
        return user;
    }
}
