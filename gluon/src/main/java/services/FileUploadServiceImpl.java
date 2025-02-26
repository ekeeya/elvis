package services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${application.file.upload-dir:media}")
    private String baseUploadDir;

    /**
     * Uploads a file to a specified subfolder and returns the saved file path.
     *
     * @param file      The MultipartFile to upload
     * @param subfolder The subfolder under the base directory (e.g., "categories")
     * @return The saved file path as a String
     * @throws IOException If file operations fail
     */
    public String uploadFile(MultipartFile file, String subfolder) throws IOException {
        Path uploadPath = Paths.get(baseUploadDir, subfolder).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath); // Ensure directory exists

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String uniqueFilename = UUID.randomUUID() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFilename);

        file.transferTo(filePath);
        return filePath.toString();
    }

    /**
     * Deletes a file if it exists.
     *
     * @param filePath The path to the file to delete
     * @throws IOException If deletion fails
     */
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }
}
