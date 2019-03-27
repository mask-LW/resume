package com.resume.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resume.common.ResponseCode;
import com.resume.common.ServerResponse;
import com.resume.dao.ResumeSchoolExpMapper;
import com.resume.pojo.ResumeSchoolExp;
import com.resume.service.IResumeSchoolExpService;

@Service("iResumeSchoolExpService")
public class ResumeSchoolExpServiceImpl implements IResumeSchoolExpService{
	@Autowired
	private ResumeSchoolExpMapper resumeSchoolExpMapper;
	
	public ServerResponse saveOrUpdate(Integer resumeId, ResumeSchoolExp resumeSchoolExp) {
		if(resumeId == null ||  resumeSchoolExp == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		resumeSchoolExp.setResumeId(resumeId);
		if(resumeSchoolExp.getId() != null) {
			int rowCount = resumeSchoolExpMapper.updateByPrimaryKey(resumeSchoolExp);
			if( rowCount > 0) {
				return ServerResponse.createBySuccess("���³ɹ�");
			}
			return ServerResponse.createByErrorMessage("����ʧ��");
		}
		else {
			int rowCount = resumeSchoolExpMapper.insert(resumeSchoolExp);
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
		int rowCount = resumeSchoolExpMapper.deleteByPrimaryKey(id);
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
		int rowCount = resumeSchoolExpMapper.deleteByResumeId(resumeId);
		if( rowCount > 0) {
			return ServerResponse.createBySuccess("ɾ���ɹ�");
		}
		return ServerResponse.createByErrorMessage("ɾ��ʧ��");
		
	}

}
