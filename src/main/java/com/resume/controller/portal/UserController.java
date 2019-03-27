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
 * �û�ģ��
 * ע�ᡢ��½��ע�����鿴������Ϣ���޸ĸ�����Ϣ���һ����루��ѯ���⡢���𰸡��������룩
 * @author mac
 *	����md5����
 */

@Controller
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private IUserService iUserService;
	
	
	/**
	 * 	�û���½ 
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
			//���û�����session
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		
		return response;
	}
	
	/**
	 * �û�ע��
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
	 * �û�ע��
	 * @param user
	 * @return
	 */
	@RequestMapping(value="register.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse register(User user) {
		
		return iUserService.register(user);
	}
	
	/**
	 * ʵʱУ���û�����email�� �绰�Ƿ����
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
	 * ��ȡ�û���Ϣ
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
		return ServerResponse.createByErrorMessage("�û�δ��¼���޷���ȡ��ǰ�û�����Ϣ");
	}
	
	/**
	 * ѡ���һ����������
	 * @param username
	 * @return
	 */
	@RequestMapping(value="forget_get_question.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		return iUserService.selectQuestion(username);
		
	}
	
	
	/**
	 * ʹ�ñ��ػ���У���һ�����Ĵ��Ƿ���ȷ
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
	 * ��������
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
	 * ��½״̬����������
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
	 * ���¸�����Ϣ
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
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"δ��¼,��Ҫǿ�Ƶ�¼status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }

}
