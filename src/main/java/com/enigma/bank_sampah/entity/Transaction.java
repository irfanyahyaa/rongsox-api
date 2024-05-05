package com.enigma.bank_sampah.entity;

import com.enigma.bank_sampah.constant.ConstantTable;
import com.enigma.bank_sampah.constant.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue( strategy = GenerationType.UUID)
    private String id;

    @Temporal( TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    @Column( name = "transaction_type")
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    @JsonBackReference
    private BankAccount bankAccount;

    @OneToOne
    @JoinColumn( name = "admin_id")
    private Admin admin;

    @OneToOne
    @JoinColumn( name = "bank_id")
    private Bank bank;

    @OneToOne
    @JoinColumn( name = "withdrawal_image_id")
    private Image withdrawalImageId;

    @Column( name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionDetail> transactionDetails;
}
