package com.shopro.shop1905.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Product
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_name", columnList = "name"),
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    private int percent;
    private String image;
    private Long categoryId;

    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    private boolean isPublish;
    private boolean isDraft;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subCate_id", referencedColumnName = "id")
    private SubCategory subCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_detail_id", referencedColumnName = "id")
    DetailProduct detailProduct;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    // @OrderBy("id ASC")
    @Builder.Default

    private List<ProductSize> productSizes = new ArrayList<>();

    @Override
    public String toString() {
        return "Product [categoryId=" + categoryId + ", createdDate=" + createdDate + ", id=" + id + ", image=" + image
                + ", isDraft=" + isDraft + ", isPublish=" + isPublish + ", name=" + name + ", percent=" + percent
                + ", price=" + price + ", productDetail=" + detailProduct + ", productSizes=" + productSizes
                + ", subCategory=" + subCategory + ", totalAccess=" + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        return id == other.id;
    }
}