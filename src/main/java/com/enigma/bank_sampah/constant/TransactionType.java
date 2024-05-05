package com.enigma.bank_sampah.constant;

import java.util.Arrays;

public enum TransactionType {
    DEPOSIT("deposit", "Deposit"),
    WITHDRAWAL("withdrawal", "WithDrawal");

    private String name;
    private String description;

    TransactionType(String name, String description){
        this.name = name;
        this.description = description;
    }

    public static TransactionType getByName(String name){
        return Arrays.stream(TransactionType.values())
                .filter(transactionType -> transactionType.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
