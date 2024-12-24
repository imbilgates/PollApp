package com.bilgates.pollApp.Controller;

import com.bilgates.pollApp.Model.Poll;
import com.bilgates.pollApp.Request.Vote;
import com.bilgates.pollApp.Service.PollService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "https://pollwebapp.netlify.app")
@RestController
@RequestMapping("/api/polls")


public class PollController {

    private final PollService pollService;


    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping
    public Poll createPoll(@RequestBody Poll poll, Authentication authentication){
        return pollService.createPoll(poll, authentication);
    }

    @GetMapping
    public List<Poll> getAllPolls(){
        return pollService.getAllPolls();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPollById(@PathVariable Long id){
        return pollService.getPollById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    POST VOTE
    @PostMapping("/vote")
    public void vote(@RequestBody Vote vote){
        pollService.vote(vote.getPollId(), vote.getOptionIndex());
    }


    @GetMapping("/by-email")
    public ResponseEntity<List<Poll>> getPollsByEmail(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            String authenticatedEmail = (String) principal.getAttribute("email");

            // Fetch polls created by the authenticated user
            List<Poll> polls = pollService.getPollsByEmail(authenticatedEmail);
            return ResponseEntity.ok(polls);
        }

        // Return 401 Unauthorized if authentication fails
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/by-email/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        Optional<Poll> poll = pollService.getPollById(id);
        if (poll.isPresent()) {
            pollService.deletePoll(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content when successfully deleted
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found if poll doesn't exist
    }



}
