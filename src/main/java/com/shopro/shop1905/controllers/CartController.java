package com.shopro.shop1905.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.CartAddProductDTO;
import com.shopro.shop1905.dtos.dtosRes.ApiMetaRes;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.CartProductSizeColorDTO;
import com.shopro.shop1905.dtos.dtosRes.MetadataDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductDTO;
import com.shopro.shop1905.services.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cart")
public class CartController {
        private final CartService cartService;

        @PostMapping("/addProduct")
        public ApiRes<Void> addProductToCart(@RequestBody CartAddProductDTO cartAddProductDTO) {
                return ApiRes.<Void>builder().code(1000).message("add product to cart success")
                                .result(cartService.addProductToCart(cartAddProductDTO))
                                .build();
        }

        @DeleteMapping("/{id}")
        public ApiRes<Void> removeProductFromCart(@PathVariable("id") long cartProductSizeColorId) {
                return ApiRes.<Void>builder().code(1000).message("remove product from cart success")
                                .result(cartService.removeProductFromCart(cartProductSizeColorId))
                                .build();
        }

        @GetMapping("/allProduct")
        public ApiMetaRes<List<CartProductSizeColorDTO>> getAllProductOfCart(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size) {
                Page<CartProductSizeColorDTO> productPage = cartService.getAllProductOfCart(page, size);
                MetadataDTO metadata = new MetadataDTO(
                                productPage.getTotalElements(),
                                productPage.getTotalPages(),
                                productPage.getNumber(),
                                productPage.getSize());
                return ApiMetaRes.<List<CartProductSizeColorDTO>>builder().code(1000)
                                .message("get all product of cart success")
                                .result(productPage.getContent()).metadata(metadata)
                                .build();
        }

}
