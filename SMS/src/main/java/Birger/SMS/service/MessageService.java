package Birger.SMS.service;

import java.util.List;

import org.springframework.stereotype.Service;

import Birger.SMS.model.Message;
import Birger.SMS.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
