package com.example.springbootcrudapi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Entity لجدول المعاملات
 * يحتوي على تفاصيل المعاملات المالية
 */
@Entity
@Table(name = "MD_TRANSACTION_CURRENT")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "TRANSACTION_SEQ", initialValue = 1, allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TRANS_ID", length = 50)
    private String transId;

    @Column(name = "TERMINAL_ID", length = 20)
    private String terminalId;

    @Column(name = "MERCHANT_NAME", length = 100)
    private String merchantName;

    @Column(name = "SOURCE_AMOUNT", precision = 15, scale = 2)
    private BigDecimal sourceAmount;

    @Column(name = "MERCHANT_COMMISSION", precision = 15, scale = 2)
    private BigDecimal merchantCommission;

    @Column(name = "TRANSACTION_DATE")
    private LocalDateTime transactionDate;

    @Column(name = "PROCESSING_DATE")
    private LocalDateTime processingDate;

    @Column(name = "MASK_PAN", length = 20)
    private String maskPan;

    @Column(name = "AUTHORIZATION_NUMBER", length = 20)
    private String authorizationNumber;

    @Column(name = "MERCHANT_ACCOUNT_NUMBER", length = 30)
    private String merchantAccountNumber;

    @Column(name = "OUTLET_CODE", length = 20)
    private String outletCode;

    // Default constructor
    public Transaction() {
    }

    // Constructor with main fields
    public Transaction(String transId, String terminalId, String merchantName,
            BigDecimal sourceAmount, LocalDateTime transactionDate) {
        this.transId = transId;
        this.terminalId = terminalId;
        this.merchantName = merchantName;
        this.sourceAmount = sourceAmount;
        this.transactionDate = transactionDate;
        this.processingDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    public BigDecimal getMerchantCommission() {
        return merchantCommission;
    }

    public void setMerchantCommission(BigDecimal merchantCommission) {
        this.merchantCommission = merchantCommission;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDateTime getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(LocalDateTime processingDate) {
        this.processingDate = processingDate;
    }

    public String getMaskPan() {
        return maskPan;
    }

    public void setMaskPan(String maskPan) {
        this.maskPan = maskPan;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public String getMerchantAccountNumber() {
        return merchantAccountNumber;
    }

    public void setMerchantAccountNumber(String merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transId='" + transId + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", sourceAmount=" + sourceAmount +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
