package io.thothcode.tech.elvis.app.api;


import io.thothcode.tech.elvis.app.api.types.shops.ShopDTO;
import io.thothcode.tech.elvis.app.services.shops.ShopService;
import io.thothcode.tech.gluon.Utils;
import io.thothcode.tech.gluon.types.responses.ListResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Slf4j

public class ShopController {

    private final ShopService shopService;

        @PostMapping
        public Mono<ResponseEntity<ShopDTO>> createShop(@RequestBody ShopDTO shopDTO) {
            return shopService.saveUpdateShop(shopDTO)
                    .flatMap(shop -> Mono.just(ResponseEntity.ok(new ShopDTO(shop))))
                    .doOnError(r -> log.error("Error creating shop", r));
        }

        @GetMapping
        public Mono<ResponseEntity<ListResponseDTO<ShopDTO>>> getAllShops() {
            Pageable pageable = Utils.getPageable(0, Integer.MAX_VALUE, "id", "desc");
            return shopService.getAllShops(pageable)
                    .flatMap(shops -> Mono.just(ResponseEntity.ok(new ListResponseDTO<>(shops))));
        }


}
