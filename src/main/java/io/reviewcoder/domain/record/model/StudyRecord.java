package io.reviewcoder.domain.record.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "study_record",
        indexes = {
                @Index(name = "idx_record_user_problem", columnList = "user_id,problem_id"),
                @Index(name = "idx_record_problem_created", columnList = "problem_id,created_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "title_opt", length = 300)
    private String titleOpt;

    @Column(name = "content_markdown", columnDefinition = "mediumtext")
    private String contentMarkdown; //

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
