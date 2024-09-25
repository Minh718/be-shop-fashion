package com.shopro.shop1905.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.ColorRes;
import com.shopro.shop1905.dtos.dtosRes.ProductColorsAdmin;
import com.shopro.shop1905.entities.Color;
import com.shopro.shop1905.services.ColorService;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/color")
public class ColorController {
    private final ColorService colorService;

    public ApiRes<Void> addColor(@RequestBody Color color) {
        colorService.addColor(color);
        return ApiRes.<Void>builder().code(1003).message("delete cactegory is success")
                .build();
    }

    @GetMapping("/{idProductSize}")
    public ApiRes<List<ColorRes>> findAllColorNotInProductSize(@PathVariable Long idProductSize) {
        return ApiRes.<List<ColorRes>>builder().code(1003).message("get colors successfully")
                .result(colorService.findAllColorNotInProductSize(idProductSize)).build();
    }

    @GetMapping("/productSize/{id}")
    public ApiRes<List<ProductColorsAdmin>> getAllColorsOfProduct(@PathVariable Long id) {
        return ApiRes.<List<ProductColorsAdmin>>builder().code(1003).message("get colors successfully")
                .result(colorService.getAllColorsOfProduct(id)).build();
    }
}
