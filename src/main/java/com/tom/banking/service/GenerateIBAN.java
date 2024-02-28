package com.tom.banking.service;

import java.util.Random;

public class GenerateIBAN {

    private static long convertToNumeric(String input) {
        StringBuilder numericString = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (Character.isLetter(ch)) {
                numericString.append((int) ch - 55);
            } else {
                numericString.append(ch);
            }
        }
        return Long.parseLong(numericString.toString().substring(0, 15));
    }
    public static String generateIBAN(String countryCode, String bankCode) {

        String accountNumber = String.format("%010d", new Random().nextInt(1000000000));
        String tempIBAN = bankCode + accountNumber + countryCode + "00";
        long checkDigits = 98 - (convertToNumeric(tempIBAN) % 97);

        return countryCode + String.format("%02d", checkDigits) + bankCode + accountNumber;
    }

}
