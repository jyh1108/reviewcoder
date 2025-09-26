package io.reviewcoder.domain.record.service;
import io.reviewcoder.domain.record.dto.RecordCreateRequest;
import io.reviewcoder.domain.record.dto.RecordResponse;
import io.reviewcoder.domain.record.dto.RecordUpdateRequest;
import io.reviewcoder.domain.record.model.StudyRecord;
import io.reviewcoder.domain.record.repository.RecordRepository;
import io.reviewcoder.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final ProblemRepository problemRepository;

    private RecordResponse toResponse(StudyRecord r) {
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

    // --- 생성 ---
    @Transactional
    public RecordResponse create(Long userId, RecordCreateRequest req) {
        problemRepository.findById(req.getProblemId())
                .filter(p -> p.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Problem not found"));

        StudyRecord r = StudyRecord.builder()
                .userId(userId)
                .problemId(req.getProblemId())
                .titleOpt(req.getTitleOpt())
                .contentMarkdown(req.getContentMarkdown())
                .build();
        recordRepository.save(r);
        return toResponse(r);
    }

    // --- 단건 조회(소유자 검증) ---
    @Transactional(readOnly = true)
    public RecordResponse getOne(Long userId, Long recordId) {
        StudyRecord r = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
        return toResponse(r);
    }

    // --- 타임라인 목록(문제별) ---
    @Transactional(readOnly = true)
    public Page<RecordResponse> listByProblem(Long userId, Long problemId, Pageable pageable) {
        problemRepository.findById(problemId)
                .filter(p -> p.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Problem not found"));

        return recordRepository.findByUserIdAndProblemId(userId, problemId, pageable)
                .map(this::toResponse);
    }

    // --- 수정(부분) ---
    @Transactional
    public RecordResponse update(Long userId, Long recordId, RecordUpdateRequest req) {
        StudyRecord r = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        if (req.getTitleOpt() != null)       r.setTitleOpt(req.getTitleOpt());
        if (req.getContentMarkdown() != null) r.setContentMarkdown(req.getContentMarkdown());

        return toResponse(r);
    }

    // --- 삭제 ---
    @Transactional
    public void delete(Long userId, Long recordId) {
        StudyRecord r = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
        recordRepository.delete(r);
    }
}
