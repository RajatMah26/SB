package com.courseplatform.controller;

import com.courseplatform.dto.SearchResponse;
import com.courseplatform.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Public search endpoints for courses and content")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "Search courses and content", description = "Search across course titles, descriptions, topics, subtopics, and content using case-insensitive partial matching")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<SearchResponse> search(
            @Parameter(description = "Search query", required = true) @RequestParam String q) {
        SearchResponse response = searchService.search(q);
        return ResponseEntity.ok(response);
    }
}
