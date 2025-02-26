package io.thothcode.tech.elvis.app.repositories.categories;


import io.thothcode.tech.gluon.entities.CategoryEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveMongoRepository<CategoryEntity, String> {

    Mono<CategoryEntity> findCategoryEntityByNameIgnoreCase(String name);
}
