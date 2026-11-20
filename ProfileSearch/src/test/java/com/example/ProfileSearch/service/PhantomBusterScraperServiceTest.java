package com.example.ProfileSearch.service;

import com.example.ProfileSearch.dto.AlumniSearchRequest;
import com.example.ProfileSearch.entity.AlumniProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhantomBusterScraperServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PhantomBusterScraperService scraperService;

    private AlumniSearchRequest request;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(scraperService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(scraperService, "agentId", "123456");
        ReflectionTestUtils.setField(scraperService, "baseUrl", "https://api.phantombuster.com/api/v2");

        request = AlumniSearchRequest.builder()
                .university("University of XYZ")
                .designation("Software Engineer")
                .passoutYear(2020)
                .build();
    }

    @Test
    void scrapeAlumniProfiles_ShouldCallPhantomBusterAndParseProfiles() {
        Map<String, Object> fakeProfile = Map.of(
                "fullName", "Alice Johnson",
                "jobTitle", "Software Engineer",
                "location", "Seattle, WA",
                "headline", "Software Engineer at Cloud Inc"
        );
        Map<String, Object> fakeResponseBody = Map.of("data", List.of(fakeProfile));

        when(restTemplate.postForEntity(
                eq("https://api.phantombuster.com/api/v2/agents/launch"),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(new ResponseEntity<>(fakeResponseBody, HttpStatus.OK));

        List<AlumniProfile> profiles = scraperService.scrapeAlumniProfiles(request);

        assertNotNull(profiles);
        assertEquals(1, profiles.size());
        assertEquals("Alice Johnson", profiles.get(0).getName());
        assertEquals("University of XYZ", profiles.get(0).getUniversity());
        assertEquals(2020, profiles.get(0).getPassoutYear());

        verify(restTemplate, times(1)).postForEntity(
                eq("https://api.phantombuster.com/api/v2/agents/launch"),
                any(HttpEntity.class),
                eq(Map.class)
        );
    }

    @Test
    void scrapeAlumniProfiles_ShouldThrowExceptionWhenApiFails() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("API timeout"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> scraperService.scrapeAlumniProfiles(request));

        assertTrue(ex.getMessage().contains("PhantomBuster integration failed"));
    }
}