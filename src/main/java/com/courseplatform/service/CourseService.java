package com.courseplatform.service;

import com.courseplatform.dto.CourseDetailResponse;
import com.courseplatform.dto.CourseListResponse;
import com.courseplatform.dto.CoursesResponse;
import com.courseplatform.entity.Course;
import com.courseplatform.entity.Topic;
import com.courseplatform.exception.ResourceNotFoundException;
import com.courseplatform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public CoursesResponse getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        List<CourseListResponse> courseList = courses.stream()
                .map(this::mapToCourseListResponse)
                .collect(Collectors.toList());

        return CoursesResponse.builder()
                .courses(courseList)
                .build();
    }

    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id '" + id + "' does not exist"));

        return mapToCourseDetailResponse(course);
    }

    private CourseListResponse mapToCourseListResponse(Course course) {
        int topicCount = course.getTopics().size();
        int subtopicCount = course.getTopics().stream()
                .mapToInt(topic -> topic.getSubtopics().size())
                .sum();

        return CourseListResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .topicCount(topicCount)
                .subtopicCount(subtopicCount)
                .build();
    }

    private CourseDetailResponse mapToCourseDetailResponse(Course course) {
        List<CourseDetailResponse.TopicDto> topics = course.getTopics().stream()
                .map(this::mapToTopicDto)
                .collect(Collectors.toList());

        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .topics(topics)
                .build();
    }

    private CourseDetailResponse.TopicDto mapToTopicDto(Topic topic) {
        List<CourseDetailResponse.SubtopicDto> subtopics = topic.getSubtopics().stream()
                .map(subtopic -> CourseDetailResponse.SubtopicDto.builder()
                        .id(subtopic.getId())
                        .title(subtopic.getTitle())
                        .content(subtopic.getContent())
                        .build())
                .collect(Collectors.toList());

        return CourseDetailResponse.TopicDto.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .subtopics(subtopics)
                .build();
    }
}
