package com.endor;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class EncryptionObjects {
    public static MessageDigest md;
    public static Cipher c;
    public static SecureRandom sr;
    public static Random r;

    static void init() {
        try {
            md = MessageDigest.getInstance("SHA-512");
            c = Cipher.getInstance("DESede");
            sr = SecureRandom.getInstance("SHA1PRNG");
            r = new Random();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
