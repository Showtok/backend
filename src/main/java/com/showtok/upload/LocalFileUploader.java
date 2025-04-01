package com.showtok.upload;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class LocalFileUploader implements FileUploader {

    private final String baseDir = System.getProperty("user.dir") + "/uploads";

    @Override
    public String uploadFile(MultipartFile file, String subDir) {
        try {
            // ✅ 하위 디렉토리 경로 조합
            File dir = new File(baseDir + "/" + subDir);
            if (!dir.exists()) {
                dir.mkdirs(); // 디렉토리 생성
            }

            String originalFilename = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID() + "_" + originalFilename;

            File destination = new File(dir, storedFileName);
            file.transferTo(destination);

            return "/uploads/" + subDir + "/" + storedFileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
}
