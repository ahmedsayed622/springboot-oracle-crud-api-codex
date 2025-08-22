package com.example.springbootcrudapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler للتعامل مع الأخطاء على مستوى التطبيق
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * معالج خطأ المصادقة
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        logger.error("Authentication error: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "خطأ في المصادقة");
        response.put("message", "فشل في المصادقة - تحقق من بيانات الدخول");
        response.put("timestamp", System.currentTimeMillis());
        response.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * معالج خطأ بيانات الدخول الخاطئة
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {

        logger.error("Bad credentials: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "بيانات دخول خاطئة");
        response.put("message", "اسم المستخدم أو كلمة المرور غير صحيحة");
        response.put("timestamp", System.currentTimeMillis());
        response.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * معالج خطأ عدم وجود صلاحية
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        logger.error("Access denied: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "ممنوع الوصول");
        response.put("message", "ليس لديك صلاحية للوصول لهذا المورد");
        response.put("timestamp", System.currentTimeMillis());
        response.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * معالج خطأ Runtime العام
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        logger.error("Runtime error: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "خطأ في تنفيذ العملية");
        response.put("message", ex.getMessage());
        response.put("timestamp", System.currentTimeMillis());
        response.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * معالج الأخطاء العامة
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex, WebRequest request) {

        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "خطأ في النظام");
        response.put("message", "حدث خطأ غير متوقع في النظام");
        response.put("timestamp", System.currentTimeMillis());
        response.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * معالج خطأ Illegal Argument
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        logger.error("Illegal argument: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "معطى غير صحيح");
        response.put("message", ex.getMessage());
        response.put("timestamp", System.currentTimeMillis());
        response.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
