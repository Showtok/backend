package com.showtok.service;

import com.showtok.upload.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileUploader fileUploader;

    public String uploadImage(MultipartFile file) {
        return fileUploader.uploadFile(file, "images");
    }
}
