package com.swang.jsjavarsademo.helper;

import org.junit.Test;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class RsaToolsTest {

    private RsaTools rsaTools = new RsaTools();


    @Test
    public void writePrivateKeyAndPublicKey() throws Exception {
        Map<String, Key> keyMap = rsaTools.generateKeyPair(2048);
        PrivateKey privateKey = (PrivateKey) keyMap.get(RsaTools.PRIVATE_KEY);
        String privateKeyPem = rsaTools.pemEncodingKey(privateKey);
        System.out.println(privateKeyPem);

        PublicKey publicKey = (PublicKey) keyMap.get(RsaTools.PUBLIC_KEY);
        String publicKeyPem = rsaTools.pemEncodingKey(publicKey);
        System.out.println(publicKeyPem);

        String randomStr = UUID.randomUUID().toString();
        String encrypt = rsaTools.encrypt(randomStr, publicKeyPem);
        String decrypt = rsaTools.decrypt(encrypt, privateKeyPem);

        assertEquals(randomStr,decrypt);

    }

    @Test
    public void testKeyGeneratePair()throws  Exception{
        Map<String, String> keyMap = rsaTools.generatePemKeyPair(2048);

        String privateKeyPem = keyMap.get(RsaTools.PRIVATE_KEY);
        System.out.println(privateKeyPem);
        String publicKeyPem = keyMap.get(RsaTools.PUBLIC_KEY);
        System.out.println(publicKeyPem);

        String randomStr = UUID.randomUUID().toString();
        String encrypt = rsaTools.encrypt(randomStr, publicKeyPem);
        String decrypt = rsaTools.decrypt(encrypt, privateKeyPem);

        assertEquals(randomStr,decrypt);
    }
}