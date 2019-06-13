package com.swang.jsjavarsademo.rest;

import com.swang.jsjavarsademo.helper.AesTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/data")
public class AesController {


    @Autowired
    private AesTools aesTools;

    @PostMapping("/aes")
    public AesResult createData(@RequestBody DataBean data) {

        String key = data.plainText;

        try {
            String iv = randomStr(16);

            String secret = aesTools.encrypt("This is a secret" + new Date(), key, iv);

            System.out.println(secret);

            String decrypted = aesTools.decrypt(secret, key, iv);

            System.out.println(decrypted);

            AesResult result = new AesResult();
            result.code = "0";
            result.message = secret;
            result.iv = iv;
            return result;
        } catch (Exception e) {
            AesResult result = new AesResult();
            result.code = "1";
            result.message = e.getMessage();
            return result;
        }
    }

    public static String randomStr(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
