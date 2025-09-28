package io.reviewcoder.domain.problem.mapper;

import io.reviewcoder.domain.problem.dto.*;
import io.reviewcoder.domain.problem.model.Problem;
import io.reviewcoder.domain.problem.util.TagUtils;

public final class ProblemMapper {
    private ProblemMapper() {}

    public static Problem toEntity(Long userId, ProblemCreateRequest req) {
        return Problem.builder()
                .userId(userId)
                .title(req.getTitle())
                .sourceUrl(req.getSourceUrl())
                .difficulty(req.getDifficulty())
                .status(req.getStatus())
                .memo(req.getMemo())
                .bookmarked(false)
                .bookmarkedAt(null)
                .nextReviewAt(req.getNextReviewAt())
                .tag(TagUtils.normalize(req.getTag()))
                .build();
    }

    public static ProblemResponse toResponse(Problem p) {
        return ProblemResponse.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .title(p.getTitle())
                .sourceUrl(p.getSourceUrl())
                .difficulty(p.getDifficulty().name())
                .status(p.getStatus().name())
                .memo(p.getMemo())
                .bookmarked(p.isBookmarked())
                .bookmarkedAt(p.getBookmarkedAt())
                .nextReviewAt(p.getNextReviewAt())
                .tag(p.getTag())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    public static void applyUpdate(Problem target, ProblemUpdateRequest req) {
        if (req.getTitle() != null)       target.setTitle(req.getTitle());
        if (req.getSourceUrl() != null)   target.setSourceUrl(req.getSourceUrl());
        if (req.getDifficulty() != null)  target.setDifficulty(req.getDifficulty());
        if (req.getStatus() != null)      target.setStatus(req.getStatus());
        if (req.getMemo() != null)        target.setMemo(req.getMemo());
        if (req.getTag() != null)         target.setTag(TagUtils.normalize(req.getTag()));
        target.setNextReviewAt(req.getNextReviewAt());
    }
}
