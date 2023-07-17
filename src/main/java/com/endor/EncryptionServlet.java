package com.endor;

import javax.crypto.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

@WebServlet(name = "EncryptionServlet")
public class EncryptionServlet extends HttpServlet {
    public EncryptionServlet() {
        EncryptionObjects.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HtmlUtil.printHtmlHeader(response);
        HtmlUtil.startBody(response);
        HtmlUtil.printMenu(response);
        HtmlUtil.openTable(response);
        HtmlUtil.openRow(response);
        HtmlUtil.openCol(response);
        HtmlUtil.printCurrentTitle("Encryption", response);

        StringBuilder form = new StringBuilder();
        form.append("<form action=\"encryption\">")
                .append("Data to encrypt: <input type=\"text\" name=\"data_to_encrypt\"default-length16\"><br><br><br>")
                .append("<input type=\"reset\" value=\"Reset Form\">")
                .append("<h3> MessageDigest </h3>")
                .append("<label> Select Encryption Algorithm </label>")
                .append("<select name =\"MessageDigestAlgorithm\">")
                .append("<option disabled selected value> -- Select an Encryption Algorithm -- </option>")
                .append("<option value = \"MD2\"> MD2</option>")
                .append("<option value = \"MD5\"> MD5</option>")
                .append("<option value = \"SHA-1\"> SHA-1</option>")
                .append("<option value = \"SHA-224\"> SHA-224</option>")
                .append("<option value = \"SHA-256\"> SHA-256</option>")
                .append("<option value = \"SHA-384\"> SHA-384</option>")
                .append("<option value = \"SHA-512\"> SHA-512</option>")
                .append("<option value = \"SHA-512/224\"> SHA-512/224</option>")
                .append("<option value = \"SHA-512/256\"> SHA-512/256</option>")
                .append("</select>")

                .append("<br><br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"MessageDigest1\">: java.security.MessageDigest.getInstance(String algorithm)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"MessageDigest2\">: java.security.MessageDigest.getInstance(String algorithm, Provider provider)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"MessageDigest3\">: java.security.MessageDigest.getInstance(String algorithm, String provider)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"MessageDigest4\">: external object for java.security.MessageDigest.getInstance(String algorithm)<br>")
                .append("<br><br>")

                .append("<h3> Crypto </h3>")
                .append("<label> Select Encryption Algorithm </label>")
                .append("<select name =\"CryptoAlgorithm\">")
                .append("<option disabled selected value> -- Select an Encryption Algorithm -- </option>")
                .append("<option value = \"AES/CBC/NoPadding\">AES/CBC/NoPadding</option>")
                .append("<option value = \"AES/CBC/PKCS5Padding\">AES/CBC/PKCS5Padding</option>")
                .append("<option value = \"AES/ECB/NoPadding\">AES/ECB/NoPadding</option>")
                .append("<option value = \"AES/ECB/PKCS5Padding\">AES/ECB/PKCS5Padding</option>")
                .append("<option value = \"DES/CBC/NoPadding\">DES/CBC/NoPadding</option>")
                .append("<option value = \"DES/CBC/PKCS5Padding\">DES/CBC/PKCS5Padding</option>")
                .append("<option value = \"DES/ECB/NoPadding\">DES/ECB/NoPadding</option>")
                .append("<option value = \"DES/ECB/PKCS5Padding\">DES/ECB/PKCS5Padding</option>")
                .append("<option value = \"DESede/CBC/NoPadding\">DESede/CBC/NoPadding</option>")
                .append("<option value = \"DESede/CBC/PKCS5Padding\">DESede/CBC/PKCS5Padding</option>")
                .append("<option value = \"DESede/ECB/NoPadding\">DESede/ECB/NoPadding</option>")
                .append("<option value = \"DESede/ECB/PKCS5Padding\">DESede/ECB/PKCS5Padding</option>")
                .append("<option value = \"RSA/ECB/PKCS1Padding\">RSA/ECB/PKCS1Padding</option>")
                .append("<option value = \"RSA/ECB/OAEPWithSHA-1AndMGF1Padding\">RSA/ECB/OAEPWithSHA-1AndMGF1Padding</option>")
                .append("<option value = \"RSA/ECB/OAEPWithSHA-256AndMGF1Padding\">RSA/ECB/OAEPWithSHA-256AndMGF1Padding</option>")
                .append("</select>")

                .append("&nbsp&nbsp&nbsp")

                .append("<select name =\"KeyGeneratorAlgorithm\">")
                .append("<option disabled selected value> -- Select an Algorithm for the KeyGenerator -- </option>")
                .append("<option value = \"AES\">AES</option>")
                .append("<option value = \"ARCFOUR\">ARCFOUR</option>")
                .append("<option value = \"Blowfish\">Blowfish</option>")
                .append("<option value = \"DES\">DES</option>")
                .append("<option value = \"DESede\">DESede</option>")
                .append("<option value = \"HmacMD5\">HmacMD5</option>")
                .append("<option value = \"HmacSHA1\">HmacSHA1</option>")
                .append("<option value = \"HmacSHA224\">HmacSHA224</option>")
                .append("<option value = \"HmacSHA256\">HmacSHA256</option>")
                .append("<option value = \"HmacSHA384\">HmacSHA384</option>")
                .append("<option value = \"HmacSHA512\">HmacSHA512</option>")
                .append("<option value = \"RC2\">RC2</option>")
                .append("<option value = \"RSA\">RSA</option>")
                .append("</select>")

                .append("<br><br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"Crypto1\">: javax.crypto.Cipher.getInstance(String transformation)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"Crypto2\">: javax.crypto.Cipher.getInstance(String transformation, Provider provider)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"Crypto3\">: javax.crypto.Cipher.getInstance(String transformation, String provider)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"Crypto4\">: external object for javax.crypto.Cipher.getInstance(String transformation)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"Crypto5\">: RSA for javax.crypto.Cipher.getInstance(String transformation)<br>")
                .append("<br><br>")

                .append("<h3> SecureRandom </h3>")
                .append("<label> Select Encryption Algorithm </label>")
                .append("<select name =\"SecureRandomAlgorithm\">")
                .append("<option disabled selected value> -- Select an Encryption Algorithm -- </option>")
                .append("<option value = \"NativePRNG\">NativePRNG</option>")
                .append("<option value = \"NativePRNGBlocking\">NativePRNGBlocking</option>")
                .append("<option value = \"NativePRNGNonBlocking\">NativePRNGNonBlocking</option>")
                .append("<option disabled value = \"PKCS11\">PKCS11</option>")
                .append("<option value = \"SHA1PRNG\">SHA1PRNG</option>")
                .append("<option disabled value = \"Windows-PRNG\">Windows-PRNG</option>")
                .append("</select>")

                .append("<br><br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"SecureRandom1\">: java.security.SecureRandom.getInstance(String algorithm).nextBytes(barray)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"SecureRandom2\">: java.security.SecureRandom.getInstance(String algorithm).nextDouble()<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"SecureRandom3\">: java.security.SecureRandom.getInstance(String algorithm).nextFloat()<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"SecureRandom4\">: java.security.SecureRandom.getInstance(String algorithm).nextGaussian()<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"SecureRandom5\">: java.security.SecureRandom.getInstance(String algorithm).nextInt()<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"SecureRandom6\">: java.security.SecureRandom.getInstance(String algorithm).nextInt(99)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"SecureRandom7\">: java.security.SecureRandom.getInstance(String algorithm).nextLong()<br>")

                .append("<br><br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"MathRandom1\">: java.lang.Math.random()<br>")

                .append("<br><br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"utilRandom1\">: java.util.Random().nextBytes(bytes)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"utilRandom2\">: java.util.Random().nextDouble()<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"utilRandom3\">: java.util.Random().nextFloat()<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"utilRandom4\">: java.util.Random().nextGaussian()<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"utilRandom5\">: java.util.Random().nextInt()<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"utilRandom6\">: java.util.Random().nextInt(99)<br>")
                .append("<input type=\"radio\" name=\"encryptiontype\" value=\"utilRandom7\">: java.util.Random().nextLong()<br>")
                .append("<br><br>")
                .append("<input type=\"submit\" value=\"Submit\">" + "</form>");
        out.println(form);

        HashMap<String, Integer> encryptionTypeMap = new HashMap<String, Integer>() {
            {
                put("MessageDigest1", 0);
                put("MessageDigest2", 1);
                put("MessageDigest3", 2);
                put("MessageDigest4", 3);
                put("Crypto1", 4);
                put("Crypto2", 5);
                put("Crypto3", 6);
                put("Crypto4", 7);
                put("Crypto5", 8);
                put("SecureRandom1", 9);
                put("SecureRandom2", 10);
                put("SecureRandom3", 11);
                put("SecureRandom4", 12);
                put("SecureRandom5", 13);
                put("SecureRandom6", 14);
                put("SecureRandom7", 15);
                put("MathRandom1", 16);
                put("utilRandom1", 17);
                put("utilRandom2", 18);
                put("utilRandom3", 19);
                put("utilRandom4", 20);
                put("utilRandom5", 21);
                put("utilRandom6", 22);
                put("utilRandom7", 23);
            }
        };

        HashMap<Integer, String> encryptionMethodMap = new HashMap<Integer, String>() {
            {
                put(0, "java.security.MessageDigest.getInstance(String algorithm)");
                put(1, "java.security.MessageDigest.getInstance(String algorithm, Provider provider)");
                put(2, "java.security.MessageDigest.getInstance(String algorithm, String provider)");
                put(3, "external object for java.security.MessageDigest.getInstance(String algorithm)");
                put(4, "javax.crypto.Cipher.getInstance(String transformation)");
                put(5, "javax.crypto.Cipher.getInstance(String transformation, Provider provider)");
                put(6, "javax.crypto.Cipher.getInstance(String transformation, String provider)");
                put(7, "external object for javax.crypto.Cipher.getInstance(String transformation)");
                put(8, "RSA for javax.crypto.Cipher.getInstance(String transformation)");
                put(9, "java.security.SecureRandom.getInstance(String algorithm).nextBytes(barray)");
                put(10, "java.security.SecureRandom.getInstance(String algorithm).nextDouble()");
                put(11, "java.security.SecureRandom.getInstance(String algorithm).nextFloat()");
                put(12, "java.security.SecureRandom.getInstance(String algorithm).nextGaussian()");
                put(13, "java.security.SecureRandom.getInstance(String algorithm).nextInt()");
                put(14, "java.security.SecureRandom.getInstance(String algorithm).nextInt(99)");
                put(15, "java.security.SecureRandom.getInstance(String algorithm).nextLong()");
                put(16, "java.lang.Math.random()");
                put(17, "java.util.Random().nextBytes(bytes)");
                put(18, "java.util.Random().nextDouble()");
                put(19, "java.util.Random().nextFloat()");
                put(20, "java.util.Random().nextGaussian()");
                put(21, "java.util.Random().nextInt()");
                put(22, "java.util.Random().nextInt(99)");
                put(23, "java.util.Random().nextLong()");
            }
        };

        String encryptionTypeStr = request.getParameter("encryptiontype");
        System.out.println("encryptionTypeStr - " + encryptionTypeStr);
        int encryptionType = encryptionTypeMap.get(encryptionTypeStr);

        String dataToEncrypt = request.getParameter("data_to_encrypt");
        String algorithm = "";
        String keyGeneratorAlgorithm = "";
        if(encryptionType < 3) algorithm = request.getParameter("MessageDigestAlgorithm");
        else if(encryptionType >= 4 && encryptionType < 9) {
            algorithm = request.getParameter("CryptoAlgorithm");
            keyGeneratorAlgorithm = request.getParameter("KeyGeneratorAlgorithm");
        }
        else if(encryptionType >= 8 && encryptionType < 16) algorithm = request.getParameter("SecureRandomAlgorithm");

        String returnValue = "Failed!";
        switch (encryptionType) {
            case 0:
                // "MessageDigest1"
                // java.security.MessageDigest.getInstance(String algorithm)
                returnValue = testMessageDigest1(dataToEncrypt, algorithm);
                break;
            case 1: 
                // "MessageDigest2"
                // java.security.MessageDigest.getInstance(String algorithm, Provider provider)
                returnValue = testMessageDigest2(dataToEncrypt, algorithm);
                break;
            case 2: 
                // "MessageDigest3"
                // java.security.MessageDigest.getInstance(String algorithm, String provider)
                returnValue = testMessageDigest3(dataToEncrypt, algorithm);
                break;
            case 3: 
                // "MessageDigest3"
                // java.security.MessageDigest.getInstance(String algorithm, Provider provider)
                returnValue = testMessageDigest4(dataToEncrypt);
                break;
            case 4: 
                // "Crypto1"
                // javax.crypto.Cipher.getInstance(String transformation)
                returnValue = testCrypto1(dataToEncrypt, algorithm, keyGeneratorAlgorithm);
                break;
            case 5: 
                // "Crypto2"
                // javax.crypto.Cipher.getInstance(String transformation, Provider provider)
                returnValue = testCrypto2(dataToEncrypt, algorithm, keyGeneratorAlgorithm);
                break;
            case 6: 
                // "Crypto3"
                // javax.crypto.Cipher.getInstance(String transformation, String provider)
                returnValue = testCrypto3(dataToEncrypt, algorithm, keyGeneratorAlgorithm);
                break;
            case 7: 
                // "Crypto4"
                // javax.crypto.Cipher.getInstance(String transformation)
                returnValue = testCrypto4(dataToEncrypt);
                break;
            case 8:
                // "Crypto5"
                // javax.crypto.Cipher.getInstance(String transformation)
                returnValue = testCrypto5(dataToEncrypt, algorithm);
                break;
            case 9:
                // "SecureRandom1"
                // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextBytes(barray)
                returnValue = testSecureRandom1(algorithm);
                break;
            case 10:
                // "SecureRandom2"
                // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextBytes(barray)
                returnValue = testSecureRandom2(algorithm);
                break;
            case 11:
                // "SecureRandom3"
                // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextBytes(barray)
                returnValue = testSecureRandom3(algorithm);
                break;
            case 12:
                // "SecureRandom4"
                // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextBytes(randomBytes)
                returnValue = testSecureRandom4(algorithm);
                break;
            case 13:
                // "SecureRandom5"
                // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextBytes(randomBytes)
                returnValue = testSecureRandom5(algorithm);
                break;
            case 14:
                // "SecureRandom6"
                // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextDouble()
                returnValue = testSecureRandom6(algorithm);
                break;
            case 15:
                // "SecureRandom7"
                // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextDouble()
                returnValue = testSecureRandom7(algorithm);
                break;
            case 16:
                // "MathRandom1"
                // java.lang.Math.random()
                returnValue = testMathRandom1();
                break;
            case 17:
                // "utilRandom1"
                // java.util.Random().nextBytes(bytes)
                returnValue = testUtilRandom1();
                break;
            case 18:
                // "utilRandom2"
                // java.util.Random().nextDouble()
                returnValue = testUtilRandom2();
                break;
            case 19:
                // "utilRandom3"
                // java.util.Random().nextFloat()
                returnValue = testUtilRandom3();
                break;
            case 20:
                // "utilRandom4"
                // java.util.Random().nextGaussian()
                returnValue = testUtilRandom4();
                break;
            case 21:
                // "utilRandom5"
                // java.util.Random().nextInt()
                returnValue = testUtilRandom5();
                break;
            case 22:
                // "utilRandom6"
                // java.util.Random().nextInt(99)
                returnValue = testUtilRandom6();
                break;
            case 23:
                // "utilRandom7"
                // java.util.Random().nextLong()
                returnValue = testUtilRandom7();
                break;
            default:
                System.out.println("Encryption Type not found");
        }

        HtmlUtil.closeCol(response);
        HtmlUtil.openCol(response);

        out.println("<h1> Encryption execution result</h1>");
        out.println("<br><br>");
        out.println("<h2> Method called - " + encryptionMethodMap.get(encryptionType) + "</h2>");
        if(encryptionType< 15) out.println("<h2> Input Provided</h2>");
        if(encryptionType< 15) out.println("<h3> Text - " + dataToEncrypt + "</h3>");
        if(algorithm.length() > 0) out.println("<h3> Algorithm - " + algorithm + "</h3>");
        if(keyGeneratorAlgorithm.length() > 0) out.println("<h3> KeyGeneratorAlgorithm - " + keyGeneratorAlgorithm + "</h3>");
        out.println("<h2> Result - " + returnValue + "</h2>");
        HtmlUtil.closeCol(response);
        HtmlUtil.closeRow(response);
        HtmlUtil.closeTable(response);
        out.println("</body>");
        out.println("</html>");
    }

