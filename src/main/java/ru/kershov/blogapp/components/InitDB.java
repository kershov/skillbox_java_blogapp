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
import ru.kershov.blogapp.model.*;
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
        generateVotes();
        generateComments();
        generateChildComments();
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

        userData.entrySet().stream()
            .map(e -> {
                User user = new User();

                user.setName(e.getKey());
                user.setEmail(e.getValue());

                Instant randomRegTime = getRandomDateInRange(NOW, DAYS_BACK, 0);

                user.setRegTime(randomRegTime);
                user.setModerator(Math.random() < IS_MODERATOR_PROBABILITY);

                final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
                user.setPassword(encoder.encode("12345"));

                return user;
            })
            .forEach(
                user -> usersRepository.save(user)
            );
    }

    private void generatePosts() {
        final int NUM_POSTS = 40;
        final Instant POST_NOW_DATE = Instant.now();
        final int POST_DAYS_BEFORE_NOW = 60;
        final int POST_DAYS_AFTER_NOW = 15;
        final int POST_MAX_VIEWS_COUNT = 50;

        final List<User> users = usersRepository.findByIsModeratorFalse();
        final List<User> moderators = usersRepository.findByIsModeratorTrue();

        final int MAX_TAGS_PER_POST = 5;
        final float POST_WITH_NO_TAGS_PROBABILITY = 0.1f;
        final List<Tag> tags = (List<Tag>) tagsRepository.findAll();

        List<String> titles = fetchFishTextAPI("title",  NUM_POSTS, "h1", false);
        LinkedList<String> paragraphs = new LinkedList<>(
                Objects.requireNonNull(
                        fetchFishTextAPI("paragraph", NUM_POSTS, "p", true)
                ));

        assert !titles.isEmpty() && !paragraphs.isEmpty();

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

            // Set post text (made of 1 paragraph)
            post.setText(paragraphs.poll());

            // Set random view count
            post.setViewCount(new Random().nextInt(POST_MAX_VIEWS_COUNT));

            // Set tags
            if (Math.random() > POST_WITH_NO_TAGS_PROBABILITY) {
                int numTags = new Random().nextInt(MAX_TAGS_PER_POST);

                IntStream.range(0, numTags)
                        .mapToObj(i -> tags.get(new Random().nextInt(tags.size())))
                        .forEach(post::addTag);
            }

            // Votes: SKIPPED

            // SAVE Post
            postsRepository.save(post);
        }
    }

    private void generateVotes() {
        // Likes
        setLikes(true);

        // Dislikes
        setLikes(false);
    }

    private void generateTags() {
        final String[] tagNames = {
                "новость", "технологии", "горячее", "важно", "москва", "программирование",
                "идея", "новинка", "медицина", "космос", "Linux", "Windows", "MacOS",
                "Spring", "Java", "Python", "HTML", "нуар", "игры", "статистика"};

        Arrays.stream(tagNames)
                .map(tagName -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);

                    return tag;
                })
                .forEach(tag ->
                        tagsRepository.save(tag)
                );
    }

    private void generateComments() {
        final int MAX_COMMENTS = 200;
        final int TOTAL_USERS = (int) usersRepository.count();
        final int TOTAL_POSTS = (int) postsRepository.count();

        LinkedList<String> titles = new LinkedList<>(fetchFishTextAPI("title",  MAX_COMMENTS, "h1", false));

        final float POST_COMMENT_PROBABILITY = 0.3f;

        while (!titles.isEmpty()) {
            if (Math.random() < POST_COMMENT_PROBABILITY) {
                Comment comment = new Comment();

                // Set random post
                int randomPostId = new Random().nextInt(TOTAL_POSTS) + 1;
                Post post = postsRepository.findById(randomPostId).get();
                comment.setPost(post);

                // Set random post
                int randomUserId = new Random().nextInt(TOTAL_USERS) + 1;
                User user = usersRepository.findById(randomUserId).get();
                comment.setUser(user);

                // Set time [from postTime to now - postTime]
                Instant commentTime = post.getTime().plus(new Random().nextInt(10), ChronoUnit.MINUTES);
                comment.setTime(commentTime);

                // Set text
                comment.setText(titles.poll());

                commentsRepository.save(comment);
            }
        }
    }

    private void generateChildComments() {
        final int MAX_COMMENTS = 50;
        final int TOTAL_COMMENTS = (int) commentsRepository.count();

        LinkedList<String> titles = new LinkedList<>(fetchFishTextAPI("title",  MAX_COMMENTS, "h1", false));

        while (!titles.isEmpty()) {
            // Set random post
            int randomCommentId = new Random().nextInt(TOTAL_COMMENTS) + 1;
            Comment parentComment = commentsRepository.findById(randomCommentId).get();

            Comment comment = new Comment();
            comment.setPost(parentComment.getPost());

            // Set random post
            int randomUserId = new Random().nextInt((int) usersRepository.count()) + 1;
            User user = usersRepository.findById(randomUserId).get();
            comment.setUser(user);

            // Set time [from postTime to now - postTime]
            Instant commentTime = parentComment.getPost().getTime().plus(new Random().nextInt(10), ChronoUnit.MINUTES);
            comment.setTime(commentTime);

            // Set text
            comment.setText(titles.poll());

            comment.setParentComment(parentComment);

            commentsRepository.save(comment);
        }

    }

    private void setLikes(boolean like) {
        final int TOTAL_USERS = (int) usersRepository.count();
        final int MAX_LIKES_PER_POST = 50;
        final float POST_LIKE_PROBABILITY = 0.5f;

        List<Post> acceptedPosts = postsRepository.findByModerationStatus(ModerationStatus.ACCEPTED);

        for (Post post : acceptedPosts) {
            if (Math.random() < POST_LIKE_PROBABILITY) {
                int numLikes = new Random().nextInt(MAX_LIKES_PER_POST);

                for (int i = 0; i < numLikes; i++) {
                    int randomUserId = new Random().nextInt(TOTAL_USERS) + 1;
                    User user = usersRepository.findById(randomUserId).get();

                    Vote vote = new Vote(user, post);

                    if (like) {
                        vote.like();
                    } else {
                        vote.dislike();
                    }

                    // Save ignoring SQLIntegrityConstraintViolationException will be raised
                    // in case of duplicate likes/dislikes
                    try {
                        votesRepository.save(vote);
                    } catch (Exception ignored){ }
                }
            }
        }
    }

    private List<String> fetchFishTextAPI(String type, int numItems, String cssQuery, boolean keepTags) {
        assert numItems > 0 && !type.isEmpty() && !type.equals("title") && !type.equals("paragraph");

        final String API_URL = String.format("https://fish-text.ru/get?type=%s&format=html&number=%d",
                type, numItems);

        try {
            Document document = Jsoup.connect(API_URL).get();

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
