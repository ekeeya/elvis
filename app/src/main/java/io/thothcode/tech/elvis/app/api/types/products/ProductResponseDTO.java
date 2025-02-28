package io.thothcode.tech.elvis.app.api.types.products;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDTO {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String categoryName;
    private String image;
    private String shopName;
    private List<String> images;
    private Double price;
    private List<String> sizes;
    private List<String> tags;
    private List<String> colors;
    private Integer quantity;
    private Integer frequency;
}
