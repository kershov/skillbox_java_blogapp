package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.Tag;
import ru.kershov.blogapp.model.dto.TagDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.TagsRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagsService {
    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    PostsRepository postsRepository;

    public ResponseEntity<?> getWeightedTags(String query) {
        final long NUM_POSTS = postsRepository.countActivePosts(Instant.now());

        List<TagDTO> tags = tagsRepository.findAllTags();
        tags.forEach(tag -> tag.setBaseWeight(NUM_POSTS));
        tags.forEach(tag -> tag.setWeight(tags.get(0).getBaseWeight()));

        final List<TagDTO> filteredTags = (query == null) ? tags : tags.stream()
                .filter(tag ->
                        tag.getName().toLowerCase()
                                .contains(query.toLowerCase()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new HashMap<>() {{
            put("tags", filteredTags);
        }});
    }

    public Tag saveTag(String tagName) {
        Tag tag = tagsRepository.findByNameIgnoreCase(tagName);

        return (tag != null) ? tag : tagsRepository.save(new Tag(tagName));
    }
}
