package com.resume.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resume.pojo.User;


public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	int checkUserName(String username);
	
	int checkEmail(String email);
	
	int checkPhone(String phone);
	
    User selectLogin(@Param("username")String username,@Param("password")String password);

	String selectQuestionByUsername(String username);

	int checkAnswer(@Param("username")String username, @Param("question")String question, @Param("answer")String answer);

	int updatePasswordByUsername(@Param("username")String username,@Param("password")String md5Password);

	int checkPassword(@Param("password")String password,@Param("userId") Integer id);

	int checkEmailByUserId(@Param("email")String email, @Param("userId") Integer id);

	int checkPhoneByUserId(@Param("phone")String phone, @Param("userId") Integer id);

	List<User> selectByRole(int role);

	User selectByUserName(String username); 
}