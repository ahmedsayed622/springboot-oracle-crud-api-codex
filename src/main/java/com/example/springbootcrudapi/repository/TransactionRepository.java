package com.example.springbootcrudapi.repository;

import com.example.springbootcrudapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Native query that matches your SQL exactly
    @Query(value = """
        SELECT ROWNUM                                               as seq,
               OUTLET_CODE                                          as entity_id,
               MERCHANT_NAME                                        as entity_name,
               TERMINAL_ID                                          as terminal_id,
               TRANS_ID                                             as transaction_type,
               SOURCE_AMOUNT                                        as transaction_amount,
               TO_CHAR(TRANSACTION_DATE-3 , 'DD/MM/YYYY')           as transaction_date_formatted,
               TO_CHAR(TRANSACTION_DATE-3 , 'HH:MI:SS AM')          as transaction_time_formatted,
               MASK_PAN                                             as masked_card_number,
               AUTHORIZATION_NUMBER                                 as authorization_number,
               MERCHANT_COMMISSION                                  as merchant_commission,
               SOURCE_AMOUNT - MERCHANT_COMMISSION                 as transaction_net_amount,
               MERCHANT_ACCOUNT_NUMBER                              as merchant_account_number,
               TO_CHAR(PROCESSING_DATE , 'DD/MM/YYYY HH:MI:SS AM')  as processing_date_formatted,
               TRANSACTION_DATE                                     as transaction_date,
               PROCESSING_DATE                                      as processing_date
        FROM MD_TRANSACTION_CURRENT
        ORDER BY ROWNUM
        """, nativeQuery = true)
    List<Object[]> findAllTransactionsNative();

    // Search with query parameters
    @Query(value = """
        SELECT ROWNUM                                               as seq,
               OUTLET_CODE                                          as entity_id,
               MERCHANT_NAME                                        as entity_name,
               TERMINAL_ID                                          as terminal_id,
               TRANS_ID                                             as transaction_type,
               SOURCE_AMOUNT                                        as transaction_amount,
               TO_CHAR(TRANSACTION_DATE-3 , 'DD/MM/YYYY')           as transaction_date_formatted,
               TO_CHAR(TRANSACTION_DATE-3 , 'HH:MI:SS AM')          as transaction_time_formatted,
               MASK_PAN                                             as masked_card_number,
               AUTHORIZATION_NUMBER                                 as authorization_number,
               MERCHANT_COMMISSION                                  as merchant_commission,
               SOURCE_AMOUNT - MERCHANT_COMMISSION                 as transaction_net_amount,
               MERCHANT_ACCOUNT_NUMBER                              as merchant_account_number,
               TO_CHAR(PROCESSING_DATE , 'DD/MM/YYYY HH:MI:SS AM')  as processing_date_formatted,
               TRANSACTION_DATE                                     as transaction_date,
               PROCESSING_DATE                                      as processing_date
        FROM MD_TRANSACTION_CURRENT
        WHERE (:entityId IS NULL OR OUTLET_CODE = :entityId)
          AND (:entityName IS NULL OR UPPER(MERCHANT_NAME) LIKE UPPER('%' || :entityName || '%'))
          AND (:terminalId IS NULL OR TERMINAL_ID = :terminalId)
          AND (:transactionType IS NULL OR TRANS_ID = :transactionType)
          AND (:minAmount IS NULL OR SOURCE_AMOUNT >= :minAmount)
          AND (:maxAmount IS NULL OR SOURCE_AMOUNT <= :maxAmount)
          AND (:maskedCardNumber IS NULL OR MASK_PAN = :maskedCardNumber)
          AND (:authorizationNumber IS NULL OR AUTHORIZATION_NUMBER = :authorizationNumber)
          AND (:merchantAccountNumber IS NULL OR MERCHANT_ACCOUNT_NUMBER = :merchantAccountNumber)
          AND (:fromDate IS NULL OR TRANSACTION_DATE >= :fromDate)
          AND (:toDate IS NULL OR TRANSACTION_DATE <= :toDate)
        ORDER BY ROWNUM
        """, nativeQuery = true)
    List<Object[]> searchTransactions(
            @Param("entityId") String entityId,
            @Param("entityName") String entityName,
            @Param("terminalId") String terminalId,
            @Param("transactionType") String transactionType,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("maskedCardNumber") String maskedCardNumber,
            @Param("authorizationNumber") String authorizationNumber,
            @Param("merchantAccountNumber") String merchantAccountNumber,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    // Additional convenient search methods
    @Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT WHERE OUTLET_CODE = :entityId", nativeQuery = true)
    List<Transaction> findByEntityId(@Param("entityId") String entityId);

    @Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT WHERE TERMINAL_ID = :terminalId", nativeQuery = true)
    List<Transaction> findByTerminalId(@Param("terminalId") String terminalId);

    @Query(value = "SELECT * FROM MD_TRANSACTION_CURRENT WHERE AUTHORIZATION_NUMBER = :authNumber", nativeQuery = true)
    List<Transaction> findByAuthorizationNumber(@Param("authNumber") String authNumber);
}
