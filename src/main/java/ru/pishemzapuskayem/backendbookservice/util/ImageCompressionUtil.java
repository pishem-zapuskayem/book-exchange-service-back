package ru.pishemzapuskayem.backendbookservice.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageCompressionUtil {

    public static byte[] compressImage(
            MultipartFile sourceImage,
            float compressionQuality,
            String outputFormat
    ) throws IOException {
        if (!sourceImage.getContentType().startsWith("image")) {
            throw new IllegalStateException("Пожалуйста, загрузите изображение.");
        }

        InputStream sourseImageInputStream = sourceImage.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(sourseImageInputStream)
                .scale(1f)
                .outputQuality(compressionQuality)
                .outputFormat(outputFormat)
                .toOutputStream(outputStream);

        byte[] bytes = outputStream.toByteArray();

        outputStream.close();
        sourseImageInputStream.close();

        return bytes;
    }
}
