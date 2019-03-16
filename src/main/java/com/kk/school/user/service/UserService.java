package com.kk.school.user.service;


import com.kk.school.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
     boolean userExist(String no);
     void face(User user, MultipartFile file);

     boolean saveUser(User user);

     User findUser(String no);
}
