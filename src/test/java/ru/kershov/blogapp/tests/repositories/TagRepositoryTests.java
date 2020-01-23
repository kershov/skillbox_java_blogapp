package ru.kershov.blogapp.tests.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kershov.blogapp.model.Tag;
import ru.kershov.blogapp.repositories.TagsRepository;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class TagRepositoryTests {
    /**
     * IMPORTANT: Do not forget to add .env variables into your configuration
     * in case of testing each individual test
     */
    
    @Autowired
    private TagsRepository tagsRepository;

    private Tag tag;

    @Before
    public void init() {
        tag = new Tag();
        tag.setName("Hot");
    }

    @Test
    public void testSaveAndGetTag() {
        assertNotNull(tag);

        Tag savedTag = tagsRepository.save(tag);
        assertNotNull(savedTag);

        Tag tagFromDB = tagsRepository.findById(savedTag.getId()).orElse(null);
        assertNotNull(tagFromDB);
        assertEquals(savedTag.getName(), tagFromDB.getName());

        assertEquals(0, tagFromDB.getPosts().size());
    }

    @Test
    public void testDeleteTag() {
        assertNotNull(tag);

        Tag savedTag = tagsRepository.save(tag);
        assertNotNull(savedTag);

        tagsRepository.delete(tag);

        Tag deletedTag = tagsRepository.findById(savedTag.getId()).orElse(null);
        assertNull(deletedTag);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDeleteNonExistentTag() {
        tagsRepository.deleteById(-1);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddTagWithBlankName() {
        assertNotNull(tag);

        tag.setName("");
        tagsRepository.save(tag);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddTagWithNameMoreThan255Symbols() {
        assertNotNull(tag);

        tag.setName("A".repeat(260));
        tagsRepository.save(tag);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCantAddAnotherTagWithSameEmail() {
        assertNotNull(tag);

        Tag anotherTag = new Tag();
        anotherTag.setName("Hot");

        Tag savedUser = tagsRepository.save(tag);
        assertNotNull(savedUser);

        tagsRepository.save(anotherTag);
    }

    @Test
    public void testAddMultipleTags() {
        final byte NUM_TAGS = 5;

        for (int i = 0; i < NUM_TAGS; i++) {
            Tag tag = new Tag();
            tag.setName("Tag" + i);
            tagsRepository.save(tag);
        }

        Set<Tag> tags = new HashSet<>();
        tagsRepository.findAll().forEach(tags::add);

        assertNotNull(tags);
        assertEquals(NUM_TAGS, tags.size());
    }
}
