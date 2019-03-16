package com.kk.school.user.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kk.school.common.CommonException;
import com.kk.school.common.Constants;
import com.kk.school.common.FacePlus;
import com.kk.school.common.FileUtil;
import com.kk.school.user.entity.User;
import com.kk.school.util.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.MarshalledObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/face")
@Slf4j
public class FaceController {


    @Autowired
    FileUtil fileUtil;
    @RequestMapping("/faceRegister")
    @ResponseBody
    public ResultVo faceRegister(@RequestParam("the_file") MultipartFile file , HttpServletRequest httpRequset) throws IOException {
        ResultVo resultVo = new ResultVo();
        fileUtil.generateImage(file.getBytes(),"xx");
        if (file != null) {
            try {
                BASE64Encoder encoder = new BASE64Encoder();
                // 通过base64来转化图片
                String data = encoder.encode(file.getBytes());
                String  result = FacePlus.post(data);
                if (result != null) {
                    //json转换这里要记得看一看
                    Map<String, Object> map = new HashMap<>();
                    map = JSONObject.parseObject(result, HashMap.class);
                    JSONArray jsonArray = (JSONArray) map.get("faces");
                    User user = new User();
                    if(jsonArray.isEmpty()){
                        throw  new CommonException("请对准人脸");
                    }
                    Map<String, Object> jsonMap = (Map<String, Object>) jsonArray.get(0);
                    if(jsonMap != null) {
                        Map<String, Map<String, Object>> arrMap = (Map<String, Map<String, Object>>) jsonMap.get("attributes");
                        //记得采用jdk 8 的新写法,避免空指针
                        BigDecimal threshold = (BigDecimal) arrMap.get("facequality").get("threshold");
                        BigDecimal value = (BigDecimal) arrMap.get("facequality").get("value");
                        String faceToken = (String) jsonMap.get("face_token");
                        user.setFaceScore(value);
                        user.setFaceToken(faceToken);
                        if (value.compareTo(threshold) < 0) {
                            log.info("value:{}", value);
                            log.info("threshold:{}", threshold);
                            throw new CommonException("人脸识别分数过低");
                        }
                    }else{

                        throw new CommonException("人脸信息获取失败");
                    }
                    resultVo.setResultCode(Constants.SUC_CODE);
                    resultVo.setMsg("识别成功");
                    resultVo.setData(user);
                    log.info("result={}",map);
                }else{
                    resultVo.setResultCode(Constants.ERR_CODE);
                    resultVo.setMsg("人脸识别失败,请重试");
                }
            } catch (Exception e) {
                log.error("人脸识别失败,请重试",e);
                resultVo.setResultCode(Constants.ERR_CODE);
                if(e instanceof  CommonException){
                    resultVo.setMsg(e.getMessage());
                }else{
                    resultVo.setMsg("人脸识别失败");
                }
            }
        }
        return resultVo;
    }



    @RequestMapping("/index")
    public String index(){
        return "face";
    }




}
