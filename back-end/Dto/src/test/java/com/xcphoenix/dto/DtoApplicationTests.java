package com.xcphoenix.dto;

import com.xcphoenix.dto.util.Base64Img;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DtoApplicationTests {

    @Autowired
    private UploadImageService uploadImageService;

    @Autowired
    private Base64Img base64Img;

    @Test
    public void contextLoads() {
        System.out.println(uploadImageService.getImageUrl());
    }

    @Test
    public void testBase64() throws IOException {
        File file = new File("src/test/resources/base64-test.txt");
        FileInputStream fi = new FileInputStream(file);
        InputStreamReader streamReader = new InputStreamReader(fi);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        fi.close();

        String base64Str = String.valueOf(stringBuilder);

        String filepath = UUID.randomUUID().toString();
        if (base64Img.base64TransToFile(base64Str, filepath)) {
            System.out.println("success");
        } else {
            System.out.println("error");
        }
    }

}
