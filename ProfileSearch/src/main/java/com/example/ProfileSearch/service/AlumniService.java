package com.example.ProfileSearch.service;

import com.example.ProfileSearch.dto.AlumniProfileResponse;
import com.example.ProfileSearch.dto.AlumniSearchRequest;
import com.example.ProfileSearch.entity.AlumniProfile;
import com.example.ProfileSearch.repository.AlumniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumniService {

    private final AlumniScraperStrategy scraperStrategy;
    private final AlumniRepository alumniRepository;

    public List<AlumniProfileResponse> searchAndSaveAlumni(AlumniSearchRequest request) {
        List<AlumniProfile> scrapedProfiles = scraperStrategy.scrapeAlumniProfiles(request);
        List<AlumniProfile> savedProfiles = alumniRepository.saveAll(scrapedProfiles);

        return savedProfiles.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AlumniProfileResponse> getAllSavedAlumni() {
        return alumniRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AlumniProfileResponse mapToResponse(AlumniProfile entity) {
        return AlumniProfileResponse.builder()
                .name(entity.getName())
                .currentRole(entity.getCurrentRole())
                .university(entity.getUniversity())
                .location(entity.getLocation())
                .linkedinHeadline(entity.getLinkedinHeadline())
                .passoutYear(entity.getPassoutYear())
                .build();
    }
}
