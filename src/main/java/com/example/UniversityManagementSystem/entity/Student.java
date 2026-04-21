package com.example.UniversityManagementSystem.entity;

import com.example.UniversityManagementSystem.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
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

    @Column(length = 8, nullable = false, unique = true)
    private String studentId;

    @Column(length = 10, nullable = false)
    private String firstName;

    @Column(length = 10, nullable = false)
    private String lastName;

    private LocalDate dob;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false)
    private StudentStatus studentStatus = StudentStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "courseEnrolled")
    private Course course;

}
