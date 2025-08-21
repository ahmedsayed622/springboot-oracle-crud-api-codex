package com.example.springbootcrudapi.service;

import com.example.springbootcrudapi.entity.Transaction;
import com.example.springbootcrudapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all transactions using native query
    public List<Transaction> getAllTransactions() {
        List<Object[]> results = transactionRepository.findAllTransactionsNative();
        return mapObjectArraysToTransactions(results);
    }

    // Search transactions with multiple parameters
    public List<Transaction> searchTransactions(
            String entityId,
            String entityName,
            String terminalId,
            String transactionType,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String maskedCardNumber,
            String authorizationNumber,
            String merchantAccountNumber,
            LocalDateTime fromDate,
            LocalDateTime toDate) {
        
        List<Object[]> results = transactionRepository.searchTransactions(
                entityId, entityName, terminalId, transactionType,
                minAmount, maxAmount, maskedCardNumber, authorizationNumber,
                merchantAccountNumber, fromDate, toDate
        );
        return mapObjectArraysToTransactions(results);
    }

    // Convenient search methods
    public List<Transaction> getTransactionsByEntityId(String entityId) {
        return transactionRepository.findByEntityId(entityId);
    }

    public List<Transaction> getTransactionsByTerminalId(String terminalId) {
        return transactionRepository.findByTerminalId(terminalId);
    }

    public List<Transaction> getTransactionsByAuthorizationNumber(String authNumber) {
        return transactionRepository.findByAuthorizationNumber(authNumber);
    }

    // Helper method to map Object[] to Transaction objects
    private List<Transaction> mapObjectArraysToTransactions(List<Object[]> results) {
        List<Transaction> transactions = new ArrayList<>();
        
        for (Object[] row : results) {
            Transaction transaction = new Transaction();
            
            // Map the native query results to Transaction object
            transaction.setSeq(row[0] != null ? ((Number) row[0]).longValue() : null);
            transaction.setEntityId((String) row[1]);
            transaction.setEntityName((String) row[2]);
            transaction.setTerminalId((String) row[3]);
            transaction.setTransactionType((String) row[4]);
            transaction.setTransactionAmount(row[5] != null ? (BigDecimal) row[5] : null);
            transaction.setTransactionDateFormatted((String) row[6]);
            transaction.setTransactionTimeFormatted((String) row[7]);
            transaction.setMaskedCardNumber((String) row[8]);
            transaction.setAuthorizationNumber((String) row[9]);
            transaction.setMerchantCommission(row[10] != null ? (BigDecimal) row[10] : null);
            // row[11] is calculated net amount - we'll calculate it in the getter
            transaction.setMerchantAccountNumber((String) row[12]);
            transaction.setProcessingDateFormatted((String) row[13]);
            
            // Set actual dates if available
            if (row.length > 14) {
                transaction.setTransactionDate(row[14] != null ? (LocalDateTime) row[14] : null);
            }
            if (row.length > 15) {
                transaction.setProcessingDate(row[15] != null ? (LocalDateTime) row[15] : null);
            }
            
            transactions.add(transaction);
        }
        
        return transactions;
    }

    // Helper method to parse date strings (if needed)
    private LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss a");
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            // Try alternative format
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDateTime.parse(dateStr + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
