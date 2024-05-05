package com.enigma.bank_sampah.entity;

import com.enigma.bank_sampah.constant.ConstantTable;
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
@Table(name = ConstantTable.SELLING)
public class Selling {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "selling_date", updatable = false)
    private Date sellingDate;

    @OneToMany(mappedBy = "selling")
    @JsonManagedReference
    private List<SellingDetail> sellingDetails;
}
