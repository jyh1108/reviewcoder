package io.reviewcoder.domain.problem.controller;


import io.reviewcoder.domain.problem.dto.ProblemCreateRequest;
import io.reviewcoder.domain.problem.dto.ProblemResponse;
import io.reviewcoder.domain.problem.dto.ProblemUpdateRequest;
import io.reviewcoder.domain.problem.service.ProblemService;
import io.reviewcoder.global.auth.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService service;
    private final CurrentUser currentUser;

    // 생성
    @PostMapping
    public ResponseEntity<ProblemResponse> create(Principal principal,
                                                  @Valid @RequestBody ProblemCreateRequest req) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.create(userId, req));
    }

    // 상세
    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> detail(Principal principal,
                                                  @PathVariable Long id) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.getOne(userId, id));
    }

    // 목록 + 제목 검색만
    @GetMapping
    public ResponseEntity<Page<ProblemResponse>> list(
            Principal principal,
            @RequestParam(required = false) String title,   // ← 제목 부분일치 검색
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.search(userId, title, pageable));
    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ProblemResponse> update(Principal principal,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody ProblemUpdateRequest req) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.update(userId, id, req));
    }

    // 북마크
    @PatchMapping("/{id}/bookmark")
    public ResponseEntity<ProblemResponse> bookmark(Principal principal,
                                                    @PathVariable Long id,
                                                    @RequestParam boolean on) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.setBookmark(userId, id, on));
    }

    @GetMapping("/bookmarked")
    public ResponseEntity<Page<ProblemResponse>> listBookmarked(
            Principal principal,
            @org.springframework.data.web.PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.listBookmarked(userId, pageable));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable Long id) {
        Long userId = currentUser.id(principal);
        service.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}