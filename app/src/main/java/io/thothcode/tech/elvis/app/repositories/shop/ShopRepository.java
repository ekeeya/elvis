package io.thothcode.tech.elvis.app.repositories.shop;

import io.thothcode.tech.gluon.entities.ShopEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends ReactiveMongoRepository<ShopEntity, String> {
}
