package io.reviewcoder.domain.problem.service;

import io.reviewcoder.domain.problem.dto.ProblemCreateRequest;
import io.reviewcoder.domain.problem.dto.ProblemResponse;
import io.reviewcoder.domain.problem.dto.ProblemUpdateRequest;
import io.reviewcoder.domain.problem.mapper.ProblemMapper;
import io.reviewcoder.domain.problem.model.Problem;
import io.reviewcoder.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static io.reviewcoder.domain.problem.mapper.ProblemMapper.*;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository repository;

    private Problem getOwnedProblemOrThrow(Long userId, Long id) {
        return repository.findById(id)
                .filter(p -> p.getUserId().equals(userId))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "문제를 찾을 수 없습니다."));
    }

    @Transactional
    public ProblemResponse create(Long userId, ProblemCreateRequest req) {
        LocalDateTime now = LocalDateTime.now();
        Problem p = toEntity(userId, req);
        p.setCreatedAt(now);
        p.setUpdatedAt(now);
        repository.save(p);
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public ProblemResponse getOne(Long userId, Long id) {
        return toResponse(getOwnedProblemOrThrow(userId, id));
    }

    @Transactional(readOnly = true)
    public Page<ProblemResponse> search(Long userId, String title, Pageable pageable) {
        Page<Problem> page = (title != null && !title.isBlank())
                ? repository.findByUserIdAndTitleContainingIgnoreCase(userId, title, pageable)
                : repository.findByUserId(userId, pageable);
        return page.map(ProblemMapper::toResponse);
    }

    @Transactional
    public ProblemResponse update(Long userId, Long id, ProblemUpdateRequest req) {
        Problem p = getOwnedProblemOrThrow(userId, id);
        applyUpdate(p, req);
        p.setUpdatedAt(LocalDateTime.now());
        return toResponse(p);
    }

    @Transactional
    public ProblemResponse setBookmark(Long userId, Long id, boolean on) {
        Problem p = getOwnedProblemOrThrow(userId, id);
        LocalDateTime now = LocalDateTime.now();
        p.setBookmarked(on);
        p.setBookmarkedAt(on ? now : null);
        p.setUpdatedAt(now);
        return toResponse(p);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        repository.delete(getOwnedProblemOrThrow(userId, id));
    }

    @Transactional(readOnly = true)
    public Page<ProblemResponse> listBookmarked(Long userId, Pageable pageable) {
        return repository.findByUserIdAndBookmarkedTrue(userId, pageable)
                .map(ProblemMapper::toResponse);
    }
}
