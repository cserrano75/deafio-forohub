package com.alura.desafio_forohub.service.impl;

import com.alura.desafio_forohub.dto.TopicCreateDTO;
import com.alura.desafio_forohub.dto.TopicDTO;
import com.alura.desafio_forohub.exception.ResourceNotFoundException;
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
@Transactional

public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public TopicDTO createTopic(TopicCreateDTO dto, String author) {
        Topic topic = Topic.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .createdAt(Instant.now())
                .build();
        Topic saved = topicRepository.save(topic);
        return toDTO(saved);
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO getTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
        return toDTO(topic);
    }

    @Override
    public TopicDTO updateTopic(Long id, TopicCreateDTO dto, String username) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));

        // Regla conservadora: la verificación de autor/rol se maneja en la capa de seguridad/controlador.
        topic.setTitle(dto.getTitle());
        topic.setContent(dto.getContent());
        topic.setUpdatedAt(Instant.now());

        Topic saved = topicRepository.save(topic);
        return toDTO(saved);
    }

    @Override
    public void deleteTopic(Long id, String username) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
        topicRepository.delete(topic);
    }

    private TopicDTO toDTO(Topic t) {
        return TopicDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .content(t.getContent())
                .author(t.getAuthor())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}