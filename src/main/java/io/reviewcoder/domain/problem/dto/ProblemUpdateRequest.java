package io.reviewcoder.domain.problem.dto;


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

    @Size(max = 16)
    private String difficulty; // "E" | "M" | "H"

    @Size(max = 20)
    private String status; // "UNSOLVED" | "SOLVED" | "REVIEW_NEEDED"

    private String memo;

    private String tag;

    private LocalDateTime nextReviewAt;
}