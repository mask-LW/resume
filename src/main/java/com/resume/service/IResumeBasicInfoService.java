package com.resume.service;

import com.resume.common.ServerResponse;
import com.resume.pojo.ResumeBasicInfo;

public interface IResumeBasicInfoService {
	ServerResponse saveOrUpdate(Integer userId, ResumeBasicInfo resumeBasicInfo);
	ServerResponse delete(Integer userId,Integer resumeId);
}
