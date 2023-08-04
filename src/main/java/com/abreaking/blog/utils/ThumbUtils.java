package com.abreaking.blog.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 图片压缩工具
 * @author liwei@abreaking
 * @date 2023/8/4
 */
public class ThumbUtils {

    private static final Logger logger = LoggerFactory.getLogger(ThumbUtils.class);

    private static final Integer ZERO = 0;
    private static final Integer ONE_ZERO_TWO_FOUR = 1024;
    private static final Integer NINE_ZERO_ZERO = 900;
    private static final Integer THREE_TWO_SEVEN_FIVE = 3275;
    private static final Integer TWO_ZERO_FOUR_SEVEN = 2047;
    private static final Double ZERO_EIGHT_FIVE = 0.85;
    private static final Double ZERO_SIX = 0.6;
    private static final Double ZERO_FOUR_FOUR = 0.44;
    private static final Double ZERO_FOUR = 0.4;

    public static void compressImgToThumb(byte[] imageBytes,String outputThumbFile) throws IOException {
        byte[] bytes = compressPicForScale(imageBytes, 60);
        File file = new File(outputThumbFile);
        if (file.exists()) file.delete();
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(outputThumbFile);
        fileOutputStream.write(bytes);
    }

    public static void compressImgToThumb(File imageFile,String outputThumbFile) throws IOException {
        byte[] imageBytes = new byte[Math.toIntExact(imageFile.length())];
        FileInputStream inputStream = new FileInputStream(imageFile);
        inputStream.read(imageBytes);
        compressImgToThumb(imageBytes,outputThumbFile);
    }

    public static void compressImgToThumb(MultipartFile imageFile, String outputThumbFile) throws IOException {
        compressImgToThumb(imageFile.getBytes(),outputThumbFile);
    }

    public static String createThumb(String imgPath) {
        String defaultThumb = imgPath;
        String key = "-thumb";
        String uploadFilePath = TaleUtils.getUploadFilePath();
        if (imgPath.startsWith("/")){
            imgPath = uploadFilePath+imgPath;
        }

        File file = new File(imgPath);

        if (!file.exists()) return defaultThumb;

        if (imgPath.indexOf(key)!=-1) return imgPath;

        int i = imgPath.lastIndexOf(".");
        String imgOut = imgPath.substring(0,i)+key+imgPath.substring(i);
        File imgoutFile = new File(imgOut);
        if (imgoutFile.exists()) imgoutFile.delete();
        try {
            ThumbUtils.compressImgToThumb(file,imgOut);
            if (imgOut.startsWith(uploadFilePath)){
                imgOut = imgOut.substring(uploadFilePath.length());
            }
            return imgOut;
        } catch (IOException e) {
            e.printStackTrace();
            return defaultThumb;
        }
    }

    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) {
        if (imageBytes == null || imageBytes.length <= ZERO || imageBytes.length < desFileSize * ONE_ZERO_TWO_FOUR) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / ONE_ZERO_TWO_FOUR);
        try {
            while (imageBytes.length > desFileSize * ONE_ZERO_TWO_FOUR) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            logger.info("图片原大小={}kb | 压缩后大小={}kb",
                    srcSize / ONE_ZERO_TWO_FOUR, imageBytes.length / ONE_ZERO_TWO_FOUR);
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < NINE_ZERO_ZERO) {
            accuracy = ZERO_EIGHT_FIVE;
        } else if (size < TWO_ZERO_FOUR_SEVEN) {
            accuracy = ZERO_SIX;
        } else if (size < THREE_TWO_SEVEN_FIVE) {
            accuracy = ZERO_FOUR_FOUR;
        } else {
            accuracy = ZERO_FOUR;
        }
        return accuracy;
    }

}
