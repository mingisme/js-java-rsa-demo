package com.swang.jsjavarsademo.rest;

import com.swang.jsjavarsademo.helper.IoTools;
import com.swang.jsjavarsademo.helper.RsaTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ming.wang4@envision-digital.com
 * @date 2019-06-05
 */
@RestController
@RequestMapping("/data")
public class SubmitController {

    @Autowired
    private RsaTools rsaTools;

    @Autowired
    private IoTools ioTools;

    @PostMapping("/commit")
    public Result createData(@RequestBody DataBean data) {

        String privKeyString = ioTools.readResourceAsString("rsa_2048_priv.pem");

        try {
            String decryptText = rsaTools.decrypt(data.encryptedText, privKeyString);
            if (!data.plainText.equals(decryptText)) {
                throw new RuntimeException("decrypt failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Result result = new Result();
            result.code = "1";
            result.message = "failedxx";
            return result;
        }

        Result result = new Result();
        result.code = "0";
        result.message = "successxx";
        return result;
    }
}
