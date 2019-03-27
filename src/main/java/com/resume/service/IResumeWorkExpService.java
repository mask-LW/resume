package com.resume.service;

import com.resume.common.ServerResponse;
import com.resume.pojo.ResumeWorkExp;

public interface IResumeWorkExpService {

	
	ServerResponse saveOrUpdate(Integer resumeId, ResumeWorkExp resumeWorkExp);
	ServerResponse delete(Integer id);
	 ServerResponse deleteByResumeId(Integer resumeId);
}
