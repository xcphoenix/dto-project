package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.xcphoenix.dto.bean.Province;
import com.xcphoenix.dto.mapper.ProvinceMapper;
import com.xcphoenix.dto.service.Base64ImgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DtoApplicationTests {

    @Autowired
    private Base64ImgService base64Img;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Test
    public void contextLoads() {
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
        if (base64Img.base64TransToFile(base64Str, filepath) != null) {
            System.out.println("success");
        } else {
            System.out.println("error");
        }
    }

    @Test
    public void testProCityCountry() {
        List<Province> provinces = provinceMapper.selectProvinceAll();
        System.out.println(JSON.toJSON(provinces));
    }

    @Test
    public void testFileExist() throws IOException {
        File file = new File("/tmp/root2/root3/root4");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        System.out.println("success");
    }

}
