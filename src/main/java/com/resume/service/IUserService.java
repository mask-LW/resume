package com.resume.service;

import com.resume.common.ServerResponse;
import com.resume.pojo.User;

/**
 * 
 * @author mac
 *
 */

public interface IUserService {
	
	
	ServerResponse login( String username,String password);
	ServerResponse<String> register(User user);
	ServerResponse<String> checkValid(String str,String type);
	ServerResponse selectQuestion(String username);
	ServerResponse<String> checkAnswer(String username,String question,String answer);
	ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);
	ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);
	ServerResponse<User> updateInformation(User user);
	ServerResponse<User> getInformation(Integer id);
	
	ServerResponse queryByName(String username);
	ServerResponse queryById(Integer userId);
	ServerResponse list(int roleCustomer);
	ServerResponse update(Integer userId, User customer);
	ServerResponse delete(Integer userId);
}
