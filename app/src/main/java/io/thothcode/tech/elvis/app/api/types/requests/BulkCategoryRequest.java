package io.thothcode.tech.elvis.app.api.types.requests;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BulkCategoryRequest implements Serializable {
    private List<CategoryRequest> categories;
}
