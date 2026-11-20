package com.example.ProfileSearch.service;

import com.example.ProfileSearch.dto.AlumniSearchRequest;
import com.example.ProfileSearch.entity.AlumniProfile;

import java.util.List;

public interface AlumniScraperStrategy {
    List<AlumniProfile> scrapeAlumniProfiles(AlumniSearchRequest request);
}