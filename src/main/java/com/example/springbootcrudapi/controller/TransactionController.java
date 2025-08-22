package com.example.springbootcrudapi.controller;

import com.example.springbootcrudapi.entity.Transaction;
import com.example.springbootcrudapi.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Transaction Controller للتعامل مع عمليات المعاملات
 */
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    /**
     * جلب جميع المعاملات
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        logger.debug("Request to get all transactions");

        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error getting all transactions: {}", e.getMessage());
            throw new RuntimeException("خطأ في جلب المعاملات: " + e.getMessage());
        }
    }

    /**
     * جلب معاملة بواسطة ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        logger.debug("Request to get transaction by id: {}", id);

        try {
            Optional<Transaction> transaction = transactionService.getTransactionById(id);

            if (transaction.isPresent()) {
                return ResponseEntity.ok(transaction.get());
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "غير موجود");
                response.put("message", "المعاملة غير موجودة برقم: " + id);
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            logger.error("Error getting transaction by id {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * جلب معاملة بواسطة رقم المعاملة
     */
    @GetMapping("/trans-id/{transId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getTransactionByTransId(@PathVariable String transId) {
        logger.debug("Request to get transaction by transId: {}", transId);

        try {
            Optional<Transaction> transaction = transactionService.getTransactionByTransId(transId);

            if (transaction.isPresent()) {
                return ResponseEntity.ok(transaction.get());
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "غير موجود");
                response.put("message", "المعاملة غير موجودة برقم المعاملة: " + transId);
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            logger.error("Error getting transaction by transId {}: {}", transId, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * إنشاء معاملة جديدة
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        logger.debug("Request to create new transaction: {}", transaction.getTransId());

        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "تم إنشاء المعاملة بنجاح");
            response.put("transaction", createdTransaction);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(201).body(response);

        } catch (RuntimeException e) {
            logger.error("Error creating transaction {}: {}", transaction.getTransId(), e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في إنشاء المعاملة");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(400).body(response);

        } catch (Exception e) {
            logger.error("Unexpected error creating transaction {}: {}", transaction.getTransId(), e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * تحديث بيانات معاملة
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        logger.debug("Request to update transaction with id: {}", id);

        try {
            Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "تم تحديث المعاملة بنجاح");
            response.put("transaction", updatedTransaction);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            logger.error("Error updating transaction {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في تحديث المعاملة");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(400).body(response);

        } catch (Exception e) {
            logger.error("Unexpected error updating transaction {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * حذف معاملة
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        logger.debug("Request to delete transaction with id: {}", id);

        try {
            transactionService.deleteTransaction(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "تم حذف المعاملة بنجاح");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            logger.error("Error deleting transaction {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في حذف المعاملة");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(400).body(response);

        } catch (Exception e) {
            logger.error("Unexpected error deleting transaction {}: {}", id, e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("error", "خطأ في النظام");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * البحث عن المعاملات بواسطة رقم الطرفية
     */
    @GetMapping("/terminal/{terminalId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> getTransactionsByTerminalId(@PathVariable String terminalId) {
        logger.debug("Request to get transactions by terminal id: {}", terminalId);

        try {
            List<Transaction> transactions = transactionService.getTransactionsByTerminalId(terminalId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error getting transactions by terminal id {}: {}", terminalId, e.getMessage());
            throw new RuntimeException("خطأ في جلب المعاملات: " + e.getMessage());
        }
    }

    /**
     * البحث عن المعاملات بواسطة اسم التاجر
     */
    @GetMapping("/search/merchant")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> searchTransactionsByMerchantName(@RequestParam String merchantName) {
        logger.debug("Request to search transactions by merchant name: {}", merchantName);

        try {
            List<Transaction> transactions = transactionService.searchTransactionsByMerchantName(merchantName);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error searching transactions by merchant name {}: {}", merchantName, e.getMessage());
            throw new RuntimeException("خطأ في البحث عن المعاملات: " + e.getMessage());
        }
    }

    /**
     * البحث عن المعاملات في فترة زمنية
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        logger.debug("Request to get transactions between {} and {}", startDate, endDate);

        try {
            List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error getting transactions by date range: {}", e.getMessage());
            throw new RuntimeException("خطأ في جلب المعاملات: " + e.getMessage());
        }
    }

    /**
     * البحث عن المعاملات بمبلغ أكبر من قيمة معينة
     */
    @GetMapping("/amount/above/{amount}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> getTransactionsAboveAmount(@PathVariable BigDecimal amount) {
        logger.debug("Request to get transactions above amount: {}", amount);

        try {
            List<Transaction> transactions = transactionService.getTransactionsAboveAmount(amount);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error getting transactions above amount {}: {}", amount, e.getMessage());
            throw new RuntimeException("خطأ في جلب المعاملات: " + e.getMessage());
        }
    }

    /**
     * البحث عن المعاملات في نطاق مبلغ معين
     */
    @GetMapping("/amount/range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> getTransactionsByAmountRange(
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount) {

        logger.debug("Request to get transactions between amounts {} and {}", minAmount, maxAmount);

        try {
            List<Transaction> transactions = transactionService.getTransactionsByAmountRange(minAmount, maxAmount);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error getting transactions by amount range: {}", e.getMessage());
            throw new RuntimeException("خطأ في جلب المعاملات: " + e.getMessage());
        }
    }

    /**
     * حساب إجمالي المبلغ للمعاملات في تاريخ معين
     */
    @GetMapping("/stats/total-amount")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getTotalAmountByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        logger.debug("Request to get total amount for date: {}", date);

        try {
            BigDecimal totalAmount = transactionService.getTotalAmountByDate(date);
            Long transactionCount = transactionService.getTransactionCountByDate(date);

            Map<String, Object> response = new HashMap<>();
            response.put("date", date);
            response.put("totalAmount", totalAmount);
            response.put("transactionCount", transactionCount);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting total amount by date: {}", e.getMessage());
            throw new RuntimeException("خطأ في حساب الإجمالي: " + e.getMessage());
        }
    }

    /**
     * جلب آخر المعاملات
     */
    @GetMapping("/latest")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> getLatestTransactions() {
        logger.debug("Request to get latest transactions");

        try {
            List<Transaction> transactions = transactionService.getLatestTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error getting latest transactions: {}", e.getMessage());
            throw new RuntimeException("خطأ في جلب آخر المعاملات: " + e.getMessage());
        }
    }

    /**
     * البحث الشامل في جميع حقول المعاملة
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> searchAllFields(@RequestParam String q) {
        logger.debug("Request to search all transaction fields with query: {}", q);

        try {
            List<Transaction> transactions = transactionService.searchAllFields(q);

            Map<String, Object> response = new HashMap<>();
            response.put("query", q);
            response.put("resultsCount", transactions.size());
            response.put("transactions", transactions);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching transactions with query {}: {}", q, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "خطأ في البحث");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("query", q);
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * البحث بكويري بارميتر - بسيط وعملي
     */
    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> filterTransactions(
            @RequestParam(required = false) String transId,
            @RequestParam(required = false) String terminalId,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String maskPan,
            @RequestParam(required = false) String authorizationNumber,
            @RequestParam(required = false) String merchantAccountNumber,
            @RequestParam(required = false) String outletCode,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount) {
        
        logger.debug("Filtering transactions with provided parameters");

        try {
            List<Transaction> transactions = transactionService.searchByMultipleCriteria(
                    transId, terminalId, merchantName, maskPan,
                    authorizationNumber, merchantAccountNumber, outletCode,
                    minAmount, maxAmount);

            return ResponseEntity.ok(transactions);

        } catch (Exception e) {
            logger.error("Error filtering transactions: {}", e.getMessage());
            throw new RuntimeException("خطأ في البحث: " + e.getMessage());
        }
    }
}
