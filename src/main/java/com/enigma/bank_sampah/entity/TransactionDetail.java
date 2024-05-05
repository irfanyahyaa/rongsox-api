package com.enigma.bank_sampah.entity;

import com.enigma.bank_sampah.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TRANSACTION_DETAIL)
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn( name = "transaction_id")
    @JsonBackReference
    private Transaction transaction;

    @ManyToOne
    @JoinColumn( name = "stuff_id")
    private Stuff stuff;

    @Column( name = "weight")
    private Integer weight;

    @Column(name = "buying_price")
    private  Long buyingPrice;
}
