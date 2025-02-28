package io.thothcode.tech.elvis.app.services.products;

import io.thothcode.tech.elvis.app.api.types.products.ProductRequestDTO;
import io.thothcode.tech.gluon.entities.ProductEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {

    Mono<ProductEntity> createProduct(ProductRequestDTO product);

    Mono<ProductEntity> addImagesToProduct(String productId, List<FilePart> newImages);

    Mono<ProductEntity> deleteProductImages(String productId, List<String> imagePathsToDelete);
}
