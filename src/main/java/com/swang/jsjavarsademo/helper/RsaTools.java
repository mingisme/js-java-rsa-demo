package com.swang.jsjavarsademo.helper;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class RsaTools {

    public static final String CHAR_ENCODING = "UTF-8";
//    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final String RSA_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    public static final String RSA = "RSA";
    public static final String PUBLIC_KEY = "PUBLIC_KEY";
    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    public String encrypt(String source, String publicKey) throws Exception {
        Key key = parsePublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        byte[] b1 = cipher.doFinal(b);
        return new String(Base64.getEncoder().encode(b1),
                CHAR_ENCODING);
    }

    public String decrypt(String encryptedText, String privateKey) throws Exception {
        Key key = parsePrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.getDecoder().decode(encryptedText.getBytes());
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    public PublicKey parsePublicKey(String key) throws Exception {
        try (StringReader reader = new StringReader(key);
             PEMParser pemParser = new PEMParser(reader)) {
            SubjectPublicKeyInfo subjectPublicKeyInfo = (SubjectPublicKeyInfo) pemParser.readObject();
            JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();
            return pemKeyConverter.getPublicKey(subjectPublicKeyInfo);
        }
    }

    public PrivateKey parsePrivateKey(String key) throws Exception {
        try (StringReader reader = new StringReader(key);
             PEMParser pemParser = new PEMParser(reader)) {
            PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
            return new JcaPEMKeyConverter().getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        }
    }

    public Map<String, Key> generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(RSA);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, Key> keyMap = new HashMap(2);
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());
        return keyMap;
    }

    public String pemEncodingKey(Key key) throws Exception {
        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
        pemWriter.writeObject(key);
        pemWriter.close();
        return stringWriter.toString();
    }

    public Map<String, String> generatePemKeyPair(int keySize) throws Exception {
        Map<String, Key> keyMap = generateKeyPair(keySize);
        Map<String,String> result = new HashMap<>(2);
        result.put(PUBLIC_KEY, pemEncodingKey(keyMap.get(PUBLIC_KEY)));
        result.put(PRIVATE_KEY, pemEncodingKey(keyMap.get(PRIVATE_KEY)));
        return result;
    }

    public static void main(String[] args) throws Exception {
        RsaTools rsaTools = new RsaTools();
        Map<String, String> keyPair = rsaTools.generatePemKeyPair(2048);

        String pub = keyPair.get(PUBLIC_KEY);
        String prv  = keyPair.get(PRIVATE_KEY);

        String encrypted = rsaTools.encrypt("Test1234", pub);
        System.out.println(encrypted);
        String decrypt = rsaTools.decrypt(encrypted, prv);
        System.out.println(decrypt);

    }

}