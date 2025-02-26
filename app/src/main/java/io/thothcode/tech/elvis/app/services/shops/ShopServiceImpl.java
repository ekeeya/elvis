package io.thothcode.tech.elvis.app.services.shops;

import io.thothcode.tech.elvis.app.api.types.shops.ShopDTO;
import io.thothcode.tech.elvis.app.repositories.shop.ShopRepository;
import io.thothcode.tech.gluon.entities.ShopEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;


    @Override
    public Mono<ShopEntity> saveUpdateShop(ShopDTO request) {
        Mono<ShopEntity> shopEntityMono =  Mono.empty();

        if(request.getId() != null) {
            shopEntityMono = shopRepository.findById(request.getId());
        }
        return shopEntityMono
                .switchIfEmpty(Mono.defer(()->{
                    ShopEntity shopEntity  = new ShopEntity();
                    shopEntity.setName(request.getName());
                    shopEntity.setDescription(request.getDescription());
                    return Mono.just(shopEntity);
                }))
                .flatMap(shopEntity1 -> {
                    shopEntity1.setName(request.getName());
                    shopEntity1.setDescription(request.getDescription());
                    shopEntity1.setEnabled(true);
                    return shopRepository.save(shopEntity1);
                })
                .onErrorMap(IOException.class, e -> new RuntimeException("Shop Creation failed: " + e.getMessage()));
    }

    @Override
    public Mono<Page<ShopDTO>> getAllShops(Pageable pageable) {
        return shopRepository.findAll()
                .collectList()
                .zipWith(shopRepository.count())
                .map(tuple-> new PageImpl<>(tuple.getT1().stream()
                        .map(ShopDTO::new).toList(), pageable, tuple.getT2()));
    }

    @Override
    public Mono<Void> removeShop(String id) {
        return shopRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Shop not found")))
                .flatMap(shopRepository::delete);
    }
}
