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
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.repositories.UsersRepository;

import javax.validation.ConstraintViolationException;
import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
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

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDeleteNonExistentUser() {
        usersRepository.deleteById(-1);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddUserWithBlankRegTime() {
        assertNotNull(user);

        user.setRegTime(null);
        usersRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddUserWithBlankName() {
        assertNotNull(user);

        user.setName("");
        usersRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddUserWithNameMoreThan255Symbols() {
        assertNotNull(user);

        user.setName("A".repeat(260));
        usersRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddUserWithBlankEmail() {
        assertNotNull(user);

        user.setEmail("");
        usersRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddUserWithEmailMoreThan255Symbols() {
        assertNotNull(user);

        user.setEmail("A".repeat(260));
        usersRepository.save(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCantAddAnotherUserWithSameEmail() {
        assertNotNull(user);

        User anotherUser = new User();
        anotherUser.setModerator(true);
        anotherUser.setRegTime(Instant.now());
        anotherUser.setName("John Doe Sr. II");
        anotherUser.setEmail("john.doe@email.tld");
        anotherUser.setPassword("asdfg123");

        User savedUser = usersRepository.save(user);
        assertNotNull(savedUser);

        usersRepository.save(anotherUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddUserWithBlankPassword() {
        assertNotNull(user);

        user.setPassword("");
        usersRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddUserWithPasswordMoreThan255Symbols() {
        assertNotNull(user);

        user.setPassword("A".repeat(260));
        usersRepository.save(user);
    }

    @Test
    public void testCanAddUserWithBlankCode() {
        assertNotNull(user);

        user.setCode("");
        usersRepository.save(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCantAddUserWithCodeMoreThan255Symbols() {
        assertNotNull(user);

        user.setCode("A".repeat(260));
        usersRepository.save(user);
    }

    @Test
    public void testCanAddUserWithBlankPhoto() {
        assertNotNull(user);

        user.setPhoto("");
        usersRepository.save(user);
    }
}
