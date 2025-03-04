package io.thothcode.tech.elvis.app.services.products;

import io.thothcode.tech.elvis.app.api.types.products.ProductRequestDTO;
import io.thothcode.tech.elvis.app.api.types.products.ProductResponseDTO;
import io.thothcode.tech.gluon.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {

    Mono<ProductEntity> createProduct(ProductRequestDTO product);

    Mono<Page<ProductResponseDTO>> fetchProducts(Pageable pageable, String category);

    Mono<ProductEntity> addImagesToProduct(String productId, List<FilePart> newImages);

    Mono<ProductEntity> deleteProductImages(String productId, List<String> imagePathsToDelete);

    Mono<Void> deleteProduct(String productId);
}
