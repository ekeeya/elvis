package io.thothcode.tech.elvis.app.services.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class ReactiveFileUploadServiceImpl implements ReactiveFileUploadService {

    private final Path baseUploadDir;

    public ReactiveFileUploadServiceImpl(@Value("${application.file.upload-dir:${user.dir}/media}")
                                         String uploadDir) {
        try {
            this.baseUploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(this.baseUploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize upload directory: " + uploadDir, e);
        }
    }


    @Override
    public Mono<String> uploadFile(FilePart file, String subfolder) {
        Path uploadPath = baseUploadDir.resolve(subfolder).normalize();

        return Mono.fromCallable(() -> {
                    Files.createDirectories(uploadPath);
                    String originalFilename = file.filename();
                    String fileExtension = originalFilename.substring(
                            originalFilename.lastIndexOf(".") != -1
                                    ? originalFilename.lastIndexOf(".")
                                    : originalFilename.length()
                    );
                    return uploadPath.resolve(UUID.randomUUID() + fileExtension);
                })
                .flatMap(filePath ->
                        file.transferTo(filePath)
                                .then(Mono.just(filePath.toString()))
                )
                .onErrorMap(IOException.class,
                        e -> new RuntimeException("File upload failed: " + e.getMessage())
                )
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteFile(String filePath) {
        return Mono.fromCallable(() -> {
                    Path path = Paths.get(filePath);
                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                    return null;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(IOException.class,
                        e -> new RuntimeException("Error deleting file: " + filePath, e))
                .then();
    }

    private String sanitizeSubfolder(String subfolder) {
        return subfolder.replaceAll("[^a-zA-Z0-9-_]", "");
    }

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".png", ".pdf");

    private Mono<String> validateFile(FilePart file) {
        String ext = file.filename().substring(file.filename().lastIndexOf(".")).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            return Mono.error(new RuntimeException("Invalid file type"));
        }
        // Add size check if FilePart provides size information
        return Mono.just(file.filename());
    }
}
