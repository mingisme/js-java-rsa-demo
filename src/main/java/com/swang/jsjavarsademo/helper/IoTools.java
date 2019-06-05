package com.swang.jsjavarsademo.helper;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ming.wang4@envision-digital.com
 * @date 2019-06-05
 */
@Component
public class IoTools {

    public String readResourceAsString(String resourceName) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             InputStream resourceAsStream = IoTools.class.getClassLoader().getResourceAsStream(resourceName)) {
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = resourceAsStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
