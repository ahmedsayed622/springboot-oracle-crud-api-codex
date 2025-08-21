package com.example.springbootcrudapi.controller;

import com.example.springbootcrudapi.entity.Transaction;
import com.example.springbootcrudapi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * GET /api/transactions
     * Gets all transactions from MD_TRANSACTION_CURRENT table
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/transactions/search
     * Search transactions with multiple query parameters
     * Example: /api/transactions/search?entityId=123&entityName=merchant&minAmount=100
     */
    @GetMapping("/search")
    public ResponseEntity<List<Transaction>> searchTransactions(
            @RequestParam(required = false) String entityId,
            @RequestParam(required = false) String entityName,
            @RequestParam(required = false) String terminalId,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) String maskedCardNumber,
            @RequestParam(required = false) String authorizationNumber,
            @RequestParam(required = false) String merchantAccountNumber,
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate) {
        
        try {
            List<Transaction> transactions = transactionService.searchTransactions(
                entityId, entityName, terminalId, transactionType,
                minAmount, maxAmount, maskedCardNumber, authorizationNumber,
                merchantAccountNumber, fromDate, toDate
            );
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/transactions/entity/{entityId}
     * Get transactions by entity ID (Outlet Code)
     */
    @GetMapping("/entity/{entityId}")
    public ResponseEntity<List<Transaction>> getTransactionsByEntityId(@PathVariable String entityId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByEntityId(entityId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/transactions/terminal/{terminalId}
     * Get transactions by terminal ID
     */
    @GetMapping("/terminal/{terminalId}")
    public ResponseEntity<List<Transaction>> getTransactionsByTerminalId(@PathVariable String terminalId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByTerminalId(terminalId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/transactions/auth/{authNumber}
     * Get transactions by authorization number
     */
    @GetMapping("/auth/{authNumber}")
    public ResponseEntity<List<Transaction>> getTransactionsByAuthNumber(@PathVariable String authNumber) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByAuthorizationNumber(authNumber);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/transactions/simple-search
     * Simplified search with common parameters
     * Example: /api/transactions/simple-search?entity=merchant&terminal=T123&amount=100
     */
    @GetMapping("/simple-search")
    public ResponseEntity<List<Transaction>> simpleSearch(
            @RequestParam(required = false) String entity,
            @RequestParam(required = false) String terminal,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String authNumber) {
        
        try {
            List<Transaction> transactions = transactionService.searchTransactions(
                entity,           // entityId
                entity,           // also search in entityName
                terminal,         // terminalId
                null,            // transactionType
                amount,          // minAmount
                amount,          // maxAmount (exact match if provided)
                cardNumber,      // maskedCardNumber
                authNumber,      // authorizationNumber
                null,            // merchantAccountNumber
                null,            // fromDate
                null             // toDate
            );
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/transactions/count
     * Get total count of transactions
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTransactionCount() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok((long) transactions.size());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET /api/transactions/summary
     * Get summary information about transactions
     */
    @GetMapping("/summary")
    public ResponseEntity<Object> getTransactionSummary() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            
            // Calculate summary statistics
            long totalCount = transactions.size();
            BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getTransactionAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal totalCommission = transactions.stream()
                .map(Transaction::getMerchantCommission)
                .filter(commission -> commission != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal totalNetAmount = totalAmount.subtract(totalCommission);
            
            // Create summary object
            var summary = new Object() {
                public final long totalTransactions = totalCount;
                public final BigDecimal totalAmount = totalAmount;
                public final BigDecimal totalCommission = totalCommission;
                public final BigDecimal totalNetAmount = totalNetAmount;
                public final String message = "Transaction summary calculated successfully";
            };
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
