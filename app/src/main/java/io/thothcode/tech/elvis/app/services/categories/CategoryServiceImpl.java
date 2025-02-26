package io.thothcode.tech.elvis.app.services.categories;


import io.thothcode.tech.elvis.app.api.types.requests.BulkCategoryRequest;
import io.thothcode.tech.elvis.app.api.types.requests.CategoryRequest;
import io.thothcode.tech.elvis.app.repositories.categories.CategoryRepository;
import io.thothcode.tech.elvis.app.services.common.ReactiveFileUploadService;
import io.thothcode.tech.gluon.Utils;
import io.thothcode.tech.gluon.entities.CategoryEntity;
import io.thothcode.tech.gluon.types.responses.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;


@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ReactiveFileUploadService fileUploadService;

    @Override
    public Mono<CategoryEntity> addCategory(CategoryRequest request) {
        // Step 1: Find the category by ID or name
        Mono<CategoryEntity> categoryMono = request.getId() != null
                ? categoryRepository.findById(request.getId())
                : categoryRepository.findCategoryEntityByNameIgnoreCase(request.getName());

        return categoryMono
                .switchIfEmpty(Mono.defer(() -> {
                    // Step 2: If the category doesn't exist, create a new one
                    CategoryEntity newCategory = new CategoryEntity();
                    newCategory.setName(request.getName());
                    return Mono.just(newCategory);
                }))
                .flatMap(categoryEntity -> {
                    // Step 3: Delete the old icon file if it exists
                    Mono<Void> deleteOldFileMono = (categoryEntity.getId() != null && categoryEntity.getIcon() != null)
                            ? fileUploadService.deleteFile(categoryEntity.getIcon())
                            : Mono.empty();

                    return deleteOldFileMono
                            .then(fileUploadService.uploadFile(request.getIcon(), "categories")) // Step 4: Upload the new icon file
                            .flatMap(filePath -> {
                                // Step 5: Update the category entity with the new icon and save it
                                categoryEntity.setName(request.getName());
                                categoryEntity.setIcon(filePath);
                                return categoryRepository.save(categoryEntity);
                            });
                })
                .onErrorMap(IOException.class, e -> new RuntimeException("File operation failed: " + e.getMessage()));
    }

    @Override
    public Flux<CategoryEntity> bulkAddCategories(BulkCategoryRequest request) {

        if(Utils.isEmpty(request) || Utils.isEmpty(request.getCategories())){
            return  Flux.error(new IllegalArgumentException("Bad parameter: request ot categories list cannot be empty"));
        }

        return Flux.fromIterable(request.getCategories())
                .flatMap(this::addCategory);
    }

    @Override
    public Mono<Void> removeCategory(String id) {

        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Category not found")))
                .flatMap(categoryRepository::delete);
    }

    @Override
    public Mono<Page<CategoryResponse>> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll().collectList()
                .zipWith(categoryRepository.count())
                .map(tuple-> new PageImpl<>(tuple.getT1().stream()
                        .map(CategoryResponse::new).toList(), pageable, tuple.getT2()));
    }
}
