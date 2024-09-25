package com.shopro.shop1905.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shopro.shop1905.dtos.dtosReq.AdminSendMess;
import com.shopro.shop1905.dtos.dtosReq.UserSendMess;
import com.shopro.shop1905.dtos.dtosRes.MessageDTO;
import com.shopro.shop1905.dtos.dtosRes.projections.InfoChatBox;
import com.shopro.shop1905.dtos.dtosRes.projections.ListChatBox;
import com.shopro.shop1905.entities.ChatBox;
import com.shopro.shop1905.entities.Message;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.mappers.MessageMapper;
import com.shopro.shop1905.repositories.ChatBoxRepository;
import com.shopro.shop1905.repositories.MessageRepository;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatBoxRepository chatBoxRepository;
    private final UserService userService;
    private final MessageRepository messageRepository;

    @Transactional
    public List<MessageDTO> userGetMessages(int size) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatBox chatBox = chatBoxRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("ChatBox not found"));
        chatBox.setUserReaded(true);
        Pageable pageable = PageRequest.of(0, size);
        List<Message> messages = messageRepository.findTopMessages(chatBox.getId(), pageable);
        return MessageMapper.INSTANCE.toMessageDTOs(messages);
    }

    public InfoChatBox getInfoChatBox() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatBox chatBox = chatBoxRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("ChatBox not found"));
        String adminId = userService.getIdAdmin();
        return InfoChatBox.builder().idAdmin(adminId).idChatBox(chatBox.getId()).isSeen(chatBox.isUserReaded()).build();
    }

    public List<ListChatBox> findChatBoxListUnSeenByAdmin() {
        return messageRepository.findChatBoxListUnSeenByAdmin();
    };

    @Transactional
    public List<MessageDTO> adminGetMessages(Long chatBoxId, int size) {
        ChatBox chatBox = chatBoxRepository.findById(chatBoxId)
                .orElseThrow(() -> new RuntimeException("ChatBox not found"));
        chatBox.setAdminReaded(true);
        Pageable pageable = PageRequest.of(0, size);
        List<Message> messages = messageRepository.findTopMessages(chatBox.getId(), pageable);
        return MessageMapper.INSTANCE.toMessageDTOs(messages);
    }

    public MessageDTO userSendMessage(UserSendMess message) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatBox chatBox = chatBoxRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_BOX_NOT_FOUND));
        chatBox.setAdminReaded(false);
        chatBox.setUpdatedAt(LocalDateTime.now());
        Message newMessage = new Message();
        newMessage.setIdChatBox(chatBox.getId());
        newMessage.setIdSend(userId);
        newMessage.setMessage(message.getMessage());
        messageRepository.save(newMessage);
        chatBoxRepository.save(chatBox);
        return MessageMapper.INSTANCE.toMessageDTO(newMessage);
    }

    public MessageDTO adminSendMessage(AdminSendMess adminSendMess) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatBox chatBox = chatBoxRepository.findById(adminSendMess.getChatBoxId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_BOX_NOT_FOUND));
        chatBox.setUserReaded(false);
        chatBox.setUpdatedAt(LocalDateTime.now());
        Message newMessage = new Message();
        newMessage.setIdChatBox(chatBox.getId());
        newMessage.setIdSend(userId);
        newMessage.setMessage(adminSendMess.getMessage());
        messageRepository.save(newMessage);
        chatBoxRepository.save(chatBox);
        return MessageMapper.INSTANCE.toMessageDTO(newMessage);
    }
}
