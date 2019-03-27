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
 * ����Աģ��
 * 1.��ͨ�û�����ɾ�Ĳ�
 * 2.todo ��������
 * @author mac
 *
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {
	@Autowired
	private IUserService iUserService;
	
	/**
	 * ����Ա��½
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
                //˵����¼���ǹ���Ա
                session.setAttribute(Const.CURRENT_USER,user);
                System.out.print("��½�ɹ�");
                return response;
            }else{
                return ServerResponse.createByErrorMessage("���ǹ���Ա,�޷���¼");
            }
        }
        return response;
    }
	
	/**
	 * ����Աע��
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
	 * ��ͨ�û��б�
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
	        	 return ServerResponse.createByErrorMessage("�û�Ȩ�޲���");
	        }
	       return iUserService.list(Const.Role.ROLE_CUSTOMER);
	}
	
	/**
	 * ͨ��id�����û�
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
	        	 return ServerResponse.createByErrorMessage("�û�Ȩ�޲���");
	        }
	       return iUserService.queryById(userId);
	}
	/**
	 * ͨ��username �����û�
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
	        	 return ServerResponse.createByErrorMessage("�û�Ȩ�޲���");
	        }
	       return iUserService.queryByName(username);
	}
	
	
	/**
	 * ����Ա������ͨ�û���Ϣ
	 */
	@RequestMapping(value="update.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse update(HttpSession session,Integer userId, User customer) {
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	        if(user ==null){
	            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	        }
	        if(user.getRole() != Const.Role.ROLE_ADMIN) {
	        	 return ServerResponse.createByErrorMessage("�û�Ȩ�޲���");
	        }
	       return iUserService.update(userId,customer);
	}
	
	/**
	 * ɾ���û�
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
	        	 return ServerResponse.createByErrorMessage("�û�Ȩ�޲���");
	        }
	       return iUserService.delete(userId);
	}
}
