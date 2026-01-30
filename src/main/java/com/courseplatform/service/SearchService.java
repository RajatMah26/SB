package com.courseplatform.service;

import com.courseplatform.dto.SearchResponse;
import com.courseplatform.entity.Course;
import com.courseplatform.entity.Subtopic;
import com.courseplatform.entity.Topic;
import com.courseplatform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public SearchResponse search(String query) {
        List<Course> courses = courseRepository.searchCourses(query);

        List<SearchResponse.SearchResult> results = courses.stream()
                .map(course -> buildSearchResult(course, query))
                .filter(result -> !result.getMatches().isEmpty())
                .collect(Collectors.toList());

        return SearchResponse.builder()
                .query(query)
                .results(results)
                .build();
    }

    private SearchResponse.SearchResult buildSearchResult(Course course, String query) {
        List<SearchResponse.Match> matches = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        // Check course title and description
        if (course.getTitle().toLowerCase().contains(lowerQuery)) {
            matches.add(SearchResponse.Match.builder()
                    .type("course")
                    .snippet(course.getTitle())
                    .build());
        }

        if (course.getDescription() != null && course.getDescription().toLowerCase().contains(lowerQuery)) {
            matches.add(SearchResponse.Match.builder()
                    .type("course")
                    .snippet(createSnippet(course.getDescription(), query))
                    .build());
        }

        // Check topics and subtopics
        for (Topic topic : course.getTopics()) {
            if (topic.getTitle().toLowerCase().contains(lowerQuery)) {
                matches.add(SearchResponse.Match.builder()
                        .type("topic")
                        .topicTitle(topic.getTitle())
                        .snippet(topic.getTitle())
                        .build());
            }

            for (Subtopic subtopic : topic.getSubtopics()) {
                if (subtopic.getTitle().toLowerCase().contains(lowerQuery)) {
                    matches.add(SearchResponse.Match.builder()
                            .type("subtopic")
                            .topicTitle(topic.getTitle())
                            .subtopicId(subtopic.getId())
                            .subtopicTitle(subtopic.getTitle())
                            .snippet(subtopic.getTitle())
                            .build());
                }

                if (subtopic.getContent() != null && subtopic.getContent().toLowerCase().contains(lowerQuery)) {
                    matches.add(SearchResponse.Match.builder()
                            .type("content")
                            .topicTitle(topic.getTitle())
                            .subtopicId(subtopic.getId())
                            .subtopicTitle(subtopic.getTitle())
                            .snippet(createSnippet(subtopic.getContent(), query))
                            .build());
                }
            }
        }

        return SearchResponse.SearchResult.builder()
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .matches(matches)
                .build();
    }

    private String createSnippet(String text, String query) {
        int maxSnippetLength = 150;
        String lowerText = text.toLowerCase();
        String lowerQuery = query.toLowerCase();

        int index = lowerText.indexOf(lowerQuery);
        if (index == -1) {
            return text.length() > maxSnippetLength ? text.substring(0, maxSnippetLength) + "..." : text;
        }

        int start = Math.max(0, index - 50);
        int end = Math.min(text.length(), index + query.length() + 50);

        String snippet = text.substring(start, end);
        if (start > 0)
            snippet = "..." + snippet;
        if (end < text.length())
            snippet = snippet + "...";

        return snippet;
    }
}
