package com.example.demo.Config.JWT;


import com.alibaba.fastjson.JSON;
import com.example.demo.BackEnd.Repository.TokenRepository;
import com.example.demo.Exception.AppException;
import com.example.demo.entity.DTO.LoginDTO;
import com.example.demo.entity.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    private static final long EXPIRATION = 864000000;//10 day
    private static final String SECRET = "123456";//chữ kí bí mật
    private static final String PREFIX_TOKEN = "Bearer";// ký tự đầu của token
    private static final String AUTHORIZATION = "Authorization";// key của token trên header


    @Autowired
    private TokenRepository tokenRepository;

    // cái này là tạo ra token
    public String createAccessToken(LoginDTO loginDTO) {
        // tạo giá trị thời hạn cho token
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION);
        String token = Jwts.builder()
                .setId(String.valueOf(loginDTO.getId()))//sét giá trị của id
                .setSubject(loginDTO.getUserAgent())//sét giá trị của subject
                .setIssuedAt(new Date())
                .setIssuer("VTI")
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .claim("user-Agent", loginDTO.getUserAgent()).compact();
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setExpiration(expirationDate);
        tokenEntity.setUserAgent(loginDTO.getUserAgent());
        tokenRepository.save(tokenEntity);
        return token;
    }

    // Hàm này dùng để giải mã hoá token
    public LoginDTO parseAccessToken(String token) {

        LoginDTO loginDto = new LoginDTO();
        if (!token.isEmpty()) {
            try {
                token = token.replace(PREFIX_TOKEN, "").trim();
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token).getBody();
                // Lấy ra các thông tin
                String user = claims.getSubject();

                String userAgent = claims.get("user-Agent").toString();

                // Gán các thông tin vào đối tượng LoginDto, có thể sử dụng constructor
                loginDto.setUsername(user);

                loginDto.setUserAgent(userAgent);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return loginDto;
    }

    public boolean checkToken(String token, HttpServletResponse response, HttpServletRequest httpServletRequest) {
        try {
            if (StringUtils.isBlank(token) || !token.startsWith(PREFIX_TOKEN)) { // token bị trống -> lỗi
                responseJson(response, new AppException("Token ko hợp lệ", 401, httpServletRequest.getRequestURI()));
                return false;
            }
            token = token.replace(PREFIX_TOKEN, "").trim();

            Token tokenEntity = tokenRepository.findByToken(token);
            if (tokenEntity == null) { // Ko có token trên hệ thống
                responseJson(response, new AppException("Token ko tồn tại", 401, httpServletRequest.getRequestURI()));
                return false;
            }

//            String ip = httpServletRequest.getHeader("User-Agent");
//            if (!StringUtils.equals(ip, tokenEntity.getUserAgent())) { // Token sử dụng ở một nơi khác
//                responseJson(response, new AppException("Token đc dùng ở chỗ khác",401, httpServletRequest.getRequestURI()));
//                return false;
//            }

            if (tokenEntity.getExpiration().after(new Date(System.currentTimeMillis() + EXPIRATION))) { // Token hết hạn
                responseJson(response, new AppException("Token hết hiệu lực", 401, httpServletRequest.getRequestURI()));
                return false;
            }
        } catch (Exception e) {
            responseJson(response, new AppException(e.getMessage(), 401, httpServletRequest.getRequestURI()));
            return false;
        }
        return true;
    }

    // Hàm này dùng để response dữ liệu khi gặp lỗi
    private void responseJson(HttpServletResponse response, AppException appException) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(appException.getCode());
        try {
            response.getWriter().print(JSON.toJSONString(appException));
        } catch (IOException e) {
            log.debug(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

