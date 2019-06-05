package com.swang.jsjavarsademo.helper;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.StringReader;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Component
public class RsaTools {

    private final String CHAR_ENCODING = "UTF-8";

    private final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";

    public String encrypt(String source, String publicKey) throws Exception {
        Key key = readPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        byte[] b1 = cipher.doFinal(b);
        return new String(Base64.getEncoder().encode(b1),
                CHAR_ENCODING);
    }

    public String decrypt(String cryptograph, String privateKey) throws Exception {
        Key key = readPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.getDecoder().decode(cryptograph.getBytes());
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    public PublicKey readPublicKey(String key) throws Exception {
        try (StringReader reader = new StringReader(key);
             PEMParser pemParser = new PEMParser(reader)) {
            SubjectPublicKeyInfo subjectPublicKeyInfo = (SubjectPublicKeyInfo) pemParser.readObject();
            JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();
            return pemKeyConverter.getPublicKey(subjectPublicKeyInfo);
        }
    }

    public PrivateKey readPrivateKey(String key) throws Exception {
        try (StringReader reader = new StringReader(key);
             PEMParser pemParser = new PEMParser(reader)) {
            PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
            return new JcaPEMKeyConverter().getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        }
    }
}