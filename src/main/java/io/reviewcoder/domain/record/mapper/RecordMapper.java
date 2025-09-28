package io.reviewcoder.domain.record.mapper;

import io.reviewcoder.domain.record.dto.*;
import io.reviewcoder.domain.record.model.StudyRecord;

public final class RecordMapper {
    private RecordMapper() {}

    public static StudyRecord toEntity(Long userId, RecordCreateRequest req) {
        return StudyRecord.builder()
                .userId(userId)
                .problemId(req.getProblemId())
                .titleOpt(req.getTitleOpt())
                .contentMarkdown(req.getContentMarkdown())
                .build();
    }

    public static RecordResponse toResponse(StudyRecord r) {
        return RecordResponse.builder()
                .id(r.getId())
                .userId(r.getUserId())
                .problemId(r.getProblemId())
                .titleOpt(r.getTitleOpt())
                .contentMarkdown(r.getContentMarkdown())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }

    public static void applyUpdate(StudyRecord r, RecordUpdateRequest req) {
        if (req.getTitleOpt() != null)         r.setTitleOpt(req.getTitleOpt());
        if (req.getContentMarkdown() != null)  r.setContentMarkdown(req.getContentMarkdown());
    }
}
