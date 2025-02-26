package io.thothcode.tech.elvis.app.services.common;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface ReactiveFileUploadService {

    Mono<String> uploadFile(FilePart file, String subfolder);
    Mono<Void> deleteFile(String filePath);
}
