package com.example.demo.BackEnd.Controller;

import com.example.demo.BackEnd.Repository.StudentRepository;
import com.example.demo.Config.JWT.JwtTokenUtils;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorResponseBase;
import com.example.demo.entity.DTO.LoginDTO;
import com.example.demo.entity.LoginRequest;
import com.example.demo.entity.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("api/")
@CrossOrigin("*")
@Validated
@Component
public class AuthController {
    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Autowired
    private StudentRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private HttpServletRequest httpServletRequest;


    @PostMapping("/login")
    public LoginDTO loginJWT(@RequestBody LoginRequest request) {
        String pw = passwordEncoder.encode("123456");
        System.out.println(passwordEncoder.matches(request.getPassword(), pw));

            Optional<Student> optional = repository.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            throw new AppException(ErrorResponseBase.Login_username);

        }
        if (!passwordEncoder.matches(request.getPassword(), optional.get().getPassword())) {
            throw new AppException(ErrorResponseBase.Login_password);
        }
        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(optional.get(), loginDTO);
        loginDTO.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        String token = jwtTokenUtils.createAccessToken(loginDTO);
        loginDTO.setToken(token);
        return loginDTO;
    }

}
