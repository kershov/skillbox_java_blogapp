package ru.kershov.blogapp.components;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kershov.blogapp.enums.ModerationStatus;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.Tag;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.repositories.*;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component("db-init-runner")
@ConditionalOnExpression("${blogapp.runners.db-init-enabled:false}")
public class InitDB implements CommandLineRunner {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private VotesRepository votesRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Override
    public void run(String... args) throws Exception {
        generateTags();
        generateUsers();
        generatePosts();
    }

    private void generateUsers() {
        final Map<String, String> userData = new HashMap<>() {{
            put("Иван Иванов", "ivanov.ivan@fakemail.tld");
            put("Дмитрий Дмитриев", "dima.dima@fakemail.tld");
            put("Сергей Сергеев", "serge@fakemail.tld");
            put("Татьяна Татьянович", "tanya.tanya@fakemail.tld");
            put("Демьян Демьяненко", "demyan2D@fakemail.tld");
            put("Алевтина Алевтинкина", "alevtina@fakemail.tld");
            put("Артём Артёмов", "aa@fakemail.tld");
            put("Константин Константинов", "konstantin.konstantinov@fakemail.tld");
            put("Пётр Петров", "peter.the.pete@fakemail.tld");
            put("Анна Анновян", "anna@fakemail.tld");
        }};


        final float IS_MODERATOR_PROBABILITY = 0.3f;
        final int DAYS_BACK = 30;
        final Instant NOW = Instant.now();

        List<User> users =  userData.entrySet().stream().map(e -> {
            User user = new User();

            user.setName(e.getKey());
            user.setEmail(e.getValue());

            Instant randomRegTime = getRandomDateInRange(NOW, DAYS_BACK, 0);

            user.setRegTime(randomRegTime);
            user.setModerator(Math.random() < IS_MODERATOR_PROBABILITY);

            final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
            user.setPassword(encoder.encode("12345"));

            return user;
        }).collect(Collectors.toList());

        log.info(String.valueOf(
                usersRepository.saveAll(users)
        ));
    }

    private void generatePosts() {
        final int NUM_POSTS = 40;
        final Instant POST_NOW_DATE = Instant.now();
        final int POST_DAYS_BEFORE_NOW = 60;
        final int POST_DAYS_AFTER_NOW = 15;
        final int POST_MAX_VIEWS_COUNT = 50;

        final String API_URL = "https://fish-text.ru/get";
        final String TITLE_PARAMS = "?type=title&format=html&number=" + NUM_POSTS;
        final String PARAGRAPH_PARAMS = "?type=paragraph&format=html&number=" + 2 * NUM_POSTS;

        final List<User> users = usersRepository.findByIsModeratorFalse();
        final List<User> moderators = usersRepository.findByIsModeratorTrue();

        final int MAX_TAGS_PER_POST = 5;
        final float POST_WITH_NO_TAGS_PROBABILITY = 0.1f;
        final List<Tag> tags = (List<Tag>) tagsRepository.findAll();

        List<String> titles = fetchFishTextAPI(API_URL + TITLE_PARAMS, "h1", false);
        LinkedList<String> paragraphs = new LinkedList<>(
                Objects.requireNonNull(
                        fetchFishTextAPI(API_URL + PARAGRAPH_PARAMS, "p", true)
                ));

        assert !titles.isEmpty() && !paragraphs.isEmpty();

        List<Post> posts = new ArrayList<>();

        for (String title : titles) {
            Post post = new Post();

            // Set moderation status
            int randomId = new Random().nextInt(ModerationStatus.values().length);
            ModerationStatus moderationStatus = ModerationStatus.getById(randomId);
            post.setModerationStatus(moderationStatus);

            // Whether post is active or not: active only if ModerationStatus is ACCEPTED
            post.setActive(moderationStatus == ModerationStatus.ACCEPTED);

            // Set random moderator
            int randomModeratorId = new Random().nextInt(moderators.size());
            User moderator = moderators.get(randomModeratorId);
            post.setModeratedBy(moderator);

            // Set random author
            int randomAuthorId = new Random().nextInt(users.size());
            User author = users.get(randomAuthorId);
            post.setAuthor(author);

            // Set random publication time
            post.setTime(getRandomDateInRange(POST_NOW_DATE, POST_DAYS_BEFORE_NOW, POST_DAYS_AFTER_NOW));

            // Set post title
            post.setTitle(title);

            // Set post text (made of 2 paragraphs)
            String text = paragraphs.poll() + "\n\n" + paragraphs.poll();
            post.setText(text);

            // Set random view count
            post.setViewCount(new Random().nextInt(POST_MAX_VIEWS_COUNT));

            // Set tags
            if (Math.random() > POST_WITH_NO_TAGS_PROBABILITY) {
                int numTags = new Random().nextInt(MAX_TAGS_PER_POST);

                Set<Tag> postTags = IntStream.range(0, numTags)
                        .mapToObj(i -> tags.get(new Random().nextInt(tags.size())))
                        .collect(Collectors.toSet());

                post.setTags(postTags);
            }

            // Votes: SKIPPED

            // Add post for further saving...
            posts.add(post);
        }

        List<Post> savedPosts = (List<Post>) postsRepository.saveAll(posts);

        log.info("Total posts saved: " + savedPosts.size());
    }

    private void generateTags() {
        final String[] tagNames = {
                "новость", "технологии", "горячее", "важно", "москва", "программирование",
                "идея", "новинка", "медицина", "космос", "Linux", "Windows", "MacOS",
                "Spring", "Java", "Python", "HTML", "нуар", "игры", "статистика"};

        List<Tag> tags = Arrays.stream(tagNames)
                .map(tagName -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);

                    return tag;
                }).collect(Collectors.toList());

        log.info(String.valueOf(
                tagsRepository.saveAll(tags)
        ));
    }

    private List<String> fetchFishTextAPI(String apiURL, String cssQuery, boolean keepTags) {
        try {
            Document document = Jsoup.connect(apiURL).get();

            Thread.sleep((int) (500 * Math.random()));

            Elements elements = document.select(cssQuery);

            return elements.stream()
                    .map(e -> !keepTags ? e.text() : e.toString())
                    .collect(Collectors.toList());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private Instant getRandomDateInRange(Instant now, int daysBeforeNow, int daysAfterNow) {
        final Instant START_DATE = now.minus(daysBeforeNow, ChronoUnit.DAYS);
        final Instant END_DATE = now.plus(daysAfterNow, ChronoUnit.DAYS);

        return Instant.ofEpochMilli(
                ThreadLocalRandom.current()
                        .nextLong(START_DATE.toEpochMilli(), END_DATE.toEpochMilli())
        );
    }
}
