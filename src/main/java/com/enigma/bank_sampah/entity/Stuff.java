package com.enigma.bank_sampah.entity;

import com.enigma.bank_sampah.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.STUFF)
public class Stuff {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "stuff_name")
    private String stuffName;

    @Column(name = "buying_price")
    private Long buyingPrice;

    @Column(name = "selling_price")
    private Long sellingPrice;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "status")
    private Boolean status;
}
