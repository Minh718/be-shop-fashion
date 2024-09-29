package com.shopro.shop1905.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.shopro.shop1905.dtos.Message;
import com.shopro.shop1905.dtos.dtosRes.OrderNotificationPayload;

@Controller
public class SocketController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    // @MessageMapping("/application")
    // @SendTo("/all/messages")
    // public Message send(final Message message) throws Exception {
    // return message;
    // }

    @MessageMapping("/private")
    public void sendToSpecificUser(@Payload Message message) {
        System.out.println("Message: " + message.getText() + " to " + message.getTo() + "KKKK");
        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message.getText());
    }

    public void notifyOrderSuccess(OrderNotificationPayload payload) {
        simpMessagingTemplate.convertAndSend("/topic/order-notification", payload);
    }
}