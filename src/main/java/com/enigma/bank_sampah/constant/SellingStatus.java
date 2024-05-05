package com.enigma.bank_sampah.constant;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SellingStatus {
    // ordered, pending, settlement, cancel, deny, expire, failure
    ORDERED("ordered", "Ordered"),
    PENDING("pending", "Pending"),
    SETTLEMENT("settlement", "Settlement"),
    CANCEL("cancel", "Cancel"),
    DENY("deny", "Deny"),
    EXPIRE("expire", "Expire"),
    FAILURE("failure", "Failure");

    private String name;
    private String description;

    SellingStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static SellingStatus getByName(String name) {
        return Arrays.stream(values())
                .filter(transactionStatus -> transactionStatus.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
