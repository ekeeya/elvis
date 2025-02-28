package io.thothcode.tech.elvis.app.api.types.products;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

import java.util.List;

@Data
public class ProductRequestDTO {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String categoryName;
   // private FilePart image;
    private String shopId;
    private List<FilePart> images;
    private Double price;
    private List<String> sizes;
    private List<String> tags;
    private List<String> colors;
    private Integer quantity;
    private Integer frequency;
}
