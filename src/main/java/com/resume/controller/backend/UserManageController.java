package com.resume.controller.backend;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resume.common.Const;
import com.resume.common.ResponseCode;
import com.resume.common.ServerResponse;
import com.resume.pojo.User;
import com.resume.service.IUserService;

/**
 * 管理员模块
 * 1.普通用户的增删改查
 * 2.todo 订单管理
 * @author mac
 *
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {
	@Autowired
	private IUserService iUserService;
	
	/**
	 * 管理员登陆
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //说明登录的是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                System.out.print("登陆成功");
                return response;
            }else{
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }
	
	/**
	 * 管理员注销
	 * @param session
	 * @return
	 */
	@RequestMapping(value="logout.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse login( HttpSession session) {
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}
	
	/**
	 * 普通用户列表
	 * @param session
	 * @return
	 */
	@RequestMapping(value="list.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse list(HttpSession session) {
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	        if(user ==null){
	            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	        }
	        if(user.getRole() != Const.Role.ROLE_ADMIN) {
	        	 return ServerResponse.createByErrorMessage("用户权限不够");
	        }
	       return iUserService.list(Const.Role.ROLE_CUSTOMER);
	}
	
	/**
	 * 通过id查找用户
	 * @param session
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="query_by_id.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse queryById(HttpSession session,Integer userId) {
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	        if(user ==null){
	            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	        }
	        if(user.getRole() != Const.Role.ROLE_ADMIN) {
	        	 return ServerResponse.createByErrorMessage("用户权限不够");
	        }
	       return iUserService.queryById(userId);
	}
	/**
	 * 通过username 查找用户
	 * @param session
	 * @param username
	 * @return
	 */
	@RequestMapping(value="query_by_name.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse queryByName(HttpSession session,String username) {
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	        if(user ==null){
	            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	        }
	        if(user.getRole() != Const.Role.ROLE_ADMIN) {
	        	 return ServerResponse.createByErrorMessage("用户权限不够");
	        }
	       return iUserService.queryByName(username);
	}
	
	
	/**
	 * 管理员更新普通用户信息
	 */
	@RequestMapping(value="update.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse update(HttpSession session,Integer userId, User customer) {
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	        if(user ==null){
	            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	        }
	        if(user.getRole() != Const.Role.ROLE_ADMIN) {
	        	 return ServerResponse.createByErrorMessage("用户权限不够");
	        }
	       return iUserService.update(userId,customer);
	}
	
	/**
	 * 删除用户
	 * @param session
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="delete.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse delete(HttpSession session,Integer userId) {
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	        if(user ==null){
	            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	        }
	        if(user.getRole() != Const.Role.ROLE_ADMIN) {
	        	 return ServerResponse.createByErrorMessage("用户权限不够");
	        }
	       return iUserService.delete(userId);
	}
}
