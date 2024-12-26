package com.bilgates.pollApp.Model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Embeddable
public class OptionVote {
    private String voteOption;
    private Long voteCount = 0L;

    private Set<String> voters = new HashSet<>();
}
