package com.example.banksecurity.Controllers;

import com.example.banksecurity.DTOs.Request.FeedbackDTO;
import com.example.banksecurity.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return messageService.sendFeedback(feedbackDTO.getTitle(), feedbackDTO.getText(), feedbackDTO.getSenderId());
    }
}
