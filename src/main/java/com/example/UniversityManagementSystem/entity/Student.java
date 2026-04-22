package com.example.UniversityManagementSystem.entity;

import com.example.UniversityManagementSystem.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "studentid", length = 11, nullable = false, unique = true)
    private String studentId;

    @Column(name = "firstname", length = 10, nullable = false)
    private String firstName;

    @Column(name = "lastname", length = 10, nullable = false)
    private String lastName;

    private LocalDate dob;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "enrollmentdate")
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "studentstatus", length = 8, nullable = false)
    private StudentStatus studentStatus = StudentStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "courseenrolled")
    private Course course;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

}
