package com.example.ProfileSearch.service;
import com.example.ProfileSearch.dto.AlumniSearchRequest;
import com.example.ProfileSearch.entity.AlumniProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "phantombuster.mock-enabled", havingValue = "false")
public class PhantomBusterScraperService implements AlumniScraperStrategy {

    private final RestTemplate restTemplate;

    @Value("${phantombuster.api.key}")
    private String apiKey;

    @Value("${phantombuster.agent.id}")
    private String agentId;

    @Value("${phantombuster.api.base-url}")
    private String baseUrl;

    @Override
    public List<AlumniProfile> scrapeAlumniProfiles(AlumniSearchRequest request) {
        String launchUrl = baseUrl + "/agents/launch";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Phantombuster-Key-1", apiKey);

        String searchQuery = request.getUniversity() + " " + request.getDesignation();
        if (request.getPassoutYear() != null) {
            searchQuery += " " + request.getPassoutYear();
        }

        Map<String, Object> argument = new HashMap<>();
        argument.put("search", searchQuery);

        Map<String, Object> body = new HashMap<>();
        body.put("id", agentId);
        body.put("argument", argument);

        var entity = new HttpEntity<>(body, headers);

        try {
            log.info("Launching PhantomBuster agent {} for query: {}", agentId, searchQuery);
            var response = restTemplate.postForEntity(launchUrl, entity, Map.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Failed to fetch data from PhantomBuster API");
            }

            // Parse returned profiles from PhantomBuster container output
            return parsePhantomResponse((Map<String, Object>) response.getBody(), request);
        } catch (Exception ex) {
            log.error("PhantomBuster API error: {}", ex.getMessage());
            throw new RuntimeException("PhantomBuster integration failed: " + ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    private List<AlumniProfile> parsePhantomResponse(Map<String, Object> responseBody, AlumniSearchRequest request) {
        List<AlumniProfile> profiles = new ArrayList<>();
        Object dataObj = responseBody.get("data");

        if (dataObj instanceof List<?> dataList) {
            for (Object item : dataList) {
                if (item instanceof Map<?, ?> rawMap) {
                    Map<String, Object> map = (Map<String, Object>) rawMap;
                    profiles.add(AlumniProfile.builder()
                            .name(String.valueOf(map.getOrDefault("fullName", "Unknown")))
                            .currentRole(String.valueOf(map.getOrDefault("jobTitle", request.getDesignation())))
                            .university(request.getUniversity())
                            .location(String.valueOf(map.getOrDefault("location", "Not Specified")))
                            .linkedinHeadline(String.valueOf(map.getOrDefault("headline", request.getDesignation())))
                            .passoutYear(request.getPassoutYear())
                            .build());
                }
            }
        }
        return profiles;
    }
}