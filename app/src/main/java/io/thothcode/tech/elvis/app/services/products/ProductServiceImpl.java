package io.thothcode.tech.elvis.app.services.products;

import io.thothcode.tech.elvis.app.api.types.products.ProductRequestDTO;
import io.thothcode.tech.elvis.app.api.types.products.ProductResponseDTO;
import io.thothcode.tech.elvis.app.repositories.categories.CategoryRepository;
import io.thothcode.tech.elvis.app.repositories.products.ProductRepository;
import io.thothcode.tech.elvis.app.repositories.shop.ShopRepository;
import io.thothcode.tech.elvis.app.services.common.ReactiveFileUploadService;
import io.thothcode.tech.gluon.Utils;
import io.thothcode.tech.gluon.entities.CategoryEntity;
import io.thothcode.tech.gluon.entities.ProductEntity;
import io.thothcode.tech.gluon.entities.ShopEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReactiveFileUploadService fileUploadService;
    private final ShopRepository shopRepository;

    @Value("${application.file.upload-dir:${user.dir}/src/main/media}")
    private String uploadDir;

    private String baseUploadDir;

    @PostConstruct
    public void init() {
        baseUploadDir = Paths.get(uploadDir+"/products").toAbsolutePath().normalize().toString();
        log.info("Initialized baseUploadDir: {}", baseUploadDir);
    }

    @Override
    public Mono<ProductEntity> createProduct(ProductRequestDTO request) {
        Mono<ProductEntity> productMono = Mono.empty();
        if (!Utils.isEmpty(request.getId())) {
            productMono = productRepository.findById(request.getId());
        }

        return Mono.zip(
                        categoryRepository.findById(request.getCategoryId())
                                .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("Category ID: " + request.getCategoryId() + " not found"))),
                        shopRepository.findById(request.getShopId())
                                .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("Shop ID: " + request.getShopId() + " not found"))),
                        productMono.switchIfEmpty(Mono.defer(() -> {
                            ProductEntity productEntity = new ProductEntity();
                            return Mono.just(productEntity);
                        }))
                )
                .flatMap(tuple -> {
                    CategoryEntity category = tuple.getT1();
                    ShopEntity shop = tuple.getT2();
                    ProductEntity product = tuple.getT3();
                    product.setFrequency(request.getFrequency());
                    // Set basic product properties
                    product.setPrice(request.getPrice());
                    product.setQuantity(request.getQuantity());
                    product.setName(request.getName());
                    product.setDescription(request.getDescription());
                    product.setSizes(request.getSizes());
                    product.setColors(request.getColors());
                    product.setTags(request.getTags());
                    product.setCategoryId(category.getId());
                    product.setShopId(shop.getId());
                    Flux<FilePart> imageFlux = Flux.fromIterable(request.getImages());
                    return imageFlux
                            .flatMap(filePart -> fileUploadService.uploadFile(filePart, "products"))
                            .collectList()
                            .map(imagePaths -> {
                                product.setImages(imagePaths);
                                return product;
                            })
                            .flatMap(productRepository::save);
                });
    }


    @Override
    public Mono<ProductEntity> addImagesToProduct(String productId, List<FilePart> newImages) {
        if (productId == null || productId.trim().isEmpty()) {
            return Mono.error(() -> new IllegalArgumentException("Product ID cannot be null or empty"));
        }
        if (newImages == null || newImages.isEmpty()) {
            return Mono.error(() -> new IllegalArgumentException("Image list cannot be null or empty"));
        }
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("Product ID: " + productId + " not found")))
                .flatMap(product ->
                        Flux.fromIterable(newImages)
                                .flatMap(filePart -> fileUploadService.uploadFile(filePart, "products"))
                                .collectList()
                                .map(uploadedPaths -> {
                                    List<String> currentImages = product.getImages();
                                    if (currentImages == null) {
                                        currentImages = new ArrayList<>();
                                    }
                                    currentImages.addAll(uploadedPaths);
                                    product.setImages(currentImages);
                                    return product;
                                })
                                .flatMap(productRepository::save)
                );
    }

    @Override
    public Mono<ProductEntity> deleteProductImages(String productId, List<String> imagePathsToDelete) {
        if (productId == null || productId.trim().isEmpty()) {
            return Mono.error(() -> new IllegalArgumentException("Product ID cannot be null or empty"));
        }
        if (imagePathsToDelete == null || imagePathsToDelete.isEmpty()) {
            return Mono.error(() -> new IllegalArgumentException("Image paths list cannot be null or empty"));
        }
        if (imagePathsToDelete.stream().anyMatch(path -> path == null || path.trim().isEmpty())) {
            return Mono.error(() -> new IllegalArgumentException("Image paths list cannot contain null or empty paths"));
        }

        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("Product ID: " + productId + " not found")))
                .flatMap(product -> {
                    List<String> currentImages = product.getImages();

                    if (currentImages == null || currentImages.isEmpty()) {
                        return Mono.error(() -> new IllegalArgumentException("Product has no images to delete"));
                    }
                   List<String> filesToDelete = currentImages.stream().map(this::getFileName).toList();
                    List<String> imagesToRemove = imagePathsToDelete.stream()
                            .filter(filesToDelete::contains)
                            .map(item->Paths.get(baseUploadDir, item).toString())
                            .toList();

                    if (imagesToRemove.isEmpty()) {
                        return Mono.just(product);
                    }
                    currentImages.removeAll(imagesToRemove);
                    product.setImages(currentImages);
                    return Flux.fromIterable(imagesToRemove)
                            .flatMap(fileUploadService::deleteFile)
                            .then(Mono.just(product))
                            .flatMap(productRepository::save)
                            .onErrorMap(throwable ->
                                    new RuntimeException("Failed to delete images for product " + productId, throwable));
                });
    }

    @Override
    public Mono<Void> deleteProduct(String productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")))
                .flatMap(product -> {
                    // delete the images first
                    List<String> images = product.getImages();
                    return Flux.fromIterable(images)
                            .flatMap(fileUploadService::deleteFile)
                            .then(productRepository.delete(product));
                });
    }

    @Override
    public Mono<Page<ProductResponseDTO>> fetchProducts(Pageable pageable, String category) {
        return productRepository.findProductEntitiesByOrderByFrequencyDesc(pageable)
                .collectList()
                .zipWith(productRepository.count())
                .map(tuple-> new PageImpl<>(tuple.getT1().stream().map(ProductResponseDTO::new).toList(), pageable, tuple.getT2()));
    }


    protected String getFileName(String filePath) {
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
