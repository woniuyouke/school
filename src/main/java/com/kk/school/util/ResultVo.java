package com.kk.school.util;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultVo {
    private int resultCode;
    private String msg;
    Object data;
}
