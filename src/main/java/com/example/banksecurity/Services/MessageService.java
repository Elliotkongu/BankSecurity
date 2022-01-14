package com.example.banksecurity.Services;

import com.example.banksecurity.DTOs.Request.FeedbackDTO;
import com.example.banksecurity.DTOs.Request.ReplyDTO;
import com.example.banksecurity.DTOs.Request.SupportDTO;
import com.example.banksecurity.Storage.Message.Message;
import com.example.banksecurity.Storage.Message.MessageRepository;
import com.example.banksecurity.Storage.Message.MessageType.EMessageType;
import com.example.banksecurity.Storage.Message.MessageType.MessageType;
import com.example.banksecurity.Storage.Message.MessageType.MessageTypeRepository;
import com.example.banksecurity.Storage.User.User;
import com.example.banksecurity.Storage.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    MessageTypeRepository messageTypeRepository;
    MessageRepository messageRepository;
    UserRepository userRepository;

    @Autowired
    public MessageService(MessageTypeRepository messageTypeRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.messageTypeRepository = messageTypeRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> sendFeedback(FeedbackDTO feedbackDTO) {
        Optional<MessageType> messageType = messageTypeRepository.findByMessageType(EMessageType.FEEDBACK);
        if (messageType.isPresent()) {
            Message message = new Message(feedbackDTO.getTitle(), feedbackDTO.getText(), messageType.get(), feedbackDTO.getSenderId());
            messageRepository.save(message);
            return ResponseEntity.ok().body("Feedback sent!");
        } else {
            return ResponseEntity.internalServerError().body("Message type not found");
        }
    }

    public ResponseEntity<String> sendSupport(SupportDTO supportDTO) {
        Optional<MessageType> messageType = messageTypeRepository.findByMessageType(EMessageType.SUPPORT);
        if (messageType.isPresent()) {
            Message message = new Message(supportDTO.getTitle(), supportDTO.getText(), messageType.get(), supportDTO.getSenderId());
            messageRepository.save(message);
            return ResponseEntity.ok().body("Support request sent!");
        } else {
            return ResponseEntity.internalServerError().body("Message type not found");
        }
    }

    public ResponseEntity<String> sendReply(ReplyDTO replyDTO) {
        Optional<MessageType> messageType = messageTypeRepository.findByMessageType(EMessageType.ADMIN_REPLY);
        if (messageType.isPresent()) {
            if (messageRepository.findById(replyDTO.getReceiverId()).isPresent()) {
                Message message = messageRepository.findById(replyDTO.getReceiverId()).get();
                if (userRepository.findById(message.getSenderId()).isPresent()) {
                    Message reply = new Message(replyDTO.getTitle(), replyDTO.getText(), messageType.get(), replyDTO.getSenderId());
                    messageRepository.save(reply);
                    User user = userRepository.findById(message.getSenderId()).get();
                    user.getInbox().add(reply);
                    userRepository.save(user);
                    return ResponseEntity.ok().body("Reply sent!");
                } else {
                    return ResponseEntity.badRequest().body("User not found");
                }
            } else {
                return ResponseEntity.badRequest().body("Message not found");
            }
        } else {
            return ResponseEntity.internalServerError().body("Message type not found");
        }
    }

    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok().body(messageRepository.findAll());
    }
}
