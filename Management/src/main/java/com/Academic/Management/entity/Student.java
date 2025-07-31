package com.Academic.Management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String className;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades = new ArrayList<>();

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    // Constructors
    public Student() {}

    public Student(String name, Gender gender, String className) {
        this.name = name;
        this.gender = gender;
        this.className = className;
    }

    // Method to calculate average grade
    public double getAverageGrade() {
        if (grades.isEmpty()) return 0.0;
        return grades.stream()
                .mapToDouble(Grade::getMarks)
                .average()
                .orElse(0.0);
    }

    // Method to get division based on average
    public String getDivision() {
        double avg = getAverageGrade();
        if (avg >= 80) return "I";
        else if (avg >= 65) return "II";
        else if (avg >= 50) return "III";
        else if (avg >= 35) return "IV";
        else return "ZERO";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public List<Grade> getGrades() { return grades; }
    public void setGrades(List<Grade> grades) { this.grades = grades; }
}
