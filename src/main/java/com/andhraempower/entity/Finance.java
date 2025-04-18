package com.andhraempower.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "village_project_expenses")
public class Finance  extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "village_project_id", nullable = false)
    private Integer villageProjectId;

    @Column(name = "expense_category_id")
    private Integer expenseCategoryId;

    @Column(name = "transaction_amount")
    private Float transactionAmount;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "committee_id")
    private Integer committeeId;

    @Column(name = "paid_to")
    private String paidTo;

    @Column(name = "expense_type")
    private String expenseType;

    @Column(name = "description")
    private String description;

    @Column(name = "bill_proofs")
    private String billProofs;

    @Column(name = "spent_by")
    private String spentBy;

    @Column(name = "approved_by")
    private String approvedBy;

    @Lob
    @Column(name = "bill_image", columnDefinition = "LONGBLOB")
    private byte[] billImage;
}