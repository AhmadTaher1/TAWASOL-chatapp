package shared.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing_UtilityClass {

    public static String hashString(String input) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Apply the hash function to the input
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b); // Convert to unsigned hexadecimal
                if (hex.length() == 1) hexString.append('0'); // Add leading zero for single-digit hex values
                hexString.append(hex);
            }

            return hexString.toString(); // Return the hashed string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Hashing algorithm not found!", e);
        }
    }
}