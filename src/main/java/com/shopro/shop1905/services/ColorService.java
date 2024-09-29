package com.shopro.shop1905.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosRes.ColorRes;
import com.shopro.shop1905.dtos.dtosRes.ProductColorsAdmin;
import com.shopro.shop1905.entities.Color;
import com.shopro.shop1905.mappers.ColorMapper;
import com.shopro.shop1905.repositories.ColorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
    private final RedisService redisService;

    public void addColor(Color color) {
        colorRepository.save(color);
    }

    public List<ColorRes> findAllColorNotInProductSize(Long idProductSize) {
        List<Color> colors = colorRepository.findAllColorNotInProductSize(idProductSize);
        return ColorMapper.INSTANCE.toColorRess(colors);
    }

    public List<ProductColorsAdmin> getAllColorsOfProduct(Long id) {
        List<ProductColorsAdmin> colors = colorRepository.findAllColorsOfProduct(id);
        colors.forEach(color -> {
            var totalQuantity = redisService.getKey("productSizeColor:" + color.getId());
            if (totalQuantity != null) {
                color.setTotalQuantity((int) totalQuantity);
            }
        });
        return colors;
    }
}
