package Birger.SMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Birger.SMS.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {}
