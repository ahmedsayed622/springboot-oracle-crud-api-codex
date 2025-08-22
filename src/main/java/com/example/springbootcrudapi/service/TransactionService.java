package com.example.springbootcrudapi.service;

import com.example.springbootcrudapi.entity.Transaction;
import com.example.springbootcrudapi.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Transaction Service للتعامل مع عمليات المعاملات
 */
@Service
@Transactional
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * جلب جميع المعاملات
     */
    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        logger.debug("Fetching all transactions");
        return transactionRepository.findAll();
    }

    /**
     * جلب معاملة بواسطة ID
     */
    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionById(Long id) {
        logger.debug("Fetching transaction by id: {}", id);
        return transactionRepository.findById(id);
    }

    /**
     * جلب معاملة بواسطة رقم المعاملة
     */
    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionByTransId(String transId) {
        logger.debug("Fetching transaction by transId: {}", transId);
        return transactionRepository.findByTransId(transId);
    }

    /**
     * إنشاء معاملة جديدة
     */
    public Transaction createTransaction(Transaction transaction) {
        logger.debug("Creating new transaction: {}", transaction.getTransId());

        // التحقق من عدم وجود معاملة بنفس رقم المعاملة
        if (transaction.getTransId() != null &&
                transactionRepository.findByTransId(transaction.getTransId()).isPresent()) {
            throw new RuntimeException("رقم المعاملة موجود بالفعل: " + transaction.getTransId());
        }

        // تعيين تاريخ المعالجة إذا لم يكن موجوداً
        if (transaction.getProcessingDate() == null) {
            transaction.setProcessingDate(LocalDateTime.now());
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.debug("Transaction created successfully with id: {}", savedTransaction.getId());

        return savedTransaction;
    }

    /**
     * تحديث بيانات معاملة
     */
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        logger.debug("Updating transaction with id: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المعاملة غير موجودة برقم: " + id));

        // تحديث البيانات
        if (transactionDetails.getTerminalId() != null) {
            transaction.setTerminalId(transactionDetails.getTerminalId());
        }

        if (transactionDetails.getMerchantName() != null) {
            transaction.setMerchantName(transactionDetails.getMerchantName());
        }

        if (transactionDetails.getSourceAmount() != null) {
            transaction.setSourceAmount(transactionDetails.getSourceAmount());
        }

        if (transactionDetails.getMerchantCommission() != null) {
            transaction.setMerchantCommission(transactionDetails.getMerchantCommission());
        }

        if (transactionDetails.getMaskPan() != null) {
            transaction.setMaskPan(transactionDetails.getMaskPan());
        }

        if (transactionDetails.getAuthorizationNumber() != null) {
            transaction.setAuthorizationNumber(transactionDetails.getAuthorizationNumber());
        }

        if (transactionDetails.getMerchantAccountNumber() != null) {
            transaction.setMerchantAccountNumber(transactionDetails.getMerchantAccountNumber());
        }

        if (transactionDetails.getOutletCode() != null) {
            transaction.setOutletCode(transactionDetails.getOutletCode());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        logger.debug("Transaction updated successfully: {}", updatedTransaction.getId());

        return updatedTransaction;
    }

    /**
     * حذف معاملة
     */
    public void deleteTransaction(Long id) {
        logger.debug("Deleting transaction with id: {}", id);

        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("المعاملة غير موجودة برقم: " + id);
        }

        transactionRepository.deleteById(id);
        logger.debug("Transaction deleted successfully with id: {}", id);
    }

    /**
     * البحث عن المعاملات بواسطة رقم الطرفية
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByTerminalId(String terminalId) {
        logger.debug("Fetching transactions by terminal id: {}", terminalId);
        return transactionRepository.findByTerminalId(terminalId);
    }

    /**
     * البحث عن المعاملات بواسطة اسم التاجر
     */
    @Transactional(readOnly = true)
    public List<Transaction> searchTransactionsByMerchantName(String merchantName) {
        logger.debug("Searching transactions by merchant name: {}", merchantName);
        return transactionRepository.findByMerchantNameContainingIgnoreCase(merchantName);
    }

    /**
     * البحث عن المعاملات في فترة زمنية
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Fetching transactions between {} and {}", startDate, endDate);
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    /**
     * البحث عن المعاملات بمبلغ أكبر من قيمة معينة
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsAboveAmount(BigDecimal amount) {
        logger.debug("Fetching transactions above amount: {}", amount);
        return transactionRepository.findBySourceAmountGreaterThan(amount);
    }

    /**
     * البحث عن المعاملات بمبلغ أقل من قيمة معينة
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsBelowAmount(BigDecimal amount) {
        logger.debug("Fetching transactions below amount: {}", amount);
        return transactionRepository.findBySourceAmountLessThan(amount);
    }

    /**
     * البحث عن المعاملات في نطاق مبلغ معين
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        logger.debug("Fetching transactions between amounts {} and {}", minAmount, maxAmount);
        return transactionRepository.findByAmountRange(minAmount, maxAmount);
    }

    /**
     * البحث عن المعاملات بواسطة رقم حساب التاجر
     */
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByMerchantAccount(String merchantAccountNumber) {
        logger.debug("Fetching transactions by merchant account: {}", merchantAccountNumber);
        return transactionRepository.findByMerchantAccountNumber(merchantAccountNumber);
    }

    /**
     * حساب إجمالي المبلغ للمعاملات في تاريخ معين
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountByDate(LocalDateTime date) {
        logger.debug("Calculating total amount for date: {}", date);
        BigDecimal total = transactionRepository.sumAmountByDate(date);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * عدد المعاملات في تاريخ معين
     */
    @Transactional(readOnly = true)
    public Long getTransactionCountByDate(LocalDateTime date) {
        logger.debug("Counting transactions for date: {}", date);
        return transactionRepository.countTransactionsByDate(date);
    }

    /**
     * جلب آخر المعاملات
     */
    @Transactional(readOnly = true)
    public List<Transaction> getLatestTransactions() {
        logger.debug("Fetching latest transactions");
        return transactionRepository.findLatestTransactions();
    }

    /**
     * البحث الشامل في جميع حقول المعاملة
     */
    @Transactional(readOnly = true)
    public List<Transaction> searchAllFields(String query) {
        logger.debug("Searching all transaction fields with query: {}", query);
        if (query == null || query.trim().isEmpty()) {
            logger.warn("Empty search query provided, returning all transactions");
            return getAllTransactions();
        }
        return transactionRepository.searchAllFields(query.trim());
    }

    /**
     * البحث بمعايير متعددة
     */
    @Transactional(readOnly = true)
    public List<Transaction> searchByMultipleCriteria(
            String transId, String terminalId, String merchantName,
            String maskPan, String authorizationNumber, String merchantAccountNumber,
            String outletCode, BigDecimal minAmount, BigDecimal maxAmount) {
        
        logger.debug("Searching transactions with multiple criteria - transId: {}, terminalId: {}, merchantName: {}", 
                    transId, terminalId, merchantName);
        
        return transactionRepository.searchByMultipleCriteria(
                transId, terminalId, merchantName, maskPan, 
                authorizationNumber, merchantAccountNumber, outletCode,
                minAmount, maxAmount);
    }
}
