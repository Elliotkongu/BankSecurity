package com.example.banksecurity.Controllers;

import com.example.banksecurity.DTOs.Request.FeedbackDTO;
import com.example.banksecurity.DTOs.Request.ReplyDTO;
import com.example.banksecurity.DTOs.Request.SupportDTO;
import com.example.banksecurity.Services.MessageService;
import com.example.banksecurity.Storage.Message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/message")
public class MessageController {

    MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/sendfeedback")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('BANKER')")
    public ResponseEntity<String> sendFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return messageService.sendFeedback(feedbackDTO);
    }

    @PostMapping("/sendsupport")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('BANKER')")
    public ResponseEntity<String> sendSupport(@RequestBody SupportDTO supportDTO) {
        return messageService.sendSupport(supportDTO);
    }

    @PostMapping("/sendreply")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sendReply(@RequestBody ReplyDTO replyDTO) {
        return messageService.sendReply(replyDTO);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Message>> getAllMessages(){
        return messageService.getAllMessages();
    }
}
