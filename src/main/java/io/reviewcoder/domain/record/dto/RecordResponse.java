package io.reviewcoder.domain.record.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecordResponse {
    private final Long id;
    private final Long userId;
    private final Long problemId;
    private final String titleOpt;
    private final String contentMarkdown;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
