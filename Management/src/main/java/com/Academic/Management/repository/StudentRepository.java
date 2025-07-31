package com.Academic.Management.repository;

import com.Academic.Management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByNameAndClassName(String name, String className);
    List<Student> findByClassName(String className);

    @Query("SELECT DISTINCT s.className FROM Student s")
    List<String> findDistinctClassNames();
}