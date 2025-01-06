package io.redispro.redisexec.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    public static String getEncodedPw(String rawPw) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPw);
        System.out.println(encodedPassword);
        return encodedPassword;
    }

}
