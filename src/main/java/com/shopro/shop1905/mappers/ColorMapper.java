package com.shopro.shop1905.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.ColorRes;
import com.shopro.shop1905.entities.Color;

@Mapper
public interface ColorMapper {
    ColorMapper INSTANCE = Mappers.getMapper(ColorMapper.class);

    ColorRes toColorRes(Color color);

    List<ColorRes> toColorRess(List<Color> colors);
}
