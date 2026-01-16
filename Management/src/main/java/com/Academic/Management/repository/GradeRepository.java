package com.Academic.Management.repository;

import com.Academic.Management.entity.Grade;
import com.Academic.Management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent(Student student);
    List<Grade> findByStudentClassName(String className);

    @Query("SELECT DISTINCT g.subject FROM Grade g WHERE g.student.className = :className")
    List<String> findDistinctSubjectsByClassName(@Param("className") String className);

    @Query("SELECT g FROM Grade g WHERE g.student.className = :className ORDER BY g.student.name, g.subject")
    List<Grade> findByClassNameOrderByStudentAndSubject(@Param("className") String className);
}