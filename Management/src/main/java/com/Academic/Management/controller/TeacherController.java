package com.Academic.Management.controller;

import com.Academic.Management.entity.*;
import com.Academic.Management.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != User.Role.TEACHER) {
            return "redirect:/login?error=unauthorized";
        }

        model.addAttribute("teacher", currentUser);
        return "teacher/dashboard";
    }

    @PostMapping("/submit-grade")
    public String submitGrade(@RequestParam String studentName,
                              @RequestParam Student.Gender gender,
                              @RequestParam String className,
                              @RequestParam String subject,
                              @RequestParam Double marks,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != User.Role.TEACHER) {
            return "redirect:/login?error=unauthorized";
        }

        try {
            // Validate input
            if (studentName == null || studentName.trim().isEmpty()) {
                throw new IllegalArgumentException("Student name is required");
            }
            if (className == null || className.trim().isEmpty()) {
                throw new IllegalArgumentException("Class name is required");
            }
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject is required");
            }
            if (marks == null || marks < 0 || marks > 100) {
                throw new IllegalArgumentException("Marks must be between 0 and 100");
            }

            // Find or create student
            Student student = studentService.findByNameAndClassName(studentName, className)
                    .orElseGet(() -> {
                        Student newStudent = new Student(studentName, gender, className);
                        return studentService.saveStudent(newStudent);
                    });

            // Create and save grade
            Grade grade = new Grade(student, currentUser, subject, marks);
            gradeService.saveGrade(grade);

            redirectAttributes.addFlashAttribute("success",
                    "Grade submitted successfully for " + studentName + " in " + subject);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting grade: " + e.getMessage());
        }

        return "redirect:/teacher/dashboard";
    }
}