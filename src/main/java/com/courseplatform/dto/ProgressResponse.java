package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponse {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private int totalSubtopics;
    private int completedSubtopics;
    private double completionPercentage;
    private List<CompletedItem> completedItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompletedItem {
        private String subtopicId;
        private String subtopicTitle;
        private LocalDateTime completedAt;
    }
}
