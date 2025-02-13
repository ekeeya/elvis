package io.thothcode.tech.gluon.types.responses;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
public class ListResponseDTO<T> implements Serializable {

    private long totalElements;
    private int totalPages;
    private int pageSize;
    private int page;
    private List<T> entries;

    public ListResponseDTO(Page<T> data) {
        if (data != null) {
            this.entries = data.getContent();
            this.totalElements = data.getTotalElements();
            this.totalPages = data.getTotalPages();
            this.pageSize = data.getNumberOfElements();
        }
    }
}
