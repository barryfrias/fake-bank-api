package org.blfrias.fakebank.util;

import java.util.Random;

public class BankAccountNumberGenerator {

    private static final String BANK_CODE_PREFIX = "FBNK-"; // Replace with your bank code prefix

    public static String generateAccountNumber() {
        // Generate a random 10-digit numeric string
        Random random = new Random();
        StringBuilder accountNumberBuilder = new StringBuilder();
        accountNumberBuilder.append(BANK_CODE_PREFIX);
        for (int i = 0; i < 10 - BANK_CODE_PREFIX.length(); i++) {
            accountNumberBuilder.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return accountNumberBuilder.toString();
    }

    public static void main(String[] args) {
        String accountNumber = generateAccountNumber();
        System.out.println("Generated Bank Account Number: " + accountNumber);
    }
}