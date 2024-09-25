package com.shopro.shop1905.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.entities.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query("SELECT v FROM Voucher v where v.forNewUser = true AND v.endDate > CURRENT_TIMESTAMP AND v.isActive = true")
    List<Voucher> findAllForNewUser();

    Optional<Voucher> findByCode(String code);

}