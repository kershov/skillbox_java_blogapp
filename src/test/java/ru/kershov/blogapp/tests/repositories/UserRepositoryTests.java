package ru.kershov.blogapp.tests.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.repositories.UsersRepository;

import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {
    /**
     * IMPORTANT: Do not forget to add .env variables into your configuration
     * in case of testing each individual test
     */
    
    @Autowired
    private UsersRepository usersRepository;

    private User user;

    @Before
    public void init() {
        user = new User();
        user.setModerator(false);
        user.setRegTime(Instant.now());
        user.setName("John Doe");
        user.setEmail("john.doe@email.tld");
        user.setPassword("qwerty");
    }

    @Test
    public void testSaveAndGetUser() {
        assertNotNull(user);

        User savedUser = usersRepository.save(user);
        assertNotNull(savedUser);

        User userFromDB = usersRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(userFromDB);
        assertEquals(savedUser.isModerator(), userFromDB.isModerator());
        assertEquals(savedUser.getRegTime(), userFromDB.getRegTime());
        assertEquals(savedUser.getName(), userFromDB.getName());
        assertEquals(savedUser.getEmail(), userFromDB.getEmail());
        assertEquals(savedUser.getPassword(), userFromDB.getPassword());

        assertEquals(0, userFromDB.getPosts().size());
        assertEquals(0, userFromDB.getModeratedPosts().size());
        assertEquals(0, userFromDB.getComments().size());
        assertEquals(0, userFromDB.getVotes().size());
    }

    @Test
    public void testDeleteUser() {
        assertNotNull(user);

        User savedUser = usersRepository.save(user);
        assertNotNull(savedUser);

        usersRepository.delete(user);

        User deletedUser = usersRepository.findById(savedUser.getId()).orElse(null);
        assertNull(deletedUser);
    }
}