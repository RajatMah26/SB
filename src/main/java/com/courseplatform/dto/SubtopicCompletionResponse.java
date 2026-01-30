package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubtopicCompletionResponse {
    private String subtopicId;
    private Boolean completed;
    private LocalDateTime completedAt;
}
