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
	//个人信息是唯一的，因此先对简历id进行判断（数据库自动生成）
	//若存在，则更新，否则，插入数据
	public ServerResponse saveOrUpdate(Integer userId, ResumeBasicInfo resumeBasicInfo) {
		if(userId == null || resumeBasicInfo==null ) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		resumeBasicInfo.setUserId(userId);
		if(resumeBasicInfo.getId() != null ) {
			int rowCount = resumeBasicInfomapper.updateByPrimaryKeySelective(resumeBasicInfo);
			if( rowCount > 0) {
				return ServerResponse.createBySuccess("更新成功");
			}
			return ServerResponse.createByErrorMessage("更新失败");
		}else {
			int rowCount = resumeBasicInfomapper.insert(resumeBasicInfo);
			int id = resumeBasicInfo.getId(); 
			
			if( rowCount > 0) {
				return ServerResponse.createBySuccess("新增成功",id);
			}
			return ServerResponse.createByErrorMessage("新增失败");
		}
	}
	
	//个人信息是唯一的,则删除时，即把个人简历删除，需要一起把学习经历和工作经历对相关信息一起删除
	public ServerResponse delete(Integer userId,Integer resumeId) {
		if(userId == null || resumeId==null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		int rowCount = resumeBasicInfomapper.deleteByPrimaryKey(resumeId);
		if( rowCount > 0) {
			//删除学习经历
			ServerResponse response1 = iResumeSchoolExpService.deleteByResumeId(resumeId);
			//删除工作经历
			ServerResponse response2 = iResumeWorkExpService.deleteByResumeId(resumeId);
			//删除索引
			IEsService.delete(resumeId.toString());
			if(response1.getStatus() == ResponseCode.SUCCESS.getCode() && response2.getStatus() == ResponseCode.SUCCESS.getCode()) {
				return ServerResponse.createBySuccess("删除成功");
			}
			return ServerResponse.createBySuccess("删除成功");
		}
		return ServerResponse.createByErrorMessage("删除失败");
	}
	
}
