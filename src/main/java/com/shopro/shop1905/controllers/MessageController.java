package com.shopro.shop1905.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.AdminSendMess;
import com.shopro.shop1905.dtos.dtosReq.UserSendMess;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.MessageDTO;
import com.shopro.shop1905.dtos.dtosRes.projections.InfoChatBox;
import com.shopro.shop1905.dtos.dtosRes.projections.ListChatBox;
import com.shopro.shop1905.services.MessageService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/messages")
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/user/get")
    public ApiRes<List<MessageDTO>> getMessages(@RequestParam(defaultValue = "10") int size) {
        return ApiRes.<List<MessageDTO>>builder().result(messageService.userGetMessages(size)).code(1000)
                .message("succesfully").build();
    }

    @GetMapping("/infoChatBox")
    public ApiRes<InfoChatBox> getInfoChatBox() {
        return ApiRes.<InfoChatBox>builder().result(messageService.getInfoChatBox()).code(1000)
                .message("succesfully").build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get")
    public ApiRes<List<MessageDTO>> getMessages(@RequestParam(defaultValue = "10") int size,
            @RequestParam Long chatBoxId) {
        return ApiRes.<List<MessageDTO>>builder().result(messageService.adminGetMessages(chatBoxId, size)).code(1000)
                .message("succesfully").build();
    }

    @PostMapping("/user/send")
    public ApiRes<MessageDTO> userSendMessage(@RequestBody UserSendMess message) {
        return ApiRes.<MessageDTO>builder().code(1000).result(messageService.userSendMessage(message))
                .message("succesfully").build();
    }

    @PostMapping("/admin/send")
    public ApiRes<MessageDTO> adminSendMessage(@RequestBody AdminSendMess adminSendMess) {
        return ApiRes.<MessageDTO>builder().code(1000).result(messageService.adminSendMessage(adminSendMess))
                .message("succesfully").build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/cbus")
    public ApiRes<List<ListChatBox>> adminSendMessage() {
        return ApiRes.<List<ListChatBox>>builder().code(1000).result(messageService.findChatBoxListUnSeenByAdmin())
                .message("succesfully").build();
    }
}
