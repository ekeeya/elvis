package io.thothcode.tech.elvis.app.api.types.shops;


import io.thothcode.tech.gluon.entities.ShopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {
    private String id;
    private String name;
    private String description;

    public ShopDTO(ShopEntity shopEntity) {
        setId(shopEntity.getId());
        setName(shopEntity.getName());
        setDescription(shopEntity.getDescription());
    }
}
