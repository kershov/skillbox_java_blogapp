package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.dto.TagDTO;
import ru.kershov.blogapp.repositories.TagsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagsService {
    @Autowired
    TagsRepository tagsRepository;

    public ResponseEntity<Map<String, List<TagDTO>>> getWeightedTags(String query) {
        List<TagDTO> weightedTags;

        // TODO: Stub: Math.random() >> to be replaced with real tag weight calculation
        if (query == null) {
            weightedTags = tagsRepository.findAll().stream()
                    .map(t -> new TagDTO(t.getName(), Math.random())).collect(Collectors.toList());
        } else {
            weightedTags = tagsRepository.findByNameContaining(query).stream()
                    .map(t -> new TagDTO(t.getName(), Math.random())).collect(Collectors.toList());
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new HashMap<>() {{ put("tags", weightedTags); }});
    }
}
