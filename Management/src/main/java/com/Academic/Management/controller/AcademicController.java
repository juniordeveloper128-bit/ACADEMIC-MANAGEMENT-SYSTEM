package com.Academic.Management.controller;

import com.Academic.Management.entity.*;
import com.Academic.Management.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/academic")
public class AcademicController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private ClassResultService classResultService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != User.Role.ACADEMIC_MASTER) {
            return "redirect:/login";
        }

        List<String> classNames = studentService.getAllClassNames();
        List<ClassResult> classResults = classResultService.getAllClassResults();

        model.addAttribute("academic", currentUser);
        model.addAttribute("classNames", classNames);
        model.addAttribute("classResults", classResults);
        return "academic/dashboard";
    }

    @GetMapping("/StudentResult")
    public String viewResults(@RequestParam String className,
                              @RequestParam(defaultValue = "average") String sortBy,
                              Model model,
                              HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != User.Role.ACADEMIC_MASTER) {
            return "redirect:/login";
        }

        List<Student> students = studentService.getStudentsSortedByPerformance(className, sortBy);
        List<String> subjects = gradeService.getDistinctSubjectsByClassName(className);

        // Create a map for easy grade lookup
        Map<String, Map<String, Double>> studentGrades = new HashMap<>();
        for (Student student : students) {
            Map<String, Double> grades = new HashMap<>();
            for (Grade grade : student.getGrades()) {
                grades.put(grade.getSubject(), grade.getMarks());
            }
            studentGrades.put(student.getName(), grades);
        }

        model.addAttribute("students", students);
        model.addAttribute("subjects", subjects);
        model.addAttribute("studentGrades", studentGrades);
        model.addAttribute("className", className);
        model.addAttribute("sortBy", sortBy);

        return "academic/view-results";
    }

    @PostMapping("/create-result")
    public String createResult(@RequestParam String className,
                               @RequestParam String resultName,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null || currentUser.getRole() != User.Role.ACADEMIC_MASTER) {
                return "redirect:/login";
            }

            ClassResult classResult = classResultService.createClassResult(className, resultName, currentUser);
            redirectAttributes.addFlashAttribute("success",
                    "Class result '" + resultName + "' created successfully for " + className);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating result: " + e.getMessage());
        }

        return "redirect:/academic/dashboard";
    }

    @GetMapping("/result/{id}")
    public String viewResult(@PathVariable Long id,
                             @RequestParam(defaultValue = "average") String sortBy,
                             Model model,
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != User.Role.ACADEMIC_MASTER) {
            return "redirect:/login";
        }

        ClassResult classResult = classResultService.findById(id);
        if (classResult == null) {
            return "redirect:/academic/dashboard";
        }

        List<StudentResult> studentResults = classResultService.getStudentResultsByClassResult(classResult, sortBy);
        List<String> subjects = gradeService.getDistinctSubjectsByClassName(classResult.getClassName());

        // Create grade mapping for display
        Map<String, Map<String, Double>> studentGrades = new HashMap<>();
        for (StudentResult sr : studentResults) {
            Map<String, Double> grades = new HashMap<>();
            for (Grade grade : sr.getStudent().getGrades()) {
                grades.put(grade.getSubject(), grade.getMarks());
            }
            studentGrades.put(sr.getStudent().getName(), grades);
        }

        model.addAttribute("classResult", classResult);
        model.addAttribute("studentResults", studentResults);
        model.addAttribute("subjects", subjects);
        model.addAttribute("studentGrades", studentGrades);
        model.addAttribute("sortBy", sortBy);

        return "academic/result-detail";
    }
}