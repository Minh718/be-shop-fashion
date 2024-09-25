package com.shopro.shop1905.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopro.shop1905.dtos.dtosRes.projections.InfoChatBox;
import com.shopro.shop1905.entities.ChatBox;

@Repository
public interface ChatBoxRepository extends JpaRepository<ChatBox, Long> {

    Optional<ChatBox> findByUserId(String userId);

    // Optional<InfoChatBox> findInfoChatBoxByUserId(String userId);
}
