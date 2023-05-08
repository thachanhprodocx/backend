package com.example.demo.BackEnd.Service;

import com.example.demo.BackEnd.Repository.StudentRepository;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService, UserDetailsService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public void deleteStudent(int id) {
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isEmpty()){
            throw new ApplicationContextException("khong co du lieu");
        }
        studentRepository.delete(optional.get());
    }

    @Override
    public Student updateStudent(int id, Student student) {
        Student student1 = studentRepository.findById(id).orElseThrow(() -> new ApplicationContextException("khong co du lieu"));
       student1.setName(student.getName());
       student1.setEmail(student.getEmail());
        student1.setPassword(encoder.encode(student.getPassword()));
       student1.setUsername(student.getUsername());

        return studentRepository.save(student1);
    }

    @Override
    public void createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())){
            throw new ApplicationContextException("loi da co e mai roi ");
        }
        Student student1 = new Student();
        student1.setName(student.getName());
        student1.setPassword(encoder.encode(student.getPassword()));
        student1.setUsername(student.getUsername());
        student1.setEmail(student.getEmail());

        studentRepository.save(student1);
    }

    @Override
    public Student authenticate(String userName, String password) {
        return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> optional = studentRepository.findByUsername(username);
        if (optional.isPresent()) {
            Student  student = optional.get();
            List<GrantedAuthority> authorities = new ArrayList<>();

            return new User(student.getUsername(), student.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

}
