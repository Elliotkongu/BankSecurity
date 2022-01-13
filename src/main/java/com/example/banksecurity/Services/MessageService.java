package com.example.banksecurity.Services;

import com.example.banksecurity.Storage.Message.Message;
import com.example.banksecurity.Storage.Message.MessageRepository;
import com.example.banksecurity.Storage.Message.MessageType.EMessageType;
import com.example.banksecurity.Storage.Message.MessageType.MessageType;
import com.example.banksecurity.Storage.Message.MessageType.MessageTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

    MessageTypeRepository messageTypeRepository;
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageTypeRepository messageTypeRepository, MessageRepository messageRepository) {
        this.messageTypeRepository = messageTypeRepository;
        this.messageRepository = messageRepository;
    }

    public ResponseEntity<String> sendFeedback(String title, String text, Long senderId) {
        Optional<MessageType> messageType = messageTypeRepository.findByMessageType(EMessageType.FEEDBACK);
        if (messageType.isPresent()) {
            Message message = new Message(title, text, messageType.get(), senderId);
            messageRepository.save(message);
            return ResponseEntity.ok().body("Feedback sent!");
        } else {
            return ResponseEntity.internalServerError().body("Message type not found");
        }
    }
}
