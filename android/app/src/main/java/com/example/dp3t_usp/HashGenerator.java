package com.example.dp3t_usp;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Arrays;
import java.util.UUID;
import java.math.BigInteger;

public class HashGenerator{
    public static String Generate() {

        String uniqueID = UUID.randomUUID().toString();
        Log.d("check", "random uuid:" + uniqueID);
        MessageDigest message;
        try {
            message = MessageDigest.getInstance("SHA-224");
            byte[] result = message.digest(uniqueID.getBytes());
            Log.d("check", "original hash:" + result);
            Log.d("check", "bytes size original hash:" + result.length);

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, Arrays.copyOfRange(result, 0, 14));
            // Convert message digest into hex value
            String hashtext = no.toString(16);

            Log.d("check", "hash generated:" + hashtext);
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "error";
        }
    }
}


