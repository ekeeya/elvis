package io.thothcode.tech.elvis.app.api.types.requests;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

@Data
public class CategoryRequest {
    private String id;// for updating
    private String name;
    private FilePart icon;
}
