package com.enigma.bank_sampah.entity;

import com.enigma.bank_sampah.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = ConstantTable.CUSTOMER)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "ktp_number")
    private String ktpNumber;

    @Column(name = "balance")
    private Long balance;

    @OneToOne
    @JoinColumn(name = "ktp_image_id", unique = true)
    private Image ktpImage;

    @OneToOne
    @JoinColumn(name = "profile_image_id", unique = true)
    private Image profileImage;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_account_id", unique = true)
    private UserAccount userAccount;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<Token> tokens;

    @Column( name = "status")
    private Boolean status;

}
