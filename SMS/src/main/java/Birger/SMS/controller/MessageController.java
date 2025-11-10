package Birger.SMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.model.Message;
import Birger.SMS.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 🔹 Ajouter un nouveau message
     */
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message saved = messageService.save(message);
        return ResponseEntity.ok(saved);
    }

    /**
     * 🔹 Récupérer tous les messages
     */
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.findAll();
        return ResponseEntity.ok(messages);
    }

    /**
     * 🔹 Supprimer un message par ID
     */
    /**
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        try {
            messageService.deleteById(id);
            return ResponseEntity.ok("Message supprimé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Message non trouvé !");
        }
    }
     */
}
