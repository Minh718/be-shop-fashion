package com.shopro.shop1905.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.shopro.shop1905.dtos.dtosRes.MessageDTO;
import com.shopro.shop1905.entities.Message;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDTO toMessageDTO(Message message);

    List<MessageDTO> toMessageDTOs(List<Message> messages);
}
