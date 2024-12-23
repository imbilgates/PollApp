package com.bilgates.pollApp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;

    private String username;
    private String picture;
    private String email;

    @ElementCollection
    private List<OptionVote> options = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

}
