package com.Academic.Management.repository;

import com.Academic.Management.entity.ClassResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassResultRepository extends JpaRepository<ClassResult, Long> {
    List<ClassResult> findByClassName(String className);
    List<ClassResult> findByOrderByCreatedAtDesc();
}
