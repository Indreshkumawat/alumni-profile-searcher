package com.example.ProfileSearch.service;

import com.example.ProfileSearch.dto.AlumniSearchRequest;
import com.example.ProfileSearch.entity.AlumniProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(name = "phantombuster.mock-enabled", havingValue = "true", matchIfMissing = true)
public class MockAlumniScraperService implements AlumniScraperStrategy {

    @Override
    public List<AlumniProfile> scrapeAlumniProfiles(AlumniSearchRequest request) {
        log.info("Using MockAlumniScraperService for university: {}, designation: {}",
                request.getUniversity(), request.getDesignation());

        int year = request.getPassoutYear() != null ? request.getPassoutYear() : 2020;

        return List.of(
                AlumniProfile.builder()
                        .name("John Doe")
                        .currentRole(request.getDesignation())
                        .university(request.getUniversity())
                        .location("New York, NY")
                        .linkedinHeadline("Passionate " + request.getDesignation() + " at Tech Corp")
                        .passoutYear(year)
                        .build(),
                AlumniProfile.builder()
                        .name("Jane Smith")
                        .currentRole(request.getDesignation())
                        .university(request.getUniversity())
                        .location("San Francisco, CA")
                        .linkedinHeadline(request.getDesignation() + " | Cloud & AI Enthusiast")
                        .passoutYear(year - 1)
                        .build()
        );
    }
}