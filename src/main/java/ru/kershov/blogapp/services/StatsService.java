package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.dto.StatsDTO;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.VotesRepository;

@Service
public class StatsService {
    @Autowired
    PostsRepository postsRepository;

    @Autowired
    VotesRepository votesRepository;

    public StatsDTO getStats() {
        StatsDTO stats = new StatsDTO();

        stats.setPostsCount(postsRepository.count());

        // TODO: Refactor: byte >>> Enum.Votes.LIKE/Dislike + Entity + Migrate DB
        stats.setLikesCount(votesRepository.getVotes((byte) 1));
        stats.setDislikesCount(votesRepository.getVotes((byte) -1));

        stats.setViewsCount(postsRepository.getViews());
        stats.setFirstPublication(postsRepository.getFirstPostPublicationDate());

        return stats;
    }
}
