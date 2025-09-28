package io.reviewcoder.domain.problem.dto;

import io.reviewcoder.domain.problem.model.Problem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCreateRequest {

    @NotBlank
    @Size(max = 300)
    private String title;

    @Size(max = 1000)
    private String sourceUrl;

    @NotNull
    private Problem.Difficulty difficulty;   // E | M | H

    @NotNull
    private Problem.ProblemStatus status;    // UNSOLVED | SOLVED | REVIEW_NEEDED

    private String memo;

    @Size(max = 500)
    private String tag;

    private LocalDateTime nextReviewAt;
}
