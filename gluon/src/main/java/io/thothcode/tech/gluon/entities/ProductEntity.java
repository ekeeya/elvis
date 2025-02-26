package io.thothcode.tech.gluon.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@Document(collection = "product")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEntity extends BaseEntity {

    private String name;
    private String description;
    @DBRef
    private CategoryEntity category;

    @DBRef
    private ShopEntity shop;

    private List<String> images;
    private Double price;
    private List<String> sizes;
    private List<String> tags;
    private List<String> colors;
    private Integer quantity;
    private Integer frequency;
}
