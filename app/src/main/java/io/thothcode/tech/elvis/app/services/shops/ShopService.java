package io.thothcode.tech.elvis.app.services.shops;

import io.thothcode.tech.elvis.app.api.types.shops.ShopDTO;
import io.thothcode.tech.gluon.entities.ShopEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface ShopService {

    Mono<ShopEntity> saveUpdateShop(ShopDTO shop);

    Mono<Page<ShopDTO>> getAllShops(Pageable pageable);

    Mono<Void> removeShop(String id);

}
