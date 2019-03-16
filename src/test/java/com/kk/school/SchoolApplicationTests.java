package com.kk.school;

import com.kk.school.user.entity.User;
import com.kk.school.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchoolApplicationTests {

    @Autowired
    UserService userService;


    @Test
    public void saveUser(){
        User user = new User();
        user.setFaceToken("xxx");
        user.setType(1);
        user.setPwd("ss");
        user.setName("xwk");
        user.setSchNo("112");
        userService.saveUser(user);

    }

    @Test
    public void queryUser(){
       User user =  userService.findUser("112");
       System.out.println(user);
    }

}
