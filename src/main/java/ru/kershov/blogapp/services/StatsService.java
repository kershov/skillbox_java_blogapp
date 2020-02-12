package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.dto.StatsDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.VotesRepository;

@Service
public class StatsService {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private VotesRepository votesRepository;

    public StatsDTO getStats(User user) {
        StatsDTO stats = new StatsDTO();

        stats.setPostsCount(postsRepository.countByAuthor(user));
        stats.setLikesCount(votesRepository.countByUserAndValue(user, (byte) 1));
        stats.setDislikesCount(votesRepository.countByUserAndValue(user, (byte) -1));
        stats.setViewsCount(postsRepository.getViewsByUser(user));
        stats.setFirstPublication(postsRepository.getFirstPostDateByUser(user));

        return stats;
    }
}
