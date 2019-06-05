package com.swang.jsjavarsademo.rest;

import com.swang.jsjavarsademo.helper.IoTools;
import com.swang.jsjavarsademo.helper.RsaTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author ming.wang4@envision-digital.com
 * @date 2019-06-05
 */
@RestController
@RequestMapping("/keys")
public class KeyController {

    @Autowired
    private RsaTools rsaTools;

    @Autowired
    private IoTools ioTools;

    @GetMapping("/any")
    public KeyBean getKeys(){
        KeyBean keyBean = new KeyBean();
        keyBean.publicKey = ioTools.readResourceAsString("rsa_2048_pub.pem");
        keyBean.keyId = UUID.randomUUID().toString();
        return keyBean;
    }
}
