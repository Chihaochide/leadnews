package com.liuxuanhe;

import com.liuxuanhe.utils.common.BCrypt;
import org.junit.Test;

public class MethodTest {

    @Test
    public void testEncode(){
        String salt = BCrypt.gensalt(); // 盐
        String password = "admin";

        String pwd = BCrypt.hashpw(password,salt);

        System.out.println("pwd = " + pwd);

    }
}
