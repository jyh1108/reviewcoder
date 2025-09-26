package io.reviewcoder.domain.problem.dto;


import io.reviewcoder.domain.problem.model.Problem;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemUpdateRequest {

    @Size(max = 300)
    private String title;

    @Size(max = 1000)
    private String sourceUrl;

    private Problem.Difficulty difficulty;       

    private Problem.ProblemStatus status;

    private String memo;

    @Size(max = 500)
    private String tag;

    private LocalDateTime nextReviewAt;
}