package io.thothcode.tech.elvis.app.repositories.products;

import io.thothcode.tech.gluon.entities.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<ProductEntity, String> {

    Flux<ProductEntity> findProductEntitiesByOrderByFrequencyDesc(Pageable pageable);

    Flux<ProductEntity> findProductEntitiesByCategoryId(String categoryId, Pageable pageable);

    Flux<ProductEntity> findProductEntitiesBy(Pageable pageable);
}
