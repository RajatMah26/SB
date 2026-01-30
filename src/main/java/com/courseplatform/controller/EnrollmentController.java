package com.courseplatform.controller;

import com.courseplatform.dto.EnrollmentResponse;
import com.courseplatform.dto.ProgressResponse;
import com.courseplatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Course enrollment and progress tracking endpoints (requires authentication)")
@SecurityRequirement(name = "Bearer Authentication")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/api/courses/{courseId}/enroll")
    @Operation(summary = "Enroll in a course", description = "Enroll the authenticated user in a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully enrolled"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Already enrolled in this course")
    })
    public ResponseEntity<EnrollmentResponse> enrollInCourse(@PathVariable String courseId) {
        EnrollmentResponse response = enrollmentService.enrollInCourse(courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/enrollments/{enrollmentId}/progress")
    @Operation(summary = "View enrollment progress", description = "Get detailed progress information for a specific enrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved progress"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<ProgressResponse> getProgress(@PathVariable Long enrollmentId) {
        ProgressResponse response = enrollmentService.getProgress(enrollmentId);
        return ResponseEntity.ok(response);
    }
}
