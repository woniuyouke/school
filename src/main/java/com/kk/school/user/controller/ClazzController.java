package com.kk.school.user.controller;


import com.kk.school.common.Constants;
import com.kk.school.user.entity.Clazz;
import com.kk.school.user.service.ClazzService;
import com.kk.school.util.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClazzController {


    @Autowired
    ClazzService clazzService;

    @RequestMapping("/listClass")
    @ResponseBody
    public ResultVo listClass() {
        List<Clazz> clazzes = clazzService.listClazz();
        ResultVo resultVo = new ResultVo();
        resultVo.setData(clazzes);
        resultVo.setResultCode(Constants.SUC_CODE);
        return resultVo;
    }

}
