package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.Tag;
import ru.kershov.blogapp.model.dto.TagDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.TagsRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagsService {
    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    PostsRepository postsRepository;

    public ResponseEntity<?> getWeightedTags(String query) {
        return ResponseEntity.status(HttpStatus.OK).body(getTags(query));
    }

    /*
     * TODO: Get not all the tags but only those whose posts are marked as
     *       isActive, ModerationStatus.ACCEPTED & time <= NOW()
     */
    private Map<String, Object> getTags(String query) {
        final long NUM_POSTS = postsRepository.count();

        final List<Tag> tags = (query == null) ?
                tagsRepository.findAll() : tagsRepository.findByNameContaining(query);

        // Tags >> TagDTOs + calculate weight + sort by avg
        final List<TagDTO> weightedTags = tags.stream()
                .map(tag -> new TagDTO(tag, NUM_POSTS))
                .sorted(Comparator.comparing(TagDTO::getBaseWeight).reversed())
                .collect(Collectors.toList());

        // Get max weight from TagDTOs
        final double MAX_TAG_WEIGHT = weightedTags.get(0).getBaseWeight();

        // Calculate normalized weight for all the tags
        weightedTags.forEach(tag -> tag.setWeight(MAX_TAG_WEIGHT));

        return new HashMap<>() {{
            put("tags", weightedTags);
        }};
    }
}
