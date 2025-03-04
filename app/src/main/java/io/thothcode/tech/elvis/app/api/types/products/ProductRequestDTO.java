package io.thothcode.tech.elvis.app.api.types.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String shopId;
    private List<FilePart> images;
    private Double price;
    private List<String> sizes;
    private List<String> tags;
    private List<String> colors;
    private Integer quantity;
    private Integer frequency;

    public void setSizes(String sizes) {
        this.sizes = List.of(sizes.split(","));
    }

    public void setTags(String tags) {
        this.tags = List.of(tags.split(","));
    }

    public void setColors(String colors) {
        this.colors = List.of(colors.split(","));
    }
}
