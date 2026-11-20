package com.example.ProfileSearch.repository;

import com.example.ProfileSearch.entity.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumniRepository extends JpaRepository<AlumniProfile, Long> {
    List<AlumniProfile> findByUniversityIgnoreCaseAndCurrentRoleContainingIgnoreCase(
            String university, String currentRole);
}