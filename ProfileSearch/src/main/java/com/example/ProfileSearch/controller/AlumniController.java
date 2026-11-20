package com.example.ProfileSearch.controller;

import com.example.ProfileSearch.dto.AlumniProfileResponse;
import com.example.ProfileSearch.dto.AlumniSearchRequest;
import com.example.ProfileSearch.dto.ApiResponse;
import com.example.ProfileSearch.service.AlumniService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumni")
@RequiredArgsConstructor
@Tag(name = "Alumni Search API", description = "Search and retrieve LinkedIn alumni profiles")
public class AlumniController {

    private final AlumniService alumniService;

    @PostMapping("/search")
    @Operation(summary = "Search Alumni Profiles", description = "Searches LinkedIn alumni profiles via PhantomBuster and saves them to PostgreSQL")
    public ResponseEntity<ApiResponse<List<AlumniProfileResponse>>> searchAlumni(
            @Valid @RequestBody AlumniSearchRequest request) {

        List<AlumniProfileResponse> results = alumniService.searchAndSaveAlumni(request);
        return ResponseEntity.ok(ApiResponse.<List<AlumniProfileResponse>>builder()
                .status("success")
                .data(results)
                .build());
    }

    @GetMapping("/all")
    @Operation(summary = "Get All Saved Alumni Profiles", description = "Retrieves all alumni profiles saved in PostgreSQL")
    public ResponseEntity<ApiResponse<List<AlumniProfileResponse>>> getAllAlumni() {

        List<AlumniProfileResponse> results = alumniService.getAllSavedAlumni();
        return ResponseEntity.ok(ApiResponse.<List<AlumniProfileResponse>>builder()
                .status("success")
                .data(results)
                .build());
    }
}
