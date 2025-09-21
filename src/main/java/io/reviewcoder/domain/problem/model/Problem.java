package io.reviewcoder.domain.problem.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "problem",
        indexes = {
                @Index(name = "idx_problem_user_status", columnList = "user_id,status"),
                @Index(name = "idx_problem_user_bookmark", columnList = "user_id,bookmarked"),
                @Index(name = "idx_problem_nextreview", columnList = "user_id,next_review_at")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 300, nullable = false)
    private String title;

    @Column(name = "source_url", length = 1000)
    private String sourceUrl;

    // ENUM 대신 문자열(간단 버전)
    @Column(length = 16, nullable = false)
    private String difficulty; // "E" | "M" | "H"

    @Column(length = 20, nullable = false)
    private String status; // "UNSOLVED" | "SOLVED" | "REVIEW_NEEDED"

    @Column(name = "memo", columnDefinition = "text")
    private String memo;

    @Column(name = "bookmarked", nullable = false)
    private boolean bookmarked;

    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;

    @Column(name = "tag", length = 10)   // 최대 10자
    private String tag;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}