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
@Table(name = ConstantTable.SELLING_DETAIL)
public class SellingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "selling_id")
    @JsonBackReference
    private Selling selling;

    @ManyToOne
    @JoinColumn(name = "stuff_id", nullable = false)
    private Stuff stuff;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "selling_price")
    private Long sellingPrice;
}
