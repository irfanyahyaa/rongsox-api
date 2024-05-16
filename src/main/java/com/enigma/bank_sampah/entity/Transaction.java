package com.enigma.bank_sampah.entity;

import com.enigma.bank_sampah.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TRANSACTION)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "transaction_type")
    private String transactionType;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    @JsonBackReference
    private BankAccount bankAccount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "amount")
    private Long amount;

    @OneToOne
    @JoinColumn(name = "transfer_receipt")
    private Image transferReceipt;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "transaction")
    @JsonManagedReference
    private List<TransactionDetail> transactionDetails;
}
