package com.courseplatform.service;

import com.courseplatform.dto.SubtopicCompletionResponse;
import com.courseplatform.entity.*;
import com.courseplatform.exception.ForbiddenException;
import com.courseplatform.exception.ResourceNotFoundException;
import com.courseplatform.repository.EnrollmentRepository;
import com.courseplatform.repository.SubtopicProgressRepository;
import com.courseplatform.repository.SubtopicRepository;
import com.courseplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubtopicProgressService {

    private final SubtopicProgressRepository subtopicProgressRepository;
    private final SubtopicRepository subtopicRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubtopicCompletionResponse markSubtopicComplete(String subtopicId) {
        User user = getCurrentUser();

        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Subtopic with id '" + subtopicId + "' does not exist"));

        // Get the course ID from the subtopic
        String courseId = subtopic.getTopic().getCourse().getId();

        // Check if user is enrolled in the course
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new ForbiddenException(
                        "You must be enrolled in this course to mark subtopics as complete"));

        // Check if progress already exists (idempotent operation)
        SubtopicProgress progress = subtopicProgressRepository
                .findByUserIdAndSubtopicId(user.getId(), subtopicId)
                .orElse(null);

        if (progress == null) {
            // Create new progress record
            progress = SubtopicProgress.builder()
                    .user(user)
                    .subtopic(subtopic)
                    .enrollment(enrollment)
                    .completed(true)
                    .build();
        } else {
            // Update existing record (idempotent)
            progress.setCompleted(true);
        }

        SubtopicProgress savedProgress = subtopicProgressRepository.save(progress);

        return SubtopicCompletionResponse.builder()
                .subtopicId(subtopicId)
                .completed(savedProgress.getCompleted())
                .completedAt(savedProgress.getCompletedAt())
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
