package com.resume.dao;

import com.resume.pojo.ResumeBasicInfo;

public interface ResumeBasicInfoMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(ResumeBasicInfo record);

	int insertSelective(ResumeBasicInfo record);

	ResumeBasicInfo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ResumeBasicInfo record);

	int updateByPrimaryKey(ResumeBasicInfo record);

	Integer selectIdByOther(ResumeBasicInfo record);
	
	
}