    private String testMessageDigest1(String message, String algorithm) {
        // java.security.MessageDigest.getInstance(String algorithm)
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            System.out.println("md.toString() - " + md.toString());

            // Passing data to the created MessageDigest Object
            md.update(message.getBytes());

            // Compute the message digest
            byte[] digest = md.digest();

            System.out.println(Arrays.toString(digest));

            // Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            System.out.println("Hex format : " + hexString.toString());
            return "Success - Value returned = " + hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testMessageDigest2(String message, String algorithm) {
        // java.security.MessageDigest.getInstance(String algorithm, Provider provider)
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm, "SUN");

            System.out.println("md.toString() - " + md.toString());

            // Passing data to the created MessageDigest Object
            md.update(message.getBytes());

            // Compute the message digest
            byte[] digest = md.digest();

            System.out.println(digest);

            // Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            System.out.println("Hex format : " + hexString.toString());
            return "Success - Value returned = " + hexString.toString();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testMessageDigest3(String message, String algorithm) {
        // java.security.MessageDigest.getInstance(String algorithm, String provider)
        MessageDigest md = null;
        try {
            Provider[] provider = java.security.Security.getProviders();

            System.out.println(provider[0].getName());
            md = MessageDigest.getInstance(algorithm, provider[0]);

            System.out.println("md.toString() - " + md.toString());

            // Passing data to the created MessageDigest Object
            md.update(message.getBytes());

            // Compute the message digest
            byte[] digest = md.digest();

            System.out.println(digest);

            // Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            System.out.println("Hex format : " + hexString.toString());
            return "Success - Value returned = " + hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testMessageDigest4(String message) {
        // java.security.MessageDigest.getInstance(String algorithm, Provider provider)
        MessageDigest md = null;
        try {
            md = EncryptionObjects.md;

            System.out.println("md.toString() - " + md.toString());

            // Passing data to the created MessageDigest Object
            md.update(message.getBytes());

            // Compute the message digest
            byte[] digest = md.digest();

            System.out.println(digest);

            // Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            System.out.println("Hex format : " + hexString.toString());
            return "Success - Value returned = " + hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testCrypto1(String message, String algorithm, String keyGeneratorAlgorithm) {
        // javax.crypto.Cipher.getInstance(String transformation)
        try {
            Cipher c = Cipher.getInstance(algorithm);

            // Prepare the cipher to encrypt
            SecretKey key = KeyGenerator.getInstance(keyGeneratorAlgorithm).generateKey();
            c.init(javax.crypto.Cipher.ENCRYPT_MODE, key);

            // encrypt and store the results
            byte[] input = { (byte)'?' };
            Object inputParam = message;
            if (inputParam instanceof String) input = ((String) inputParam).getBytes();
            byte[] result = c.doFinal(input);

            // Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                hexString.append(Integer.toHexString(0xFF & result[i]));
            }
            System.out.println("Hex format : " + hexString.toString());
            return "Success - Value returned = " + hexString.toString();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testCrypto2(String message, String algorithm, String keyGeneratorAlgorithm) {
        // javax.crypto.Cipher.getInstance(String transformation, Provider provider)
        try {
            Cipher c = Cipher.getInstance(algorithm, "SunJCE");

            // Prepare the cipher to encrypt
            SecretKey key = KeyGenerator.getInstance(keyGeneratorAlgorithm).generateKey();
            c.init(javax.crypto.Cipher.ENCRYPT_MODE, key);

            // encrypt and store the results
            byte[] input = { (byte)'?' };
            Object inputParam = message;
            if (inputParam instanceof String) input = ((String) inputParam).getBytes();
            byte[] result = c.doFinal(input);

            // Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                hexString.append(Integer.toHexString(0xFF & result[i]));
            }
            System.out.println("Hex format : " + hexString.toString());
            return "Success - Value returned = " + hexString.toString();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testCrypto3(String message, String algorithm, String keyGeneratorAlgorithm) {
        // javax.crypto.Cipher.getInstance(String transformation, String provider)
        try {
            Cipher c = Cipher.getInstance(algorithm, Security.getProvider("SunJCE"));

            // Prepare the cipher to encrypt
            SecretKey key = KeyGenerator.getInstance(keyGeneratorAlgorithm).generateKey();
            c.init(javax.crypto.Cipher.ENCRYPT_MODE, key);

            // encrypt and store the results
            byte[] input = { (byte)'?' };
            Object inputParam = message;
            if (inputParam instanceof String) input = ((String) inputParam).getBytes();
            byte[] result = c.doFinal(input);

            // Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                hexString.append(Integer.toHexString(0xFF & result[i]));
            }
            System.out.println("Hex format : " + hexString.toString());
            return "Success - Value returned = " + hexString.toString();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testCrypto4(String message) {
        // javax.crypto.Cipher.getInstance(String transformation)
        try {
            Cipher c = EncryptionObjects.c;

            // Prepare the cipher to encrypt
            SecretKey key = KeyGenerator.getInstance("DESede").generateKey();
            c.init(javax.crypto.Cipher.ENCRYPT_MODE, key);

            // encrypt and store the results
            byte[] input = { (byte)'?' };
            Object inputParam = message;
            if (inputParam instanceof String) input = ((String) inputParam).getBytes();
            byte[] result = c.doFinal(input);

            // Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                hexString.append(Integer.toHexString(0xFF & result[i]));
            }
            System.out.println("Hex format : " + hexString.toString());
            return "Success - Value returned = " + hexString.toString();
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    // RSA algorithm credit - https://www.devglan.com/java8/rsa-encryption-decryption-java
    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";

    private String testCrypto5(String message, String algorithm) {
        // javax.crypto.Cipher.getInstance(String transformation, String provider)
        String encryptedString = "";
        try {
            Cipher cipher = Cipher.getInstance(algorithm);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(this.publicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(message.getBytes());

            encryptedString = Base64.getEncoder().encodeToString(result);
            System.out.println(encryptedString);
//            String decryptedString = decrypt(encryptedString, privateKey);
//            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | NoSuchPaddingException | InvalidKeySpecException e) {
            return "Failed with exception " + e.getMessage();
        }
        return "Success - Value returned = " + encryptedString;
    }

    private String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    private String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    private PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    private String testSecureRandom1(String algorithm) {
        // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextBytes(barray)
        try {
            SecureRandom.getInstance(algorithm).nextBytes(new byte[100]);
            return "Succeeded";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testSecureRandom2(String algorithm) {
        // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextDouble()
        try {
            return "Success - Value returned = " + SecureRandom.getInstance(algorithm).nextDouble() + "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testSecureRandom3(String algorithm) {
        // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextFloat()
        try {
            return "Success - Value returned = " + SecureRandom.getInstance(algorithm).nextFloat() + "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testSecureRandom4(String algorithm) {
        // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextGaussian()
        try {
            return "Success - Value returned = " + SecureRandom.getInstance(algorithm).nextGaussian() + "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testSecureRandom5(String algorithm) {
        // java.security.SecureRandom.getInstance("SHA1PRNG").nextInt()
        try {
            return "Success - Value returned = " + SecureRandom.getInstance(algorithm).nextInt() + "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testSecureRandom6(String algorithm) {
        // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextInt(99)
        try {
            return "Success - Value returned = " + SecureRandom.getInstance(algorithm).nextInt(99) + "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testSecureRandom7(String algorithm) {
        // java.security.SecureRandom.getInstance(\"SHA1PRNG\").nextLong()
        try {
            return "Success - Value returned = " + SecureRandom.getInstance(algorithm).nextLong() + "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Failed with exception " + e.getMessage();
        }
    }

    private String testMathRandom1() {
        // java.lang.Math.random()
        return "Success - Value returned = " + java.lang.Math.random() + "";
    }

    private String testUtilRandom1() {
        // java.util.Random().nextBytes(bytes)
        EncryptionObjects.r.nextBytes(new byte[100]);
        return "Succeeded";
    }

    private String testUtilRandom2() {
        // java.util.Random().nextDouble()
        return "Success - Value returned = " + EncryptionObjects.r.nextDouble() + "";
    }

    private String testUtilRandom3() {
        // java.util.Random().nextFloat()
        return "Success - Value returned = " + EncryptionObjects.r.nextFloat() + "";
    }

    private String testUtilRandom4() {
        // java.util.Random().nextGaussian()
        return "Success - Value returned = " + EncryptionObjects.r.nextGaussian() + "";
    }

    private String testUtilRandom5() {
        // java.util.Random().nextInt()
        return "Success - Value returned = " + EncryptionObjects.r.nextInt() + "";
    }

    private String testUtilRandom6() {
        // java.util.Random().nextInt(99)
        return "Success - Value returned = " + EncryptionObjects.r.nextInt(99) + "";
    }

    private String testUtilRandom7() {
        // java.util.Random().nextLong()
        return "Success - Value returned = " + EncryptionObjects.r.nextLong() + "";
    }
}
