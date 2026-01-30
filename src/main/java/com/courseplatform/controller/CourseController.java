package com.courseplatform.controller;

import com.courseplatform.dto.CourseDetailResponse;
import com.courseplatform.dto.CoursesResponse;
import com.courseplatform.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Public course browsing endpoints")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "List all courses", description = "Get a list of all available courses with topic and subtopic counts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    public ResponseEntity<CoursesResponse> getAllCourses() {
        CoursesResponse response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Get detailed course information including all topics, subtopics, and content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseDetailResponse> getCourseById(@PathVariable String id) {
        CourseDetailResponse response = courseService.getCourseById(id);
        return ResponseEntity.ok(response);
    }
}
