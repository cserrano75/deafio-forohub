package com.alura.desafio_forohub.service.impl;

import com.alura.desafio_forohub.dto.TopicCreateDTO;
import com.alura.desafio_forohub.dto.TopicDTO;
import com.alura.desafio_forohub.exception.ResourceNotFoundException;
import com.alura.desafio_forohub.exception.UnauthorizedAccessException;
import com.alura.desafio_forohub.model.Topic;
import com.alura.desafio_forohub.repository.TopicRepository;
import com.alura.desafio_forohub.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)

public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional // escritura
    public TopicDTO createTopic(TopicCreateDTO dto, String author) {
        Topic topic = Topic.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .build();

        Topic saved = topicRepository.save(topic);
        return toDTO(saved);
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO getTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tema no encontrado (id): " + id));
        return toDTO(topic);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional // escritura
    public TopicDTO updateTopic(Long id, TopicCreateDTO dto, String username) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tema no encontrado (id): " + id));

        if (!username.equals(topic.getAuthor())) { // null-safe primero el parámetro
            throw new UnauthorizedAccessException("No esta autorizado para actualizar este tema");
        }

        topic.setTitle(dto.getTitle());
        topic.setContent(dto.getContent());
        topic.setUpdatedAt(Instant.now());

        Topic saved = topicRepository.save(topic);
        return toDTO(saved);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional // escritura
    public void deleteTopic(Long id, String username) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tema no encontrado (id): " + id));

        if (!username.equals(topic.getAuthor())) {
            throw new UnauthorizedAccessException("No esta autorizado para actualizar este tema");
        }

        topicRepository.delete(topic);
    }

    private TopicDTO toDTO(Topic topic) {
        return TopicDTO.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .content(topic.getContent())
                .author(topic.getAuthor())
                .createdAt(topic.getCreatedAt())
                .updatedAt(topic.getUpdatedAt())
                .build();
    }
}