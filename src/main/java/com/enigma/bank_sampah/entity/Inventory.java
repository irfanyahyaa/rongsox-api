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
@Table(name = ConstantTable.INVENTORY)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "stuff_id")
    private Stuff stuff;

    @Column(name = "total_weight")
    private Integer totalWeight;
}
