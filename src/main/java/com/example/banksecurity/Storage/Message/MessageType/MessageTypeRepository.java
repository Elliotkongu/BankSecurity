package com.example.banksecurity.Storage.Message.MessageType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageTypeRepository extends JpaRepository<MessageType, Long> {

    Optional<MessageType> findByMessageType(EMessageType messageType);
}
