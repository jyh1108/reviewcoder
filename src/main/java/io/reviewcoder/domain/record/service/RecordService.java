package io.reviewcoder.domain.record.service;

import io.reviewcoder.domain.record.dto.RecordCreateRequest;
import io.reviewcoder.domain.record.dto.RecordResponse;
import io.reviewcoder.domain.record.dto.RecordUpdateRequest;
import io.reviewcoder.domain.record.mapper.RecordMapper;
import io.reviewcoder.domain.record.model.StudyRecord;
import io.reviewcoder.domain.record.repository.RecordRepository;
import io.reviewcoder.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static io.reviewcoder.domain.record.mapper.RecordMapper.*;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final ProblemRepository problemRepository;

    private void assertOwnedProblem(Long userId, Long problemId) {
        problemRepository.findById(problemId)
                .filter(p -> p.getUserId().equals(userId))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "문제를 찾을 수 없습니다."));
    }

    private StudyRecord getOwnedRecordOrThrow(Long userId, Long recordId) {
        return recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다."));
    }

    // 생성: createdAt/updatedAt 수동 세팅
    @Transactional
    public RecordResponse create(Long userId, RecordCreateRequest req) {
        assertOwnedProblem(userId, req.getProblemId());
        LocalDateTime now = LocalDateTime.now();

        StudyRecord r = toEntity(userId, req);
        r.setCreatedAt(now);
        r.setUpdatedAt(now);
        recordRepository.save(r);
        return toResponse(r);
    }

    @Transactional(readOnly = true)
    public RecordResponse getOne(Long userId, Long recordId) {
        return toResponse(getOwnedRecordOrThrow(userId, recordId));
    }

    @Transactional(readOnly = true)
    public Page<RecordResponse> listByProblem(Long userId, Long problemId, Pageable pageable) {
        assertOwnedProblem(userId, problemId);
        return recordRepository.findByUserIdAndProblemId(userId, problemId, pageable)
                .map(RecordMapper::toResponse);
    }

    // 수정: updatedAt 갱신
    @Transactional
    public RecordResponse update(Long userId, Long recordId, RecordUpdateRequest req) {
        StudyRecord r = getOwnedRecordOrThrow(userId, recordId);
        applyUpdate(r, req);
        r.setUpdatedAt(LocalDateTime.now());
        return toResponse(r);
    }

    @Transactional
    public void delete(Long userId, Long recordId) {
        recordRepository.delete(getOwnedRecordOrThrow(userId, recordId));
    }
}
