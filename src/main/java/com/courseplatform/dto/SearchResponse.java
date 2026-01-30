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
public class SearchResponse {
    private String query;
    private List<SearchResult> results;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResult {
        private String courseId;
        private String courseTitle;
        private List<Match> matches;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Match {
        private String type; // "course", "topic", "subtopic", "content"
        private String topicTitle;
        private String subtopicId;
        private String subtopicTitle;
        private String snippet;
    }
}
