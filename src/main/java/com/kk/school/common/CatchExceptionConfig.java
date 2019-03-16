package com.kk.school.common;


import com.kk.school.util.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class CatchExceptionConfig {

    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultVo handle(Exception e) {
        log.error("出现异常",e);
        ResultVo resultVo = new ResultVo() ;
        resultVo.setResultCode(Constants.ERR_CODE);
        resultVo.setMsg(StringUtils.isEmpty(e.getMessage()) ? "系统异常": e.getMessage());
        return  resultVo;
    }


}
