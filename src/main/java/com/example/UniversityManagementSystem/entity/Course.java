package com.example.UniversityManagementSystem.entity;

import com.example.UniversityManagementSystem.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(length = 8, nullable = false, unique = true)
    private String courseId;

    @Column(length = 50, nullable = false)
    private String courseName;

    @Column(name = "duration_days")
    private int durationDays;

    @Column(length = 10)
    private String badge;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private CourseStatus courseStatus = CourseStatus.SCHEDULED;
}
