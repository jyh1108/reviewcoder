package io.reviewcoder.domain.record.controller;

import io.reviewcoder.domain.record.dto.*;
import io.reviewcoder.domain.record.service.RecordService;
import io.reviewcoder.global.auth.CurrentUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService service;
    private final CurrentUserProvider currentUser;

    @PostMapping("/records")
    public ResponseEntity<RecordResponse> create(Principal principal,
                                                 @Valid @RequestBody RecordCreateRequest req) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.create(userId, req));
    }

    @GetMapping("/problems/{problemId}/records")
    public ResponseEntity<Page<RecordResponse>> listByProblem(Principal principal,
                                                              @PathVariable Long problemId,
                                                              @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.listByProblem(userId, problemId, pageable));
    }

    @GetMapping("/records/{recordId}")
    public ResponseEntity<RecordResponse> getOne(Principal principal,
                                                 @PathVariable Long recordId) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.getOne(userId, recordId));
    }

    @PatchMapping("/records/{recordId}")
    public ResponseEntity<RecordResponse> update(Principal principal,
                                                 @PathVariable Long recordId,
                                                 @Valid @RequestBody RecordUpdateRequest req) {
        Long userId = currentUser.id(principal);
        return ResponseEntity.ok(service.update(userId, recordId, req));
    }

    @DeleteMapping("/records/{recordId}")
    public ResponseEntity<Void> delete(Principal principal,
                                       @PathVariable Long recordId) {
        Long userId = currentUser.id(principal);
        service.delete(userId, recordId);
        return ResponseEntity.noContent().build();
    }
}
