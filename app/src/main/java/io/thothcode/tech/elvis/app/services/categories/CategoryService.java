package io.thothcode.tech.elvis.app.services.categories;

import io.thothcode.tech.elvis.app.api.types.requests.BulkCategoryRequest;
import io.thothcode.tech.elvis.app.api.types.requests.CategoryRequest;
import io.thothcode.tech.gluon.entities.CategoryEntity;
import io.thothcode.tech.gluon.types.responses.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {

    Mono<CategoryEntity> addCategory(CategoryRequest request);

    Flux<CategoryEntity> bulkAddCategories(BulkCategoryRequest request);

    Mono<Void> removeCategory(String id);

    Mono<Page<CategoryResponse>> getAllCategories(Pageable pageable);
}
