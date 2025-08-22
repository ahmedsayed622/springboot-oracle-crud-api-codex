package com.example.springbootcrudapi.repository;

import com.example.springbootcrudapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository للتعامل مع بيانات المعاملات
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * البحث عن معاملة بواسطة رقم المعاملة
     */
    Optional<Transaction> findByTransId(String transId);

    /**
     * البحث عن المعاملات بواسطة رقم الطرفية
     */
    List<Transaction> findByTerminalId(String terminalId);

    /**
     * البحث عن المعاملات بواسطة اسم التاجر
     */
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.merchantName) LIKE LOWER(CONCAT('%', :merchantName, '%'))")
    List<Transaction> findByMerchantNameContainingIgnoreCase(@Param("merchantName") String merchantName);

    /**
     * البحث عن المعاملات في فترة زمنية محددة
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * البحث عن المعاملات بمبلغ أكبر من قيمة معينة
     */
    List<Transaction> findBySourceAmountGreaterThan(BigDecimal amount);

    /**
     * البحث عن المعاملات بمبلغ أقل من قيمة معينة
     */
    List<Transaction> findBySourceAmountLessThan(BigDecimal amount);

    /**
     * البحث عن المعاملات في نطاق مبلغ معين
     */
    @Query("SELECT t FROM Transaction t WHERE t.sourceAmount BETWEEN :minAmount AND :maxAmount ORDER BY t.sourceAmount DESC")
    List<Transaction> findByAmountRange(@Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount);

    /**
     * البحث عن المعاملات بواسطة رقم التفويض
     */
    Optional<Transaction> findByAuthorizationNumber(String authorizationNumber);

    /**
     * البحث عن المعاملات بواسطة رقم حساب التاجر
     */
    List<Transaction> findByMerchantAccountNumber(String merchantAccountNumber);

    /**
     * البحث عن المعاملات بواسطة رمز المنفذ
     */
    List<Transaction> findByOutletCode(String outletCode);

    /**
     * حساب إجمالي المبلغ للمعاملات في تاريخ معين
     */
    @Query("SELECT SUM(t.sourceAmount) FROM Transaction t WHERE DATE(t.transactionDate) = DATE(:date)")
    BigDecimal sumAmountByDate(@Param("date") LocalDateTime date);

    /**
     * عدد المعاملات في تاريخ معين
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE DATE(t.transactionDate) = DATE(:date)")
    Long countTransactionsByDate(@Param("date") LocalDateTime date);

    /**
     * البحث عن آخر المعاملات
     */
    @Query("SELECT t FROM Transaction t ORDER BY t.transactionDate DESC")
    List<Transaction> findLatestTransactions();

    /**
     * البحث الشامل في جميع حقول المعاملة
     */
    @Query("SELECT t FROM Transaction t WHERE " +
            "LOWER(t.transId) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.terminalId) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.merchantName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.maskPan) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.authorizationNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.merchantAccountNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.outletCode) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(t.sourceAmount AS string) LIKE CONCAT('%', :query, '%') OR " +
            "CAST(t.merchantCommission AS string) LIKE CONCAT('%', :query, '%')")
    List<Transaction> searchAllFields(@Param("query") String query);

    /**
     * البحث بمعايير متعددة باستخدام query parameters منفصلة
     */
    @Query("SELECT t FROM Transaction t WHERE " +
            "(:transId IS NULL OR LOWER(t.transId) LIKE LOWER(CONCAT('%', :transId, '%'))) AND " +
            "(:terminalId IS NULL OR LOWER(t.terminalId) LIKE LOWER(CONCAT('%', :terminalId, '%'))) AND " +
            "(:merchantName IS NULL OR LOWER(t.merchantName) LIKE LOWER(CONCAT('%', :merchantName, '%'))) AND " +
            "(:maskPan IS NULL OR LOWER(t.maskPan) LIKE LOWER(CONCAT('%', :maskPan, '%'))) AND " +
            "(:authorizationNumber IS NULL OR LOWER(t.authorizationNumber) LIKE LOWER(CONCAT('%', :authorizationNumber, '%'))) AND " +
            "(:merchantAccountNumber IS NULL OR LOWER(t.merchantAccountNumber) LIKE LOWER(CONCAT('%', :merchantAccountNumber, '%'))) AND " +
            "(:outletCode IS NULL OR LOWER(t.outletCode) LIKE LOWER(CONCAT('%', :outletCode, '%'))) AND " +
            "(:minAmount IS NULL OR t.sourceAmount >= :minAmount) AND " +
            "(:maxAmount IS NULL OR t.sourceAmount <= :maxAmount)")
    List<Transaction> searchByMultipleCriteria(
            @Param("transId") String transId,
            @Param("terminalId") String terminalId,
            @Param("merchantName") String merchantName,
            @Param("maskPan") String maskPan,
            @Param("authorizationNumber") String authorizationNumber,
            @Param("merchantAccountNumber") String merchantAccountNumber,
            @Param("outletCode") String outletCode,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount);
}
