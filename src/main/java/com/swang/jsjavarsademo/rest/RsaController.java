package com.swang.jsjavarsademo.rest;

import com.swang.jsjavarsademo.helper.IoTools;
import com.swang.jsjavarsademo.helper.RsaTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/data")
public class RsaController {

    @Autowired
    private RsaTools rsaTools;

    @Autowired
    private IoTools ioTools;

    @PostMapping("/rsa")
    public RsaResult createData(@RequestBody DataBean data) {

        String privKeyString = ioTools.readResourceAsString("rsa_2048_priv.pem");

        String encryptedResult = null;
        try {
            String decryptText = rsaTools.decrypt(data.encryptedText, privKeyString);
            if (!data.plainText.equals(decryptText)) {
                throw new RuntimeException("decrypt failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            RsaResult result = new RsaResult();
            result.code = "1";
            result.message = "failedxxx"+new Date();
            return result;
        }

        RsaResult result = new RsaResult();
        result.code = "0";
        result.message = "successxx"+new Date();
        return result;
    }
}
