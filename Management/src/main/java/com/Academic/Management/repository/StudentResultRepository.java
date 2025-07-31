package com.Academic.Management.repository;

import com.Academic.Management.entity.StudentResult;
import com.Academic.Management.entity.ClassResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {
    List<StudentResult> findByClassResult(ClassResult classResult);
    List<StudentResult> findByClassResultOrderByAverageGradeDesc(ClassResult classResult);
    List<StudentResult> findByClassResultOrderByDivisionAscAverageGradeDesc(ClassResult classResult);
}