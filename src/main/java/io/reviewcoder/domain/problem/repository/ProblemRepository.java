package io.reviewcoder.domain.problem.repository;
import io.reviewcoder.domain.problem.model.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Page<Problem> findByUserId(Long userId, Pageable pageable);

    Page<Problem> findByUserIdAndTitleContainingIgnoreCase(Long userId, String titlePart, Pageable pageable);

    Page<Problem> findByUserIdAndBookmarkedTrue(Long userId, Pageable pageable);

}