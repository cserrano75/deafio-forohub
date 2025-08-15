package com.alura.desafio_forohub.controller;

import com.alura.desafio_forohub.model.Topic;
import com.alura.desafio_forohub.repository.TopicRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicRepository topicRepository;

    public TopicController(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    // Listar todos los topics
    @GetMapping
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    // Crear un nuevo topic
    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        topic.setCreatedAt(Instant.now());
        return topicRepository.save(topic);
    }

    // Obtener un topic por ID
    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        return topicRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar un topic
    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic updatedTopic) {
        return topicRepository.findById(id)
                .map(topic -> {
                    topic.setTitle(updatedTopic.getTitle());
                    topic.setContent(updatedTopic.getContent());
                    topic.setUpdatedAt(Instant.now());
                    return ResponseEntity.ok(topicRepository.save(topic));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un topic
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        return topicRepository.findById(id)
                .map(topic -> {
                    topicRepository.delete(topic);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}