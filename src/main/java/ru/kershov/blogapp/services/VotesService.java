package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.Post;
import ru.kershov.blogapp.model.User;
import ru.kershov.blogapp.model.Vote;
import ru.kershov.blogapp.repositories.PostsRepository;
import ru.kershov.blogapp.repositories.VotesRepository;
import ru.kershov.blogapp.utils.APIResponse;

@Service
public class VotesService {
    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private VotesRepository votesRepository;

    @Autowired
    private PostsRepository postsRepository;

    public ResponseEntity<?> vote(String voteType, User user, Post post) {
        if (post == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error());

        byte voteRequested = voteType.equalsIgnoreCase("like") ? (byte) 1 : (byte) -1;
        Vote vote = votesRepository.findByUserAndPost(user, post);

        // 1) User hasn't voted yet, so new vote is created
        if (vote == null) {
            Vote newVote = new Vote(user, post);
            newVote.setValue(voteRequested);
            votesRepository.save(newVote);

            return ResponseEntity.ok(APIResponse.ok());
        }

        // 2) User has already voted:

        //      ...and tries to double vote
        if (voteRequested == vote.getValue())
            return ResponseEntity.ok(APIResponse.error());

        //      ...and changed he/she's mind
        // Delete current vote
        votesRepository.delete(vote);

        // Create new one with the opposite value
        Vote newVote = new Vote(user, post);
        newVote.setValue(voteRequested);
        votesRepository.save(newVote);

        return ResponseEntity.ok(APIResponse.ok());
    }
}
