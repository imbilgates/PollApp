package com.bilgates.pollApp.Service;

import com.bilgates.pollApp.Model.OptionVote;
import com.bilgates.pollApp.Model.Poll;
import com.bilgates.pollApp.Repository.PollRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PollService {


    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }


    public Poll createPoll(Poll poll, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();

            String username = null;
            String picture = null;
            String email = null;

            // Check if it's Google
            if (principal.getAttributes().containsKey("name") && principal.getAttributes().containsKey("picture") && principal.getAttributes().containsKey("email")) {
                username = (String) principal.getAttribute("name");
                picture = (String) principal.getAttribute("picture");
                email = (String) principal.getAttribute("email");
            }
            // Check if it's GitHub
            else if (principal.getAttributes().containsKey("login") && principal.getAttributes().containsKey("avatar_url") && principal.getAttributes().containsKey("email")) {
                username = (String) principal.getAttribute("login");
                picture = (String) principal.getAttribute("avatar_url");
                email = (String) principal.getAttribute("email");
            }

            // Set the username, picture, and email in the poll
            poll.setUsername(username);
            poll.setPicture(picture);
            poll.setEmail(email);
        }

        return pollRepository.save(poll);
    }



    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public Optional<Poll> getPollById(Long id) {
        return pollRepository.findById(id);
    }

    public List<Poll> getPollsByEmail(String email) {
        return pollRepository.findByEmail(email);
    }

    public void vote(Long pollId, int optionIndex, String username) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(
                () -> new RuntimeException("Poll Not Found")
        );

        List<OptionVote> options = poll.getOptions();

        if (optionIndex < 0 || optionIndex >= options.size()) {
            throw new IllegalArgumentException("Invalid Option Index");
        }

        OptionVote selectedOption = options.get(optionIndex);

        if (selectedOption.getVoters().contains(username)) {
            // Unvote
            selectedOption.getVoters().remove(username);
            selectedOption.setVoteCount(selectedOption.getVoteCount() - 1);
        } else {
            // Vote
            selectedOption.getVoters().add(username);
            selectedOption.setVoteCount(selectedOption.getVoteCount() + 1);
        }

        pollRepository.save(poll);
    }


    public void deletePoll(Long id) {
        pollRepository.deleteById(id);
    }

}
