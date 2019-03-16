package com.kk.school.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.kk.school.common.*;
import com.kk.school.user.entity.User;
import com.kk.school.user.service.UserService;
import com.kk.school.util.ResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
@Setter
@Getter
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FileUtil fileUtil;

    @PostMapping("/login")
    @ResponseBody
    public ResultVo login(@RequestParam("no") String no, @RequestParam("pwd") String pwd, HttpServletRequest request) {
        User user = userService.findUser(no);
        if (user == null) {
            throw new CommonException("用户不存在");
        } else {
            if (pwd.equals(user.getPwd())) {
                Visitor.USER_MAP.put(request.getSession().getId(), user);
            } else {
                throw new CommonException("用户名或密码错误");
            }
        }
        ResultVo resultVo = new ResultVo();
        resultVo.setMsg("登录成功");
        resultVo.setResultCode(Constants.SUC_CODE);
        return resultVo;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultVo register(String param, @RequestParam("the_file") MultipartFile file, HttpServletRequest httpRequest) {

        if(file != null){
            log.info("图片在");
        }else{
            log.info("图片不在");
        }
        if(StringUtils.isBlank(param)){
            throw new CommonException("参数不能为空");
        }
        Map<String,String> map = JSONObject.parseObject(param,Map.class);
        System.out.println(map);
        User user = new User();
        user.setSchNo(map.get("account"));
        user.setPwd(map.get("password"));
        user.setName(map.get("username"));

        if(StringUtils.isBlank(map.get("clazz"))){
            user.setType(0);
            user.setClassId(Integer.parseInt(map.get("clazz")));
        }else{
            user.setType(1);
        }
        //JSONObject.parse(param,HashMap)
        if (user.getType() != 1 && user.getType() != 0) {
            throw new CommonException("类型不能为空");
        }
        if (StringUtils.isBlank(user.getSchNo())) {
            throw new CommonException("学号/工号不能为空");
        }
        if (StringUtils.isBlank(user.getPwd())) {
            throw new CommonException("密码不能威空");
        }
        if (StringUtils.isBlank(user.getName())) {
            throw new CommonException("名字不能威空");
        }
        if (userService.userExist(user.getSchNo())) {
            throw new CommonException("账号已经存在");
        }
        userService.face(user, file);
        try {
            fileUtil.generateImage(file.getBytes(), user.getFaceToken());
        } catch (IOException e) {
            log.error("e", "生成图片失败");
            throw  new CommonException("请重试");
        }
        user.setFacePath(fileUtil.path+File.separator+user.getFaceToken()+".jepg");
        boolean result = userService.saveUser(user);
        ResultVo resultVo = new ResultVo();
        if (result) {
            resultVo.setResultCode(Constants.SUC_CODE);
        } else {
            throw new CommonException("注册失败");
        }
        return resultVo;
    }


}
