package com.bharath.lab8.controller;

import com.bharath.lab8.model.Student;
import com.bharath.lab8.repository.StudentRepository;
import com.bharath.lab8.captcha.CaptchaUtils;
import cn.apiclub.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        Captcha captcha = CaptchaUtils.createCaptcha(200, 50);
        String captchaImage = CaptchaUtils.encodeBase64(captcha);
        // Return CAPTCHA answer and image to the client
        return ResponseEntity.ok(new CaptchaResponse(captcha.getAnswer(), captchaImage));
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        // Validate CAPTCHA before proceeding
        if (student.getCaptcha() == null || !student.getCaptcha().equals(student.getHidden())) {
            return ResponseEntity.badRequest().body("Invalid CAPTCHA.");
        }

        // Save the student to the repository
        studentRepository.save(student);
        return ResponseEntity.ok("Student added successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (!studentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Student student = studentOptional.get();
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        Student updatedStudent = studentRepository.save(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Inner class to encapsulate CAPTCHA response
    public static class CaptchaResponse {
        private String hidden;
        private String image;

        public CaptchaResponse(String hidden, String image) {
            this.hidden = hidden;
            this.image = image;
        }

        public String getHidden() {
            return hidden;
        }

        public String getImage() {
            return image;
        }
    }
}
