package com.example.demo.BackEnd.Controller;

import com.example.demo.BackEnd.Service.StudentService;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/controller")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/get-all")
    public List<Student> getAllStudent() {
        return studentService.getAllStudent();
    }

    @DeleteMapping("/delete/{id}")
    private void deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
    }

    @PutMapping("/update/{id}")
    private Student updateStudent(@PathVariable int id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @PostMapping("/create")
    private void createStudent(@RequestBody Student student) {
        studentService.createStudent(student);
    }




}
