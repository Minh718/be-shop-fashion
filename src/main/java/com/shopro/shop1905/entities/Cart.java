package com.shopro.shop1905.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.Data;

/**
 * Cart
 */
@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    // @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinColumn(name = "user_id", referencedColumnName = "id")
    // User user;

    // @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch =
    // FetchType.EAGER)
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt desc")
    private Set<CartProductSizeColor> CartProductsSizeColors = new HashSet<>();
}