package com.alura.desafio_forohub.service;

import com.alura.desafio_forohub.dto.TopicCreateDTO;
import com.alura.desafio_forohub.dto.TopicDTO;

import java.util.List;

public interface TopicService {
    TopicDTO createTopic(TopicCreateDTO dto, String author);
    List<TopicDTO> getAllTopics();
    TopicDTO getTopicById(Long id);
    TopicDTO updateTopic(Long id, TopicCreateDTO dto, String username);
    void deleteTopic(Long id, String username);
}

