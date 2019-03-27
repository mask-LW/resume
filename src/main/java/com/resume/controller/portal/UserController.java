package com.resume.controller.portal;

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
 * 用户模块
 * 注册、登陆、注销、查看个人信息、修改个人信息、找回密码（查询问题、检查答案、重设密码）
 * @author mac
 *	密码md5加密
 */

@Controller
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private IUserService iUserService;
	
	
	/**
	 * 	用户登陆 
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login( String username,String password,HttpSession session) {
		ServerResponse<User> response = iUserService.login(username, password);
		if(response.isSuccess()) {
			//将用户存入session
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		
		return response;
	}
	
	/**
	 * 用户注销
	 * @param session
	 * @return
	 */
	@RequestMapping(value="logout.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse logout( HttpSession session) {
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping(value="register.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse register(User user) {
		
		return iUserService.register(user);
	}
	
	/**
	 * 实时校验用户名、email、 电话是否存在
	 * @param str
	 * @param type
	 * @return
	 */
	@RequestMapping(value="check_valid.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return iUserService.checkValid(str, type);
	}
	
	/**
	 * 获取用户信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value="get_user_info.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if( user != null ) {
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
	}
	
	/**
	 * 选择找回密码的问题
	 * @param username
	 * @return
	 */
	@RequestMapping(value="forget_get_question.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		return iUserService.selectQuestion(username);
		
	}
	
	
	/**
	 * 使用本地缓存校验找回问题的答案是否正确
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	@RequestMapping(value="forget_get_check_answer.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
		return iUserService.checkAnswer(username, question, answer);
	}
	
	/**
	 * 重置密码
	 * @param username
	 * @param passwordNew
	 * @param forgetToken
	 * @return
	 */
	@RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
	    return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
	}
	
	/**
	 * 登陆状态的重置密码
	 * @param session
	 * @param passwordOld
	 * @param passwordNew
	 * @return
	 */
	@RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
        	return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }
	
	/**
	 * 更新个人信息
	 * @param session
	 * @param user
	 * @return
	 */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
        	return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    

    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }

}
