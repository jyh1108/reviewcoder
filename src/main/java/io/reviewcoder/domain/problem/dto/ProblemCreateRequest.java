package io.reviewcoder.domain.problem.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Size(max = 16)
    private String difficulty; // "E" | "M" | "H"

    @NotBlank
    @Size(max = 20)
    private String status; // "UNSOLVED" | "SOLVED" | "REVIEW_NEEDED"

    private String memo;

    @Size(max = 500)
    private String tag;


    private LocalDateTime nextReviewAt;
}
