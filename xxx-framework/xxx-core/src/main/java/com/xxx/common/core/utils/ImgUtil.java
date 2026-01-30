package com.xxx.common.core.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

public class ImgUtil {

    public static final String FILENAME_EXTENSION_PNG = "png";
    public static final String FILENAME_EXTENSION_JPG = "jpg";
    public static final String FILENAME_EXTENSION_JPEG = "jpeg";

    public static boolean verifyFilenameExtension(MultipartFile file, String... allowedExtensions) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }
        String originalExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        for (String ext : allowedExtensions) {
            if (originalExtension.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    public static MultipartFile zipImg(MultipartFile file) {
        return zipImg(file, 200 * 1024);
    }

    /**
     * 压缩图片，不修改尺寸
     * 1、接收 file 源文件，并判断是否为空
     * 2、不修改原尺寸，按比例压缩图片
     * 3、将压缩后的图片封装为 MultipartFile 类型返回
     *
     * @param file
     * @return
     */
    public static MultipartFile zipImg(MultipartFile file, long size) {
        // 判空，并且大于200kb再压缩
        if (file == null || file.getSize() <= size) {
            return file;
        }
        // 字节文件输出流，保存转换后的图片数据流
        ByteArrayOutputStream outputStream = null;
        // 通过输入流转换为 MultipartFile
        ByteArrayInputStream inputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .scale(0.3f) //按比例放大缩小 和size() 必须使用一个 不然会报错
                    .outputQuality(0.5f) //输出的图片质量  0~1 之间,否则报错
                    .toOutputStream(outputStream); //图片输出位置
            // 将 outputStream 转换为 MultipartFile
            byte[] bytes = outputStream.toByteArray();
            inputStream = new ByteArrayInputStream(bytes);
            // 创建 MockMultipartFile 对象，该类在【spring-test】依赖中
            // 返回图片
            return new MockMultipartFile(Objects.requireNonNull(file.getOriginalFilename()), file.getOriginalFilename(), file.getContentType(), inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public static String multipartFileConvertBase64(MultipartFile file) {
        // 通过base64来转化图片
        String base64 = null;
        if (null != file && ObjectUtils.isNotEmpty(file.getOriginalFilename())) {
            try {
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null) {
                    return null;
                }
                originalFilename = originalFilename.trim();
                String[] type = originalFilename.split("\\.");
                String imageOne = "image/" + type[1];
                base64 = "data:" + imageOne + ";base64," + Base64.getEncoder().encodeToString(file.getBytes());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return base64;
    }

}