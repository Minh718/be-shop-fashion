package com.shopro.shop1905.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.shopro.shop1905.enums.TypeLogin;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;
    private String keyToken;
    private String password;
    private String address;
    private String picture;
    @Builder.Default
    private String idUserGoogle = "0";
    @Builder.Default
    private String idUserFacebook = "0";
    @Builder.Default
    private String idUserGithub = "0";
    private LocalDate dob;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private TypeLogin typeLogin;
    @Builder.Default
    private boolean isactive = true;

    // @ElementCollection
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "refreshtoken", joinColumns = @JoinColumn(name = "id"))
    private Set<String> refreshTokenUsed;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles;

    // @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    // Cart cart;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserVoucher> userVouchers;
}