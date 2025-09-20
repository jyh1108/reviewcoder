package io.reviewcoder.domain.problem.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.reviewcoder.domain.problem.dto.ProblemCreateRequest;
import io.reviewcoder.domain.problem.dto.ProblemResponse;
import io.reviewcoder.domain.problem.dto.ProblemUpdateRequest;
import io.reviewcoder.domain.problem.model.Problem;
import io.reviewcoder.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository repository;
    private final ObjectMapper objectMapper;

    // --- 태그---
    private String toJsonTag(String tag) {
        try {
            if (tag == null || tag.isBlank()) return null; // 비우면 DB NULL 저장
            String t = tag.trim();
            if (t.length() > 10) throw new IllegalArgumentException("TAG_TOO_LONG(<=10): " + t);
            return objectMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid tag", e);
        }
    }
    private String fromJsonTag(String json) {
        try {
            if (json == null || json.isBlank()) return null;
            return objectMapper.readValue(json, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // --- 엔티티 → 응답 DTO ---
    private ProblemResponse toResponse(Problem p) {
        return ProblemResponse.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .title(p.getTitle())
                .sourceUrl(p.getSourceUrl())
                .difficulty(p.getDifficulty())
                .status(p.getStatus())
                .memo(p.getMemo())
                .bookmarked(p.isBookmarked())
                .nextReviewAt(p.getNextReviewAt())
                .tag(fromJsonTag(p.getTagJson()))
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    // --- 생성 ---
    @Transactional
    public ProblemResponse create(Long userId, ProblemCreateRequest req) {
        Problem p = Problem.builder()
                .userId(userId)
                .title(req.getTitle())
                .sourceUrl(req.getSourceUrl())
                .difficulty(req.getDifficulty())
                .status(req.getStatus())
                .memo(req.getMemo())
                .bookmarked(false)
                .nextReviewAt(req.getNextReviewAt())
                .tagJson(toJsonTag(req.getTag()))
                .build();
        repository.save(p);
        return toResponse(p);
    }

    // --- 상세 ---
    @Transactional(readOnly = true)
    public ProblemResponse getOne(Long userId, Long id) {
        Problem p = repository.findById(id)
                .filter(it -> it.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Problem not found"));
        return toResponse(p);
    }

    // --- 제목 부분일치(대소문자 무시) ---
    @Transactional(readOnly = true)
    public Page<ProblemResponse> search(Long userId, String title, Pageable pageable) {
        Page<Problem> page = (title != null && !title.isBlank())
                ? repository.findByUserIdAndTitleContainingIgnoreCase(userId, title, pageable)
                : repository.findByUserId(userId, pageable);
        return page.map(this::toResponse);
    }

    // --- 수정(부분) ---
    @Transactional
    public ProblemResponse update(Long userId, Long id, ProblemUpdateRequest req) {
        Problem p = repository.findById(id)
                .filter(it -> it.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Problem not found"));

        if (req.getTitle() != null)      p.setTitle(req.getTitle());
        if (req.getSourceUrl() != null)  p.setSourceUrl(req.getSourceUrl());
        if (req.getDifficulty() != null) p.setDifficulty(req.getDifficulty());
        if (req.getStatus() != null)     p.setStatus(req.getStatus());
        if (req.getMemo() != null)       p.setMemo(req.getMemo());
        if (req.getTag() != null)        p.setTagJson(toJsonTag(req.getTag()));
        p.setNextReviewAt(req.getNextReviewAt());

        return toResponse(p);
    }

    // --- 북마크 ---
    @Transactional
    public ProblemResponse setBookmark(Long userId, Long id, boolean on) {
        Problem p = repository.findById(id)
                .filter(it -> it.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Problem not found"));
        p.setBookmarked(on);
        return toResponse(p);
    }

    // --- 삭제 ---
    @Transactional
    public void delete(Long userId, Long id) {
        Problem p = repository.findById(id)
                .filter(it -> it.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Problem not found"));
        repository.delete(p);
    }

    @Transactional(readOnly = true)
    public Page<ProblemResponse> listBookmarked(Long userId, Pageable pageable) {
        return repository.findByUserIdAndBookmarkedTrue(userId, pageable)
                .map(this::toResponse);
    }
}