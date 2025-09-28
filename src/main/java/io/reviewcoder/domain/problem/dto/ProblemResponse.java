package io.reviewcoder.domain.problem.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
public class ProblemResponse {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String sourceUrl;
    private final String difficulty;   // "E" | "M" | "H"
    private final String status;       // "UNSOLVED" | "SOLVED" | "REVIEW_NEEDED"
    private final String memo;
    private final boolean bookmarked;
    private final LocalDateTime bookmarkedAt;
    private final LocalDateTime nextReviewAt;
    private final String tag;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}