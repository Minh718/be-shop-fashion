package com.shopro.shop1905.entities;

import java.time.LocalDateTime;

import com.shopro.shop1905.util.DateTimeUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CartProduct
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductSizeColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    @Builder.Default
    private LocalDateTime createdAt = DateTimeUtil.getCurrentVietnamTime();
    @Builder.Default
    private LocalDateTime updateAt = DateTimeUtil.getCurrentVietnamTime();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "product_size_color_id", referencedColumnName = "id")
    private Long productSizeColorId;
}