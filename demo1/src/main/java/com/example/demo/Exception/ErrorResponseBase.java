package com.example.demo.Exception;

import org.springframework.http.HttpStatus;

public enum ErrorResponseBase {
    NOT_FOUND(HttpStatus.NOT_FOUND, "ClassRoom không tồn tại "),
    USERNAME_Not(HttpStatus.INTERNAL_SERVER_ERROR, "Account không tồn tại"),
    USERNAME_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "user đã tồn tại"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ"),
    Id_not(HttpStatus.BAD_REQUEST, "không có dữ liệu trong database"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập"),
    LOGIN_ERR(HttpStatus.UNAUTHORIZED, "người dùng không tồn tại"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Không được phép truy cập"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi máy chủ"),
    INVALID_Room(HttpStatus.BAD_REQUEST, "không tìm thấy class room"),
    INVALID_ACCOUNT(HttpStatus.BAD_REQUEST, "Tài khoản không hợp lệ"),
    Min_Max(HttpStatus.BAD_REQUEST, "số min phải nhỏ hơn max"),
    Login_username(HttpStatus.UNAUTHORIZED, "usrname không đúng "),
    Login_password(HttpStatus.UNAUTHORIZED, "PASS word không hợp lệ"),
    DOUBLE_EMAIL_EX(HttpStatus.BAD_REQUEST, "password không hợp lệ");
    public final HttpStatus status;
    public final String message;

    ErrorResponseBase(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

