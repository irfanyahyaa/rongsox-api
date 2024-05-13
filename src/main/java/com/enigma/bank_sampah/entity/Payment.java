package com.enigma.bank_sampah.entity;

import com.enigma.bank_sampah.constant.ConstantTable;
import com.enigma.bank_sampah.constant.SellingStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.PAYMENT)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "token")
    private String token;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "selling_status")
    private SellingStatus sellingStatus;

    @OneToOne(mappedBy = "payment")
    private Selling selling;
}
