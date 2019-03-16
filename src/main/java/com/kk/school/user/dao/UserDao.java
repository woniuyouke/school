package com.kk.school.user.dao;

import com.kk.school.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from t_user t where t.schNo = #{schNo}")
    User findUser(String schNo);

    @Insert("insert into t_user (name,mobile,schNo,faceScore,faceTime,createTime,facePath,faceToken,classId,pwd,type) values (#{name},#{mobile},#{schNo},#{faceScore},now(),now(),#{facePath},#{faceToken},#{classId},#{pwd},#{type})")
    int saveUser(User user);



}
