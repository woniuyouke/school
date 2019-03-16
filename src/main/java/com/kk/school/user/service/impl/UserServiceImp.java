package com.kk.school.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kk.school.common.CommonException;
import com.kk.school.common.FacePlus;
import com.kk.school.user.dao.UserDao;
import com.kk.school.user.entity.User;
import com.kk.school.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public boolean userExist(String no) {
        User user = userDao.findUser(no);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public void face(User user, MultipartFile file) {
        if (file != null) {
            try {
                BASE64Encoder encoder = new BASE64Encoder();
                // 通过base64来转化成string
                String data = encoder.encode(file.getBytes());
                String result = FacePlus.post(data);
                if (result != null) {
                    //json转换这里要记得看一看
                    Map<String, Object> map = new HashMap<>();
                    map = JSONObject.parseObject(result, HashMap.class);
                    JSONArray jsonArray = (JSONArray) map.get("faces");
                    Map<String, Object> jsonMap = (Map<String, Object>) jsonArray.get(0);
                    Map<String,Map<String,Object>> arrMap = (Map<String, Map<String, Object>>) jsonMap.get("attributes");
                    //记得采用jdk 8 的新写法,避免空指针
                    BigDecimal threshold = (BigDecimal) arrMap.get("facequality").get("threshold");
                    BigDecimal value = (BigDecimal) arrMap.get("facequality").get("value");

                    if (value.compareTo(threshold) < 0) {
                        throw new CommonException("人脸识别分数过低");
                    }
                    String faceToken = (String) jsonMap.get("face_token");
                    user.setFaceScore(value);
                    user.setFaceToken(faceToken);
                } else {
                    throw new CommonException("人脸识别失败");
                }
            } catch (Exception e) {
                throw new CommonException("人脸识别失败");
            }
        }else{
            throw  new CommonException("图片未上传");
        }
    }

    @Override
    public boolean saveUser(User user) {
        int resultCode = userDao.saveUser(user);
        if(resultCode > 0){
            return true;
        }
        return false;
    }

    @Override
    public User findUser(String no) {
        User user = userDao.findUser(no);
        return user;
    }
}
