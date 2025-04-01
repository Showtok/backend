package com.showtok.upload;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    String uploadFile(MultipartFile file, String subDir);
}
