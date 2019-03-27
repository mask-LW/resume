package com.resume.service.Impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resume.common.Const;
import com.resume.common.ServerResponse;
import com.resume.common.TokenCache;
import com.resume.dao.UserMapper;
import com.resume.pojo.User;
import com.resume.service.IUserService;
import com.resume.util.MD5Util;

/**
 * 
 * @author mac
 *
 */

@Service("iUserService")
public class UserServiceImpl implements IUserService {
	
	
	@Autowired
	private UserMapper userMapper;
	
	
	@Override
	public ServerResponse<User> login( String username,String password) {
		
		int resultCount = userMapper.checkUserName(username);
		if(resultCount == 0 ) {
			return ServerResponse.createByErrorMessage("�û���������");
		}
		
		//md5����
		String md5Password = MD5Util.MD5EncodeUtf8(password);
		User user = userMapper.selectLogin(username, md5Password);
		if(user == null ) {
			return ServerResponse.createByErrorMessage("�������");
		}
		
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		
		return ServerResponse.createBySuccess("��½�ɹ�", user);
	}
	
	
	public ServerResponse<String> register(User user) {
		//У���û�����email���绰�Ƿ����
		int resultCount = userMapper.checkUserName(user.getUsername());
		if(resultCount > 0 ) {
			return ServerResponse.createByErrorMessage("�û����Ѵ���");
		}
		int resultCount1 = userMapper.checkEmail(user.getEmail());
		if(resultCount1 > 0 ) {
			return ServerResponse.createByErrorMessage("email�Ѵ���");
		}
		int resultCount2 = userMapper.checkPhone(user.getPhone());
		if(resultCount2 > 0 ) {
			return ServerResponse.createByErrorMessage("�绰�����Ѵ���");
		}
		
		user.setRole(Const.Role.ROLE_CUSTOMER);
		
		//md5�������
		
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		
		resultCount = userMapper.insert(user);
		if(resultCount == 0) {
			return ServerResponse.createByErrorMessage("ע��ʧ��");
		}
		
		return ServerResponse.createBySuccess("ע��ɹ�");
		
	}
	
	
	public ServerResponse<String> checkValid(String str,String type) {
		if(StringUtils.isNotBlank(type)) {
			//��ʼУ��
			if(Const.USERNAME.equals(type)) {
				int resultCount = userMapper.checkUserName(str);
				if(resultCount > 0 ) {
					return ServerResponse.createByErrorMessage("�û����Ѵ���");
				}
			}
			if(Const.EMAIL.equals(type)) {
				int resultCount = userMapper.checkUserName(str);
				if(resultCount > 0 ) {
					return ServerResponse.createByErrorMessage("email�Ѵ���");
				}
			}
			if(Const.PHONE.equals(type)) {
				int resultCount = userMapper.checkUserName(str);
				if(resultCount > 0 ) {
					return ServerResponse.createByErrorMessage("�绰�����Ѵ���");
				}
			}
		}else {
			return ServerResponse.createByErrorMessage("��������");
		}
		return ServerResponse.createBySuccess("У��ɹ�");
	}
	
	
	public ServerResponse<String> selectQuestion(String username){

        ServerResponse<?> validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //�û�������
            return ServerResponse.createByErrorMessage("�û�������");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("�һ�����������ǿյ�");
    }
	
	 public ServerResponse<String> checkAnswer(String username,String question,String answer){
	        int resultCount = userMapper.checkAnswer(username,question,answer);
	        if(resultCount>0){
	            //˵�����⼰�����������û���,��������ȷ��
	            String forgetToken = UUID.randomUUID().toString();
	            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
	            return ServerResponse.createBySuccess(forgetToken);
	        }
	        return ServerResponse.createByErrorMessage("����Ĵ𰸴���");
	    }


	@Override
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("��������,token��Ҫ����");
        }
        ServerResponse<?> validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //�û�������
            return ServerResponse.createByErrorMessage("�û�������");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token��Ч���߹���");
        }

        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("�޸�����ɹ�");
            }
        }else{
            return ServerResponse.createByErrorMessage("token����,�����»�ȡ���������token");
        }
        return ServerResponse.createByErrorMessage("�޸�����ʧ��");
    }


	@Override
	   public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //��ֹ����ԽȨ,ҪУ��һ������û��ľ�����,һ��Ҫָ��������û�.��Ϊ���ǻ��ѯһ��count(1),�����ָ��id,��ô�������true��count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("���������");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("������³ɹ�");
        }
        return ServerResponse.createByErrorMessage("�������ʧ��");
    }


	@Override
	 public ServerResponse<User> updateInformation(User user){
        //username�ǲ��ܱ����µ�
        //emailҲҪ����һ��У��,У���µ�email�ǲ����Ѿ�����,���Ҵ��ڵ�email�����ͬ�Ļ�,���������ǵ�ǰ������û���.
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("email�Ѵ���,�����email�ٳ��Ը���");
        }
        int resultCount1 = userMapper.checkPhoneByUserId(user.getEmail(),user.getId());
        if(resultCount1 > 0){
            return ServerResponse.createByErrorMessage("�õ绰�����Ѵ���,������绰�����ٳ��Ը���");
        }
        //У��email�� phone�Ƿ������û�ռ��
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("���¸�����Ϣ�ɹ�",updateUser);
        }
        return ServerResponse.createByErrorMessage("���¸�����Ϣʧ��");
    }


	@Override
	  public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("�Ҳ�����ǰ�û�");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);

    }


	@Override
	public ServerResponse<List<User>> list(int role) {
		List<User> user = userMapper.selectByRole(role);
		return ServerResponse.createBySuccess(user);
	}


	@Override
	public ServerResponse<User> queryByName(String username) {
		if(StringUtils.isNotBlank(username)) {
			User user = userMapper.selectByUserName(username);
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("��������");
	}


	@Override
	public ServerResponse<User> queryById(Integer userId) {
		if(userId != null) {
			User user = userMapper.selectByPrimaryKey(userId);
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("��������");
	}


	@Override
	public ServerResponse<String> update(Integer userId, User customer) {
		if(userId != null || customer!= null) {
			User user = userMapper.selectByPrimaryKey(userId);
			
			user.setUsername(customer.getUsername());
			user.setPhone(customer.getPhone());
			user.setAnswer(customer.getAnswer());
			user.setQuestion(customer.getQuestion());
			user.setUpdateTime(new Date());
			user.setRole(customer.getRole());
			user.setEmail(customer.getEmail());
			user.setJob(customer.getJob());
		
			
			int rowCount = userMapper.updateByPrimaryKeySelective(user);
			if(rowCount > 0) {
				return ServerResponse.createBySuccess("���³ɹ�");
			}
			return ServerResponse.createByErrorMessage("����ʧ��");
		}
		
		return ServerResponse.createByErrorMessage("��������");
	}


	@Override
	public ServerResponse<String> delete(Integer userId) {
		if(userId != null) {
			int rowCount = userMapper.deleteByPrimaryKey(userId);
			if(rowCount > 0) {
				return ServerResponse.createBySuccess("ɾ���ɹ�");
			}
			return ServerResponse.createByErrorMessage("ɾ��ʧ��");
		}
		return ServerResponse.createByErrorMessage("��������");
		
	}


	

	
	 

}