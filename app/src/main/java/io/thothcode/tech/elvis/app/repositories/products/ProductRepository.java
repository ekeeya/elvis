package io.thothcode.tech.elvis.app.repositories.products;

import io.thothcode.tech.gluon.entities.ProductEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends ReactiveMongoRepository<ProductEntity, String> {


}
