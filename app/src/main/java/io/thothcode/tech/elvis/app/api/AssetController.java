package io.thothcode.tech.elvis.app.api;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class AssetController {

    private final Path baseUploadDir;

    public AssetController(@Value("${application.file.upload-dir:${user.dir}/src/main/media}") String uploadDir) {
        this.baseUploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @GetMapping("/{filename}")
    public Mono<ResponseEntity<?>> getImage(
            @PathVariable String filename,
            @RequestParam(value = "folder") String folder) {
        return Mono.fromCallable(() -> {
            // Resolve the file path with the folder
            Path filePath = baseUploadDir.resolve(folder).resolve(filename).normalize();

            if (!filePath.startsWith(baseUploadDir)) {
                return ResponseEntity.badRequest().build();
            }
            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(filePath);

            String contentType = determineContentType(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }).onErrorResume(e ->
                Mono.just(ResponseEntity.status(500)
                        .body(null))
        );
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "txt" -> "text/plain";
            default -> "application/octet-stream";
        };
    }
}
