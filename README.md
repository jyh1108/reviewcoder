# ReviewCoder API
## 프로젝트 설명

알고리즘 문제를 북마크하고, 시도할 때마다 학습 기록(풀이/회고/코드 스냅샷)을 남기며, 복습 필요 문제는 알림으로 다시 보게 해주는 RESTful 백엔드입니다.
검색은 제목, 정렬/필터는 태그·난이도·상태·북마크 중심으로 제공합니다.
통계도 제공합니다.

## 프로젝트 기능

- 회원/인증

  - 회원가입, 로그인

  - JWT 기반 인증(무상태), 사용자별 데이터 스코프 분리

  - 이메일 인증 코드: Redis 저장 + TTL

- 문제 관리

  - 문제 등록/수정/삭제: title, sourceUrl, difficulty(E/M/H), status(UNSOLVED/SOLVED/REVIEW_NEEDED), memo, bookmarked

  - 문제 목록 조회 + 필터(태그/난이도/상태/북마크) + 정렬/페이징

  - 검색: 제목

  - 북마크 토글

  - 복습 예정 시각(next_review_at) 관리 (기본 정책: REVIEW_NEEDED/UNSOLVED 선택 시 +3일)

- 태그

  - 사용자별 태그 사전 관리(중복 금지)

  - 문제–태그 N:M 연결/해제

  - 태그별 필터/집계

- 학습 기록 히스토리

  - 문제별 시도 기록(타임라인) 생성/조회/수정/삭제

  - contentMarkdown(회고/풀이), codeLang, codeText(코드 스냅샷) 저장

  - (시간 여유시) 첨부파일(S3) 연결

- 첨부(S3)

  - Presigned URL 업로드/다운로드

  - DB에는 메타데이터만 저장(s3_key, mime, size_bytes)

- 복습 알림

  - 스케줄러가 next_review_at <= now 문제를 조회 → 중복 방지 로그(notification_log)로 멱등 보장

  - 채널: INAPP/EMAIL/PUSH

- 통계

  -  월 집계(stats_monthly): solved/unsolved/review_needed 개수 + per_tag_json

  - 현재월은 원본에서 즉석 계산/보정

- 보안

  - 모든 리소스는 본인 소유(user_id)만 접근 가능

  - 입력 검증(Validation), 예외 처리 표준화
 
## ERD
<img width="1417" height="690" alt="image" src="https://github.com/user-attachments/assets/e68e5852-418b-4de0-aeb6-91cfb30cf7d8" />

## 사용 기술 (Tech Stack)

- Language: Java 17
- Framework: Spring Boot 3.5.5 (Web, Validation, Security, Data JPA, Data Redis, Mail(optional), Actuator(optional))
- DB: MySQL 8.0 / Flyway(스키마 마이그레이션)
- Cache/Queue: Redis(이메일 인증, 알림 스케줄링 보조)
- Auth: Spring Security + JWT
- Storage: AWS S3(첨부파일, 선택)
- Docs: springdoc-openapi (Swagger UI)
- Build/Dev: Gradle, Lombok
- Test: JUnit 5, Spring Security Test
