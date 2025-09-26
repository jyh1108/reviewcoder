package io.reviewcoder.domain.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordCreateRequest {

    @NotNull
    private Long problemId;

    @Size(max = 300)
    private String titleOpt;

    @NotBlank
    @Size(max = 20000)
    private String contentMarkdown;
}
