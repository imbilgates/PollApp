package com.bilgates.pollApp.Repository;

import com.bilgates.pollApp.Model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
        List<Poll> findByEmail(String email);
}
