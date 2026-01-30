package com.courseplatform.util;

import com.courseplatform.entity.Course;
import com.courseplatform.entity.Subtopic;
import com.courseplatform.entity.Topic;
import com.courseplatform.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataLoader implements ApplicationRunner {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // Check if database is empty
        if (courseRepository.count() > 0) {
            log.info("Database already contains data. Skipping seed data loading.");
            return;
        }

        log.info("Database is empty. Loading seed data...");

        try {
            List<CourseData> courseDataList = loadCoursesFromJson();

            for (CourseData courseData : courseDataList) {
                Course course = mapToCourse(courseData);
                courseRepository.save(course);
                log.info("Loaded course: {}", course.getTitle());
            }

            log.info("Seed data loading completed successfully. Loaded {} courses.", courseDataList.size());
        } catch (Exception e) {
            log.error("Error loading seed data", e);
            throw e;
        }
    }

    private List<CourseData> loadCoursesFromJson() throws IOException {
        ClassPathResource resource = new ClassPathResource("seed-data/courses.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<CourseData>>() {
            });
        }
    }

    private Course mapToCourse(CourseData courseData) {
        Course course = Course.builder()
                .id(courseData.getId())
                .title(courseData.getTitle())
                .description(courseData.getDescription())
                .build();

        for (TopicData topicData : courseData.getTopics()) {
            Topic topic = Topic.builder()
                    .id(topicData.getId())
                    .title(topicData.getTitle())
                    .course(course)
                    .build();

            for (SubtopicData subtopicData : topicData.getSubtopics()) {
                Subtopic subtopic = Subtopic.builder()
                        .id(subtopicData.getId())
                        .title(subtopicData.getTitle())
                        .content(subtopicData.getContent())
                        .topic(topic)
                        .build();

                topic.getSubtopics().add(subtopic);
            }

            course.getTopics().add(topic);
        }

        return course;
    }

    // Inner classes for JSON deserialization
    @lombok.Data
    private static class CourseData {
        private String id;
        private String title;
        private String description;
        private List<TopicData> topics;
    }

    @lombok.Data
    private static class TopicData {
        private String id;
        private String title;
        private List<SubtopicData> subtopics;
    }

    @lombok.Data
    private static class SubtopicData {
        private String id;
        private String title;
        private String content;
    }
}
