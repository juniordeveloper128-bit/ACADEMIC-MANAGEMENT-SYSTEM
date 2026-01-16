package com.Academic.Management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_results")
public class StudentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_result_id")
    private ClassResult classResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private Double averageGrade;
    private String division;
    private Integer rank;

    // Constructors
    public StudentResult() {}

    public StudentResult(ClassResult classResult, Student student) {
        this.classResult = classResult;
        this.student = student;
        this.averageGrade = student.getAverageGrade();
        this.division = student.getDivision();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ClassResult getClassResult() { return classResult; }
    public void setClassResult(ClassResult classResult) { this.classResult = classResult; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Double getAverageGrade() { return averageGrade; }
    public void setAverageGrade(Double averageGrade) { this.averageGrade = averageGrade; }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
}