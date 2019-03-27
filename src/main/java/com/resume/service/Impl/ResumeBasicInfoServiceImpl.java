package com.resume.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resume.common.ResponseCode;
import com.resume.common.ServerResponse;
import com.resume.dao.ResumeBasicInfoMapper;
import com.resume.pojo.ResumeBasicInfo;
import com.resume.service.IResumeBasicInfoService;
import com.resume.service.IResumeSchoolExpService;
import com.resume.service.IResumeWorkExpService;

import ch.qos.logback.classic.Logger;

@Service("iResumeBasicInfoService")
public class ResumeBasicInfoServiceImpl implements IResumeBasicInfoService{
	
	@Autowired
	private ResumeBasicInfoMapper resumeBasicInfomapper;
	
	@Autowired
	private IResumeSchoolExpService iResumeSchoolExpService;
	
	@Autowired
	private IResumeWorkExpService iResumeWorkExpService;
	
	@Autowired
	private EsServiceImpl IEsService;
	//������Ϣ��Ψһ�ģ�����ȶԼ���id�����жϣ����ݿ��Զ����ɣ�
	//�����ڣ�����£����򣬲�������
	public ServerResponse saveOrUpdate(Integer userId, ResumeBasicInfo resumeBasicInfo) {
		if(userId == null || resumeBasicInfo==null ) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		resumeBasicInfo.setUserId(userId);
		if(resumeBasicInfo.getId() != null ) {
			int rowCount = resumeBasicInfomapper.updateByPrimaryKeySelective(resumeBasicInfo);
			if( rowCount > 0) {
				return ServerResponse.createBySuccess("���³ɹ�");
			}
			return ServerResponse.createByErrorMessage("����ʧ��");
		}else {
			int rowCount = resumeBasicInfomapper.insert(resumeBasicInfo);
			int id = resumeBasicInfo.getId(); 
			
			if( rowCount > 0) {
				return ServerResponse.createBySuccess("�����ɹ�",id);
			}
			return ServerResponse.createByErrorMessage("����ʧ��");
		}
	}
	
	//������Ϣ��Ψһ��,��ɾ��ʱ�����Ѹ��˼���ɾ������Ҫһ���ѧϰ�����͹��������������Ϣһ��ɾ��
	public ServerResponse delete(Integer userId,Integer resumeId) {
		if(userId == null || resumeId==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		int rowCount = resumeBasicInfomapper.deleteByPrimaryKey(resumeId);
		if( rowCount > 0) {
			//ɾ��ѧϰ����
			ServerResponse response1 = iResumeSchoolExpService.deleteByResumeId(resumeId);
			//ɾ����������
			ServerResponse response2 = iResumeWorkExpService.deleteByResumeId(resumeId);
			//ɾ������
			IEsService.delete(resumeId.toString());
			if(response1.getStatus() == ResponseCode.SUCCESS.getCode() && response2.getStatus() == ResponseCode.SUCCESS.getCode()) {
				return ServerResponse.createBySuccess("ɾ���ɹ�");
			}
			return ServerResponse.createBySuccess("ɾ���ɹ�");
		}
		return ServerResponse.createByErrorMessage("ɾ��ʧ��");
	}
	
}
