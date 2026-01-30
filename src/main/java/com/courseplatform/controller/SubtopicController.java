package com.courseplatform.controller;

import com.courseplatform.dto.SubtopicCompletionResponse;
import com.courseplatform.service.SubtopicProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subtopics")
@RequiredArgsConstructor
@Tag(name = "Subtopic Progress", description = "Subtopic completion tracking endpoints (requires authentication)")
@SecurityRequirement(name = "Bearer Authentication")
public class SubtopicController {

    private final SubtopicProgressService subtopicProgressService;

    @PostMapping("/{subtopicId}/complete")
    @Operation(summary = "Mark subtopic as completed", description = "Mark a subtopic as completed for the authenticated user. Operation is idempotent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subtopic marked as completed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not enrolled in the course"),
            @ApiResponse(responseCode = "404", description = "Subtopic not found")
    })
    public ResponseEntity<SubtopicCompletionResponse> markSubtopicComplete(@PathVariable String subtopicId) {
        SubtopicCompletionResponse response = subtopicProgressService.markSubtopicComplete(subtopicId);
        return ResponseEntity.ok(response);
    }
}
