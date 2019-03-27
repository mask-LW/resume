package com.resume.dao;


import java.util.List;

import com.resume.pojo.ResumeWorkExp;

public interface ResumeWorkExpMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(ResumeWorkExp record);

	int insertSelective(ResumeWorkExp record);

	ResumeWorkExp selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ResumeWorkExp record);

	int updateByPrimaryKey(ResumeWorkExp record);

	int deleteByResumeId(Integer resumeId);

	List<ResumeWorkExp> selectByResumeId(Integer id);
}