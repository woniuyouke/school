package com.kk.school.user.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class User {
    private int id;
    private String schNo;
    private String name;
    private String mobile;
    private int type;
    private int classId;
    private BigDecimal faceScore;
    private String facePath;
    private Date faceTime;
    private String faceToken;
    private String pwd;
    private Date createTime;

}
