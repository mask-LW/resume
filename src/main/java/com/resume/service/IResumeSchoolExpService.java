package com.resume.service;

import com.resume.common.ServerResponse;
import com.resume.pojo.ResumeSchoolExp;

public interface IResumeSchoolExpService {

	
	ServerResponse saveOrUpdate(Integer resumeId, ResumeSchoolExp resumeSchoolExp);
	ServerResponse delete(Integer id);
	ServerResponse deleteByResumeId(Integer resumeId);
}
