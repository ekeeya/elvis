package io.thothcode.tech.elvis.app.api.types.products;

import io.thothcode.tech.gluon.entities.ProductEntity;
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

    public ProductResponseDTO(ProductEntity product){
        setId(product.getId());
        setName(product.getName());
        setDescription(product.getDescription());
        setCategoryId(product.getCategoryId());
        setPrice(product.getPrice());
        setSizes(product.getSizes());
        setTags(product.getTags());
        setColors(product.getColors());
        setQuantity(product.getQuantity());
        setFrequency(product.getFrequency());
        setImages(product.getImages().stream().map(this::getFileName).toList());
        if(!product.getImages().isEmpty()){
            setImage(getFileName(product.getImages().getFirst()));
        }
    }

    public String getFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        int lastSlashIndex = filePath.lastIndexOf("/");
        if (lastSlashIndex == -1) {
            return filePath;
        }
        return filePath.substring(lastSlashIndex + 1);
    }
}
