package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.pishemzapuskayem.backendbookservice.model.entity.FileAttachment;
import ru.pishemzapuskayem.backendbookservice.repository.FileAttachmentRepository;
import ru.pishemzapuskayem.backendbookservice.util.PathUtil;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileAttachmentService {
    private final FileStorageService fileStorageService;
    private final FileAttachmentRepository repository;

    @Value("${urls.me}")
    private String backendUrl;

    @Transactional
    public void delete(FileAttachment fileAttachment) {
        fileStorageService.deleteFile(fileAttachment.getPath());
        repository.delete(fileAttachment);
    }

    public List<FileAttachment> saveImagesAsJPG(List<MultipartFile> files) {
        List<FileAttachment> fileAttachments = new ArrayList<>();
        for (MultipartFile file: files) {
            fileAttachments.add(saveAsJPG(file));
        }

        return fileAttachments;
    }

    public FileAttachment saveAsPNG(MultipartFile file) {
        String path = fileStorageService.saveAsPNG(file);
        return new FileAttachment(path, PathUtil.pathToURL(backendUrl, path));
    }

    public FileAttachment saveAsJPG(MultipartFile file) {
        String path = fileStorageService.saveAsJPG(file);
        return new FileAttachment(path, PathUtil.pathToURL(backendUrl, path));
    }

    @Transactional
    public FileAttachment saveFile(MultipartFile file) {
        String path = fileStorageService.save(file);
        return repository.save(new FileAttachment(path, PathUtil.pathToURL(backendUrl, path)));
    }
}
