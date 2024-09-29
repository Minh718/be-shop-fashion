package com.shopro.shop1905.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosReq.OrderDTO;
import com.shopro.shop1905.dtos.dtosRes.DetailOrderDTO;
import com.shopro.shop1905.dtos.dtosRes.OrderProductResDTO;
import com.shopro.shop1905.dtos.dtosRes.OrderResDTO;
import com.shopro.shop1905.entities.OrderProduct;
import com.shopro.shop1905.entities.TblOrder;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResDTO toOrderResDTO(TblOrder order);

    @Mapping(target = "totalAmount", source = "order.checkoutRes.paymentFee")
    @Mapping(target = "discount", source = "order.checkoutRes.discount")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderProducts", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "shippingCost", ignore = true)
    @Mapping(target = "shippingFee", ignore = true)
    @Mapping(target = "shippingStatus", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    @Mapping(target = "urlPayment", ignore = true)
    @Mapping(target = "user", ignore = true)
    TblOrder toOrder(OrderDTO order);

    DetailOrderDTO toDetailOrderDTO(TblOrder order);

    // @Mapping(target = "image", source =
    // "orderProduct.productSizeColor.productSize.product.image")
    // @Mapping(target = "name", source =
    // "orderProduct.productSizeColor.productSize.product.name")
    @Mapping(target = "size", source = "orderProduct.productSizeColor.productSize.size.name")
    @Mapping(target = "color", source = "orderProduct.productSizeColor.color.name")
    OrderProductResDTO toProductOrderDTO(OrderProduct orderProduct);

    Set<OrderProductResDTO> toProductOrderDTOs(Set<OrderProduct> orderProducts);
    // CartProductDTO toCartProductDTO(CartProduct cartProduct);
}
