package com.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailResponse {
    private String id;
    private String title;
    private String description;
    private List<TopicDto> topics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicDto {
        private String id;
        private String title;
        private List<SubtopicDto> subtopics;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubtopicDto {
        private String id;
        private String title;
        private String content;
    }
}
