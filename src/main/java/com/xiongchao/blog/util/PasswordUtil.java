package com.xiongchao.blog.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Xiong Chao
 */
public class PasswordUtil {

    private static final PasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 密码加密，返回80位加密密码
     *
     * @param rawPassword 原始密码
     * @return
     */
    public static String encode(String rawPassword) {
        return B_CRYPT_PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 密码比较
     *
     * @param rawPassword 原始密码
     * @param password    加密后的密码
     * @return
     */
    public static boolean matches(String rawPassword, String password) {
        return B_CRYPT_PASSWORD_ENCODER.matches(rawPassword, password);
    }

    public static void main(String[] args) {
        String password = "123456";
        // 加密
        String encode = PasswordUtil.encode(password);
        System.out.println(encode);
        // 对比
        System.out.println(PasswordUtil.matches("123456", encode));
    }
}
