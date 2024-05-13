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
@Table(name = ConstantTable.BANK)
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "bank_name")
    private String name;

    @Column(name = "bank_code")
    private String bankCode;
}
