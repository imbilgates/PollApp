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
@CrossOrigin(origins = "${FRONTEND_URL}")
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

    @PostMapping("/vote")
    public ResponseEntity<String> vote(@RequestBody Vote vote, Authentication authentication) {

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();

            String username = null;
            String picture = null;
            String email = null;

            // Check if it's Google
            if (principal.getAttributes().containsKey("name")) {
                username = (String) principal.getAttribute("name");
            }
            // Check if it's GitHub
            else if (principal.getAttributes().containsKey("login")) {
                username = (String) principal.getAttribute("login");
            }

            // Optionally retrieve email and picture
            picture = (String) principal.getAttribute("picture");
            email = (String) principal.getAttribute("email");

            if (username != null) {
                pollService.vote(vote.getPollId(), vote.getOptionIndex(), username);
                return ResponseEntity.ok("Vote registered successfully");
            } else {
                return ResponseEntity.badRequest().body("Username not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }

    @GetMapping("/by-email")
    public ResponseEntity<List<Poll>> getPollsByEmail(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            String authenticatedEmail = (String) principal.getAttribute("email");


            List<Poll> polls = pollService.getPollsByEmail(authenticatedEmail);
            return ResponseEntity.ok(polls);
        }
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
