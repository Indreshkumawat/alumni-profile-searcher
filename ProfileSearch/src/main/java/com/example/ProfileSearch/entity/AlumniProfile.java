package com.example.ProfileSearch.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alumni_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumniProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "job_role", nullable = false)
    private String currentRole;

    @Column(nullable = false)
    private String university;

    private String location;

    @Column(name = "linkedin_headline", length = 500)
    private String linkedinHeadline;

    @Column(name = "passout_year")
    private Integer passoutYear;
}