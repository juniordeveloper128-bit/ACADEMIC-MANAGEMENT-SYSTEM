package com.Academic.Management.service;

import com.Academic.Management.entity.Grade;
import com.Academic.Management.entity.Student;
import com.Academic.Management.entity.User;
import com.Academic.Management.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public List<Grade> getGradesByStudent(Student student) {
        return gradeRepository.findByStudent(student);
    }

    public List<Grade> getGradesByClassName(String className) {
        return gradeRepository.findByStudentClassName(className);
    }

    public List<String> getDistinctSubjectsByClassName(String className) {
        return gradeRepository.findDistinctSubjectsByClassName(className);
    }

    public List<Grade> getGradesByClassNameSorted(String className) {
        return gradeRepository.findByClassNameOrderByStudentAndSubject(className);
    }
}
