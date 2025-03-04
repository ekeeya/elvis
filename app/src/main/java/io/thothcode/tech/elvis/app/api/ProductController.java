package io.thothcode.tech.elvis.app.api;

import io.thothcode.tech.elvis.app.api.types.products.ProductRequestDTO;
import io.thothcode.tech.elvis.app.api.types.products.ProductResponseDTO;
import io.thothcode.tech.elvis.app.services.products.ProductService;
import io.thothcode.tech.gluon.types.responses.BaseResponseDTO;
import io.thothcode.tech.gluon.types.responses.ListResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
// @CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    final ProductService productService;

    @PostMapping
    public Mono<ResponseEntity<ProductResponseDTO>> addProduct(
            @RequestPart(value = "id", required = false) String id,
            @RequestPart(value = "name") String name,
            @RequestPart(value = "description") String description,
            @RequestPart(value = "categoryId", required = false) String categoryId,
            @RequestPart(value = "shopId", required = false) String shopId,
            @RequestPart(value = "price", required = false) String priceStr, // Changed to String
            @RequestPart(value = "sizes") String sizes, // comma separated
            @RequestPart(value = "colors") String colors,
            @RequestPart(value = "tags") String tags,
            @RequestPart(value = "quantity") String quantityStr, // Changed to String
            @RequestPart(name = "images", required = false) Flux<FilePart> images
    ) {
        return Mono.just(new ProductRequestDTO())
                .map(request -> {
                    request.setId(id);
                    request.setName(name);
                    request.setDescription(description);
                    request.setCategoryId(categoryId);
                    request.setShopId(shopId);
                    request.setPrice(priceStr != null && !priceStr.isBlank() ? Double.parseDouble(priceStr) : 0.0);
                    request.setQuantity(quantityStr != null && !quantityStr.isBlank() ? Integer.parseInt(quantityStr) : 0);
                    request.setFrequency(0);
                    request.setTags(tags);
                    request.setColors(colors);
                    request.setSizes(sizes);
                    return request;
                })
                .flatMap(request ->
                        images.collectList()
                                .map(imageList -> {
                                    request.setImages(imageList);
                                    return request;
                                })
                )
                .flatMap(productService::createProduct)
                .map(product -> ResponseEntity.ok(new ProductResponseDTO(product)))
                .onErrorMap(NumberFormatException.class, e ->
                        new IllegalArgumentException("Invalid number format for price, quantity, or frequency: " + e.getMessage()));
    }


    @PostMapping("/images/{productId}/add")
    public Mono<ResponseEntity<ProductResponseDTO>> addImages(
            @PathVariable(value = "productId", required = false) String productId,
            @RequestPart(name = "images", required = false) Flux<FilePart> images
    ) {
        return Mono.just(new ArrayList<FilePart>())
                .map(newImages -> newImages)
                .flatMap(newImages ->
                        images.collectList()
                                .map(imageList -> {
                                    newImages.addAll(imageList);
                                    return newImages;
                                })
                )
                .flatMap(pics -> productService.addImagesToProduct(productId, pics))
                .map(product -> ResponseEntity.ok(new ProductResponseDTO(product)))
                .onErrorMap(NumberFormatException.class, e ->
                        new IllegalArgumentException("Error occurred when adding images to product: " + e.getMessage()));
    }


    @PostMapping("/images/{productId}/remove")
    public Mono<ResponseEntity<ProductResponseDTO>> removeImages(
            @PathVariable(value = "productId") String productId,
            @RequestPart(name = "images") Flux<String> imageFiles
    ) {
        return Mono.just(new ArrayList<String>())
                .map(imagesToDelete->imagesToDelete)
                .flatMap(images->
                        imageFiles.collectList()
                                .map(imageList-> {images.addAll(imageList);
                                    return images;
                                })
                ).flatMap(pics ->productService.deleteProductImages(productId, pics))
                .map(product -> ResponseEntity.ok(new ProductResponseDTO(product)))
                .onErrorMap(NumberFormatException.class, e ->
                        new IllegalArgumentException("Error occurred when removing images from product: " + e.getMessage()));
    }


    @GetMapping
    public Mono<ResponseEntity<ListResponseDTO<ProductResponseDTO>>> getProducts(
            Pageable pageable,
            @RequestParam(name = "category", required = false) String categoryId
    ){
        return productService.fetchProducts(pageable, categoryId)
                .flatMap(product->Mono.just(ResponseEntity.ok(new ListResponseDTO<>(product))));
    }


    @DeleteMapping("/{productId}")
    public Mono<ResponseEntity<BaseResponseDTO>> deleteProduct(
            @PathVariable(name = "productId") String productId
    ) {
        return productService.deleteProduct(productId)
                .then(Mono.fromCallable(() -> ResponseEntity.ok(new BaseResponseDTO(true, "product Deleted"))))
                .onErrorResume(throwable -> Mono.just(
                        ResponseEntity.badRequest()
                                .body(new BaseResponseDTO(false, "Error deleting product: " + throwable.getMessage()))
                ));
    }
}
