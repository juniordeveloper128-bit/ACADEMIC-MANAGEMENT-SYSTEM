package com.Academic.Management.service;

import com.Academic.Management.entity.Student;
import com.Academic.Management.entity.Grade;
import com.Academic.Management.repository.StudentRepository;
import com.Academic.Management.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> findByNameAndClassName(String name, String className) {
        return studentRepository.findByNameAndClassName(name, className);
    }

    public List<Student> findByClassName(String className) {
        return studentRepository.findByClassName(className);
    }

    public List<String> getAllClassNames() {
        return studentRepository.findDistinctClassNames();
    }

    public List<Student> getStudentsWithGradesByClassName(String className) {
        List<Student> students = studentRepository.findByClassName(className);
        // Load grades for each student
        for (Student student : students) {
            List<Grade> grades = gradeRepository.findByStudent(student);
            student.setGrades(grades);
        }
        return students;
    }

    public List<Student> getStudentsSortedByPerformance(String className, String sortType) {
        List<Student> students = getStudentsWithGradesByClassName(className);

        switch (sortType.toLowerCase()) {
            case "average":
                return students.stream()
                        .sorted((s1, s2) -> Double.compare(s2.getAverageGrade(), s1.getAverageGrade()))
                        .collect(Collectors.toList());
            case "division":
                return students.stream()
                        .sorted((s1, s2) -> {
                            int divisionOrder1 = getDivisionOrder(s1.getDivision());
                            int divisionOrder2 = getDivisionOrder(s2.getDivision());
                            if (divisionOrder1 != divisionOrder2) {
                                return Integer.compare(divisionOrder1, divisionOrder2);
                            }
                            return Double.compare(s2.getAverageGrade(), s1.getAverageGrade());
                        })
                        .collect(Collectors.toList());
            default:
                return students;
        }
    }

    private int getDivisionOrder(String division) {
        switch (division) {
            case "I": return 1;
            case "II": return 2;
            case "III": return 3;
            case "IV": return 4;
            case "ZERO": return 5;
            default: return 6;
        }
    }
}