package com.example.demo.BackEnd.Service;

import com.example.demo.entity.Student;

import java.util.List;

public interface IStudentService {

    List<Student> getAllStudent();
    void deleteStudent(int id);
    Student updateStudent(int id,Student student);
    void createStudent(Student student);

    Student authenticate(String userName,String password);
}
