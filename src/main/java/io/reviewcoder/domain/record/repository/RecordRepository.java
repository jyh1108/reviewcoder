package io.reviewcoder.domain.record.repository;

import io.reviewcoder.domain.record.model.StudyRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<StudyRecord, Long> {

    // 문제별 타임라인(내 기록만)
    Page<StudyRecord> findByUserIdAndProblemId(Long userId, Long problemId, Pageable pageable);

    // 소유자 검증 단건 조회
    Optional<StudyRecord> findByIdAndUserId(Long id, Long userId);
}