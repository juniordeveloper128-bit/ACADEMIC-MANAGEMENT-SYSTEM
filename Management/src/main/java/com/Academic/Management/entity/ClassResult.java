package com.Academic.Management.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "class_results")
public class ClassResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;
    private String resultName;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "classResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentResult> studentResults;

    // Constructors
    public ClassResult() {
        this.createdAt = LocalDateTime.now();
    }

    public ClassResult(String className, String resultName, User createdBy) {
        this.className = className;
        this.resultName = resultName;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getResultName() { return resultName; }
    public void setResultName(String resultName) { this.resultName = resultName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public List<StudentResult> getStudentResults() { return studentResults; }
    public void setStudentResults(List<StudentResult> studentResults) { this.studentResults = studentResults; }
}