package com.Academic.Management.service;

import com.Academic.Management.entity.*;
import com.Academic.Management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ClassResultService {

    @Autowired
    private ClassResultRepository classResultRepository;

    @Autowired
    private StudentResultRepository studentResultRepository;

    @Autowired
    private StudentService studentService;

    @Transactional
    public ClassResult createClassResult(String className, String resultName, User createdBy) {
        ClassResult classResult = new ClassResult(className, resultName, createdBy);
        classResult = classResultRepository.save(classResult);

        // Get all students in the class with their grades
        List<Student> students = studentService.getStudentsWithGradesByClassName(className);

        // Create student results and sort by average grade
        ClassResult finalClassResult = classResult;
        List<StudentResult> studentResults = students.stream()
                .map(student -> new StudentResult(finalClassResult, student))
                .sorted((sr1, sr2) -> Double.compare(sr2.getAverageGrade(), sr1.getAverageGrade()))
                .toList();

        // Assign ranks
        IntStream.range(0, studentResults.size())
                .forEach(i -> studentResults.get(i).setRank(i + 1));

        studentResultRepository.saveAll(studentResults);

        return classResult;
    }

    public List<ClassResult> getAllClassResults() {
        return classResultRepository.findByOrderByCreatedAtDesc();
    }

    public List<StudentResult> getStudentResultsByClassResult(ClassResult classResult, String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "division":
                return studentResultRepository.findByClassResultOrderByDivisionAscAverageGradeDesc(classResult);
            case "average":
            default:
                return studentResultRepository.findByClassResultOrderByAverageGradeDesc(classResult);
        }
    }

    public ClassResult findById(Long id) {
        return classResultRepository.findById(id).orElse(null);
    }
}