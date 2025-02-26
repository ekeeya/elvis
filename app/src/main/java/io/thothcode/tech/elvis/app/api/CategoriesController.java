package io.thothcode.tech.elvis.app.api;

import io.thothcode.tech.elvis.app.api.types.requests.CategoryRequest;
import io.thothcode.tech.elvis.app.services.categories.CategoryService;
import io.thothcode.tech.gluon.Utils;
import io.thothcode.tech.gluon.types.responses.BaseResponseDTO;
import io.thothcode.tech.gluon.types.responses.CategoryResponse;
import io.thothcode.tech.gluon.types.responses.ListResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
// @CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@Slf4j
public class CategoriesController {

    final CategoryService categoryService;

    @PostMapping
    public Mono<ResponseEntity<ListResponseDTO<CategoryResponse>>> createUpdateCategory(
            @RequestPart(value = "id", required = false) String id,
            @RequestPart(value = "name") String name,
            @RequestPart(name = "icon", required = false) Mono<FilePart> icon) {

        return icon
                .switchIfEmpty(Mono.empty())
                .map(filePart -> {
                    CategoryRequest request = new CategoryRequest();
                    request.setId(id);
                    request.setName(name);
                    request.setIcon(filePart);
                    return request;
                })
                .flatMap(categoryService::addCategory)
                .flatMap(categoryEntity ->
                        Mono.just(ResponseEntity.ok(new ListResponseDTO<>(List.of(new CategoryResponse(categoryEntity))))));
    }


    @GetMapping
    public Mono<ResponseEntity<ListResponseDTO<CategoryResponse>>> getCategories() {
       Pageable pageable = Utils.getPageable(0, Integer.MAX_VALUE, "name", "asc");
        return categoryService.getAllCategories(pageable)
                .flatMap(categories-> Mono.just(ResponseEntity.ok(new ListResponseDTO<>(categories))));
    }

    @DeleteMapping("/{categoryId}")
    public Mono<ResponseEntity<BaseResponseDTO>> deleteCategory(
            @PathVariable(name = "categoryId") String categoryId
    ) {
        return categoryService.removeCategory(categoryId)
                .then(Mono.fromCallable(() -> ResponseEntity.ok(new BaseResponseDTO(true, "Category Deleted"))))
                .onErrorResume(throwable -> Mono.just(
                        ResponseEntity.badRequest()
                                .body(new BaseResponseDTO(false, "Error deleting category: " + throwable.getMessage()))
                ));
    }
}
