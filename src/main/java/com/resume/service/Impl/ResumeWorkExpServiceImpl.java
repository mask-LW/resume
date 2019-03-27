package com.resume.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resume.common.ResponseCode;
import com.resume.common.ServerResponse;
import com.resume.dao.ResumeWorkExpMapper;
import com.resume.pojo.ResumeWorkExp;
import com.resume.service.IResumeWorkExpService;

@Service("iResumeWorkExpService")
public class ResumeWorkExpServiceImpl  implements IResumeWorkExpService{
	@Autowired
	private ResumeWorkExpMapper resumeWorkExpMapper;
	
	public ServerResponse saveOrUpdate(Integer resumeId, ResumeWorkExp resumeWorkExp) {
		
		if(resumeId == null ||  resumeWorkExp == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		resumeWorkExp.setResumeId(resumeId);
		if(resumeWorkExp.getId() != null) {
			int rowCount = resumeWorkExpMapper.updateByPrimaryKey(resumeWorkExp);
			if( rowCount > 0) {
				return ServerResponse.createBySuccess("���³ɹ�");
			}
			return ServerResponse.createByErrorMessage("����ʧ��");
		}
		else {
			int rowCount = resumeWorkExpMapper.insert(resumeWorkExp);
			if( rowCount > 0) {
				return ServerResponse.createBySuccess("�����ɹ�");
			}
			return ServerResponse.createByErrorMessage("����ʧ��");
		}
		
		
	}

	//��������ɾ��
	public ServerResponse delete(Integer id) {
		if(id == null ) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		int rowCount = resumeWorkExpMapper.deleteByPrimaryKey(id);
		if( rowCount > 0) {
			return ServerResponse.createBySuccess("ɾ���ɹ�");
		}
		return ServerResponse.createByErrorMessage("ɾ��ʧ��");
	}

	
	//����ɾ��
		public ServerResponse deleteByResumeId(Integer resumeId) {
			if(resumeId == null ) {
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
			}
			int rowCount = resumeWorkExpMapper.deleteByResumeId(resumeId);
			if( rowCount > 0) {
				return ServerResponse.createBySuccess("ɾ���ɹ�");
			}
			return ServerResponse.createByErrorMessage("ɾ��ʧ��");
			
		}

}
