package com.courseplatform.service;

import com.courseplatform.dto.EnrollmentResponse;
import com.courseplatform.dto.ProgressResponse;
import com.courseplatform.entity.Course;
import com.courseplatform.entity.Enrollment;
import com.courseplatform.entity.SubtopicProgress;
import com.courseplatform.entity.User;
import com.courseplatform.exception.DuplicateEnrollmentException;
import com.courseplatform.exception.ResourceNotFoundException;
import com.courseplatform.repository.CourseRepository;
import com.courseplatform.repository.EnrollmentRepository;
import com.courseplatform.repository.SubtopicProgressRepository;
import com.courseplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubtopicProgressRepository subtopicProgressRepository;

    @Transactional
    public EnrollmentResponse enrollInCourse(String courseId) {
        User user = getCurrentUser();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id '" + courseId + "' does not exist"));

        // Check for duplicate enrollment
        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new DuplicateEnrollmentException("You are already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return EnrollmentResponse.builder()
                .enrollmentId(savedEnrollment.getId())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .enrolledAt(savedEnrollment.getEnrolledAt())
                .build();
    }

    @Transactional(readOnly = true)
    public ProgressResponse getProgress(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment with id '" + enrollmentId + "' does not exist"));

        User currentUser = getCurrentUser();
        if (!enrollment.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Enrollment with id '" + enrollmentId + "' does not exist");
        }

        Course course = enrollment.getCourse();

        // Calculate total subtopics
        int totalSubtopics = course.getTopics().stream()
                .mapToInt(topic -> topic.getSubtopics().size())
                .sum();

        // Get completed subtopics
        List<SubtopicProgress> completedProgress = subtopicProgressRepository.findByEnrollmentId(enrollmentId)
                .stream()
                .filter(SubtopicProgress::getCompleted)
                .collect(Collectors.toList());

        int completedCount = completedProgress.size();
        double completionPercentage = totalSubtopics > 0 ? (double) completedCount / totalSubtopics * 100 : 0.0;

        List<ProgressResponse.CompletedItem> completedItems = completedProgress.stream()
                .map(progress -> ProgressResponse.CompletedItem.builder()
                        .subtopicId(progress.getSubtopic().getId())
                        .subtopicTitle(progress.getSubtopic().getTitle())
                        .completedAt(progress.getCompletedAt())
                        .build())
                .collect(Collectors.toList());

        return ProgressResponse.builder()
                .enrollmentId(enrollmentId)
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .totalSubtopics(totalSubtopics)
                .completedSubtopics(completedCount)
                .completionPercentage(Math.round(completionPercentage * 100.0) / 100.0)
                .completedItems(completedItems)
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
