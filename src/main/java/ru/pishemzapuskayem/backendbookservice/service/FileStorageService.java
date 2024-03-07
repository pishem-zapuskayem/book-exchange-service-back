package ru.pishemzapuskayem.backendbookservice.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pishemzapuskayem.backendbookservice.util.ImageCompressionUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Paths.get;

@Service
@Slf4j
public class FileStorageService {
    private static final String JPG_FORMAT = "jpg";
    private static final String PNG_FORMAT = "png";

    @Value("${files.storage-directory}")
    private String storageDirectory;

    @Value("${file.images.compression-quality}")
    private float compressionQuality;

    @PostConstruct
    public void initialize() {
        File theDir = new File(storageDirectory);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    public String save(MultipartFile multipartFile) {
        String filename = UUID.randomUUID() + getFileExtension(multipartFile.getOriginalFilename());
        Path path = get(storageDirectory, filename).toAbsolutePath().normalize();

        try (InputStream in = multipartFile.getInputStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return path.toString();
    }

    public String saveAsJPG(MultipartFile multipartFile) {
        return saveImage(multipartFile, JPG_FORMAT);
    }

    public String saveAsPNG(MultipartFile multipartFile) {
        return saveImage(multipartFile, PNG_FORMAT);
    }

    public String saveImage(MultipartFile multipartFile, String outputFormat) {
        String filename = UUID.randomUUID().toString();
        Path path = get(storageDirectory, filename).toAbsolutePath().normalize();

        try {
            byte[] compressed = ImageCompressionUtil.compressImage(multipartFile, compressionQuality, outputFormat);
            Files.write(path, compressed, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException | RuntimeException e) {
            log.error(e.getMessage());
        }

        return path.toString();
    }

    public void deleteFiles(List<String> paths) {
        paths.forEach(this::deleteFile);
    }

    public void deleteFile(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }

        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex > 0 ? fileName.substring(dotIndex) : "";
    }
}
