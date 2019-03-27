package com.resume.dao;


import java.util.List;

import com.resume.pojo.ResumeSchoolExp;

public interface ResumeSchoolExpMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(ResumeSchoolExp record);

	int insertSelective(ResumeSchoolExp record);

	ResumeSchoolExp selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ResumeSchoolExp record);

	int updateByPrimaryKey(ResumeSchoolExp record);

	int deleteByResumeId(Integer resumeId);
	
	List<ResumeSchoolExp> selectByResumeId(Integer resumeId);
}