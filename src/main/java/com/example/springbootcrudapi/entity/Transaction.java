package com.example.springbootcrudapi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MD_TRANSACTION_CURRENT")
public class Transaction {

    @Id
    @Column(name = "ROWNUM")
    private Long seq;

    @Column(name = "OUTLET_CODE")
    private String entityId;

    @Column(name = "MERCHANT_NAME")
    private String entityName;

    @Column(name = "TERMINAL_ID")
    private String terminalId;

    @Column(name = "TRANS_ID")
    private String transactionType;

    @Column(name = "SOURCE_AMOUNT")
    private BigDecimal transactionAmount;

    @Column(name = "TRANSACTION_DATE")
    private LocalDateTime transactionDate;

    @Column(name = "MASK_PAN")
    private String maskedCardNumber;

    @Column(name = "AUTHORIZATION_NUMBER")
    private String authorizationNumber;

    @Column(name = "MERCHANT_COMMISSION")
    private BigDecimal merchantCommission;

    @Column(name = "MERCHANT_ACCOUNT_NUMBER")
    private String merchantAccountNumber;

    @Column(name = "PROCESSING_DATE")
    private LocalDateTime processingDate;

    // Calculated field - not mapped to database column
    @Transient
    private BigDecimal transactionNetAmount;

    @Transient
    private String transactionDateFormatted;

    @Transient
    private String transactionTimeFormatted;

    @Transient
    private String processingDateFormatted;

    // Default constructor
    public Transaction() {}

    // Constructor with all fields
    public Transaction(Long seq, String entityId, String entityName, String terminalId, 
                      String transactionType, BigDecimal transactionAmount, 
                      LocalDateTime transactionDate, String maskedCardNumber, 
                      String authorizationNumber, BigDecimal merchantCommission, 
                      String merchantAccountNumber, LocalDateTime processingDate) {
        this.seq = seq;
        this.entityId = entityId;
        this.entityName = entityName;
        this.terminalId = terminalId;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.maskedCardNumber = maskedCardNumber;
        this.authorizationNumber = authorizationNumber;
        this.merchantCommission = merchantCommission;
        this.merchantAccountNumber = merchantAccountNumber;
        this.processingDate = processingDate;
    }

    // Calculate net amount (Source_Amount - Merchant_commission)
    public BigDecimal getTransactionNetAmount() {
        if (transactionAmount != null && merchantCommission != null) {
            return transactionAmount.subtract(merchantCommission);
        }
        return BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public BigDecimal getMerchantCommission() {
        return merchantCommission;
    }

    public void setMerchantCommission(BigDecimal merchantCommission) {
        this.merchantCommission = merchantCommission;
    }

    public String getMerchantAccountNumber() {
        return merchantAccountNumber;
    }

    public void setMerchantAccountNumber(String merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
    }

    public LocalDateTime getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(LocalDateTime processingDate) {
        this.processingDate = processingDate;
    }

    public String getTransactionDateFormatted() {
        return transactionDateFormatted;
    }

    public void setTransactionDateFormatted(String transactionDateFormatted) {
        this.transactionDateFormatted = transactionDateFormatted;
    }

    public String getTransactionTimeFormatted() {
        return transactionTimeFormatted;
    }

    public void setTransactionTimeFormatted(String transactionTimeFormatted) {
        this.transactionTimeFormatted = transactionTimeFormatted;
    }

    public String getProcessingDateFormatted() {
        return processingDateFormatted;
    }

    public void setProcessingDateFormatted(String processingDateFormatted) {
        this.processingDateFormatted = processingDateFormatted;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "seq=" + seq +
                ", entityId='" + entityId + '\'' +
                ", entityName='" + entityName + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", transactionAmount=" + transactionAmount +
                ", transactionDate=" + transactionDate +
                ", maskedCardNumber='" + maskedCardNumber + '\'' +
                ", authorizationNumber='" + authorizationNumber + '\'' +
                ", merchantCommission=" + merchantCommission +
                ", merchantAccountNumber='" + merchantAccountNumber + '\'' +
                ", processingDate=" + processingDate +
                '}';
    }
}
