package com.resume.service;

import java.util.List;

import com.resume.common.ServerResponse;
import com.resume.pojo.ResumeBasicInfo;
import com.resume.pojo.ResumeSchoolExp;
import com.resume.pojo.ResumeWorkExp;
import com.resume.vo.PythonResumeVo;
import com.resume.vo.ResumeVo;

public interface IResumeService {
	ServerResponse resume_enter(Integer userId,PythonResumeVo pythonResumeVo);
	ServerResponse getResumeDetail(Integer id) ;
	ServerResponse<ResumeVo> assembleResumeVo(ResumeBasicInfo resumeBasicInfo,List<ResumeSchoolExp>  resumeSchoolExpList,List<ResumeWorkExp>  resumeWorkExpList);
}
