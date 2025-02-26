package services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {

    String uploadFile(MultipartFile file, String subfolder) throws IOException;
    void deleteFile(String filePath) throws IOException;

}
