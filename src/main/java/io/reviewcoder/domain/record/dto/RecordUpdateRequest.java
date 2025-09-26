package io.reviewcoder.domain.record.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordUpdateRequest {

    @Size(max = 300)
    private String titleOpt;

    @Size(max = 20000)
    private String contentMarkdown;
}
