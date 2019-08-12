package com.xcphoenix.dto.service;

import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base64 url 工具类，解码生成图片，设置普通大小和宽度
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 上午8:53
 */
@Component
public class Base64ImgService {

    /**
     * 允许的图片格式
     */
    @Value("${upload.picture.type:jpg,png,jpeg}")
    private String imgType;

    @Value("${upload.picture.size.deviation:1.0}")
    private double deviation;

    @Value("${upload.picture.size.limit:1.0}")
    private double imageSizeLimit;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * base64 转文件
     *
     * @param base64Str base64 编码字符串
     * @param filepath  文件路径
     * @return 是否成功
     */
    public boolean base64TransToFile(String base64Str, String filepath) throws IOException {

        if (base64Str == null || filepath == null) {
            return false;
        }

        // 去掉图片前缀，转码
        base64Str = replacePre(base64Str);
        Base64 base64 = new Base64(true);
        byte[] baseImg = base64.decode(base64Str);

        // 判断文件大小是否符合要求
        if (!checkSize(baseImg)) {
            throw new ServiceLogicException(ErrorCode.FILE_SIZE_OUT_OF_RANGE);
        }

        // 获取图片后缀
        String suffix = getImageSuffix(baseImg);
        if (suffix == null) {
            throw new ServiceLogicException(ErrorCode.FILE_NOT_IMAGE);
        }

        filepath += "." + suffix;
        Files.write(Paths.get(filepath), baseImg, StandardOpenOption.CREATE);

        return true;
    }

    private String getImageSuffix(byte[] baseImg) throws IOException {

        // 不带类似 data:image/jpg;base64, 前缀的解析
        ImageInputStream imageInputstream = new MemoryCacheImageInputStream(new ByteArrayInputStream(
                baseImg));

        // 不使用磁盘缓存
        ImageIO.setUseCache(false);
        Iterator<ImageReader> it = ImageIO.getImageReaders(imageInputstream);
        if (it.hasNext()) {
            ImageReader imageReader = it.next();
            // 设置解码器的输入流
            imageReader.setInput(imageInputstream, true, true);

            // 图像文件格式后缀
            String suffix = imageReader.getFormatName().trim().toLowerCase();
            int height = imageReader.getHeight(0);
            int width = imageReader.getWidth(0);
            imageInputstream.close();

            logger.info("picture width: " + width);
            logger.info("picture height: " + height);

            String[] imgTypes = imgType.split(",");
            for (String type : imgTypes) {
                if (type.equalsIgnoreCase(suffix)) {
                    return type;
                }
            }
        }

        return null;
    }

    /**
     * 去掉 base64 图片前缀
     */
    private String replacePre(String imgString) {
        // 允许的图片格式（可配置）
        String imgType = "jpg,png,jpeg";
        if (!StringUtils.isEmpty(imgType)) {
            String[] imgTypes = imgType.split(",");
            Pattern pattern;
            Matcher matcher;
            String regex;
            for (String v : imgTypes) {
                regex = MessageFormat.format("data:image/{0};base64,", v);
                pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(imgString);
                if (matcher.lookingAt()) {
                    return matcher.replaceFirst("");
                }
            }
        }
        return imgString;
    }

    private boolean checkSize(byte[] bytes) {
        // 图片转base64字符串一般会大，这个变量就是设置偏移量。可配置在文件中，随时修改。目前配的是0。后续看情况适当做修改
        int length = bytes.length;
        double size = (double) length / 1024 / 1024 * (1 - deviation);

        logger.info("picture size = " + size + "MB");

        return size <= imageSizeLimit;
    }

}
