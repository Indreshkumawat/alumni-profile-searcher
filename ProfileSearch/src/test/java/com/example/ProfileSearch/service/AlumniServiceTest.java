package com.example.ProfileSearch.service;

import com.example.ProfileSearch.dto.AlumniProfileResponse;
import com.example.ProfileSearch.dto.AlumniSearchRequest;
import com.example.ProfileSearch.entity.AlumniProfile;
import com.example.ProfileSearch.repository.AlumniRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumniServiceTest {

    @Mock
    private AlumniScraperStrategy scraperStrategy;

    @Mock
    private AlumniRepository alumniRepository;

    @InjectMocks
    private AlumniService alumniService;

    private AlumniSearchRequest searchRequest;
    private AlumniProfile sampleProfile;

    @BeforeEach
    void setUp() {
        searchRequest = AlumniSearchRequest.builder()
                .university("University of XYZ")
                .designation("Software Engineer")
                .passoutYear(2020)
                .build();

        sampleProfile = AlumniProfile.builder()
                .id(1L)
                .name("John Doe")
                .currentRole("Software Engineer")
                .university("University of XYZ")
                .location("New York, NY")
                .linkedinHeadline("Passionate Software Engineer at Tech Corp")
                .passoutYear(2020)
                .build();
    }

    @Test
    void searchAndSaveAlumni_ShouldScrapeSaveAndReturnProfiles() {
        when(scraperStrategy.scrapeAlumniProfiles(searchRequest))
                .thenReturn(List.of(sampleProfile));
        when(alumniRepository.saveAll(anyList()))
                .thenReturn(List.of(sampleProfile));

        List<AlumniProfileResponse> result = alumniService.searchAndSaveAlumni(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("University of XYZ", result.get(0).getUniversity());

        verify(scraperStrategy, times(1)).scrapeAlumniProfiles(searchRequest);
        verify(alumniRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getAllSavedAlumni_ShouldReturnAllProfiles() {
        when(alumniRepository.findAll()).thenReturn(List.of(sampleProfile));

        List<AlumniProfileResponse> result = alumniService.getAllSavedAlumni();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Software Engineer", result.get(0).getCurrentRole());

        verify(alumniRepository, times(1)).findAll();
    }
}