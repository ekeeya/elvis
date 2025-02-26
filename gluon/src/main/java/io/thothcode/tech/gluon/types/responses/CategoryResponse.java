package io.thothcode.tech.gluon.types.responses;

import io.thothcode.tech.gluon.entities.CategoryEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryResponse implements Serializable {
    private String id;
    private String name;
    private String iconPath;

    public CategoryResponse(CategoryEntity categoryEntity){
        setId(categoryEntity.getId());
        setName(categoryEntity.getName());
        setIconPath(getIconFileName(categoryEntity.getIcon()));
    }

    public String getIconFileName(String iconPath) {
        int lastSlashIndex = iconPath.lastIndexOf("/");
        if (lastSlashIndex != -1 && lastSlashIndex < iconPath.length() - 1) {
            return iconPath.substring(lastSlashIndex + 1);
        }
        return iconPath;
    }
}
