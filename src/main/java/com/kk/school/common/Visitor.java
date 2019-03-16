package com.kk.school.common;

import com.kk.school.user.entity.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Visitor {

    public static ThreadLocal<Map<String,String>>  LOCAL = new ThreadLocal<>();

    public static Map<String,User> USER_MAP = new ConcurrentHashMap<>();
}
