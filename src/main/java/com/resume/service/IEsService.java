package com.resume.service;

import java.util.List;
import java.util.Map;

import com.resume.common.ServerResponse;
import com.resume.pojo.ResumeBasicInfo;
import com.resume.pojo.ResumeSchoolExp;
import com.resume.pojo.ResumeWorkExp;

public interface IEsService {
	ServerResponse insert(ResumeBasicInfo resumeBasicInfo,
			List<ResumeSchoolExp>  resumeSchoolExpList,
			List<ResumeWorkExp>  resumeWorkExpList) ;
	
	ServerResponse<List<Map<String, Object>>> queryString(Integer userId,String string);
	ServerResponse<List<Map<String, Object>>> getByName(Integer userId,String name) ;
	ServerResponse<List<Map<String, Object>>> getList(Integer userId);
	ServerResponse<List<Map<String, Object>>> multipleQuery(Integer userId,String company,
			String job,String edu,String gender,
			Integer gt_age , Integer lt_age);
	
	ServerResponse<Map<String, Object>> aggGroupByField(Integer userId,String field);
	ServerResponse<Map<String, Object>>  aggGroupByEduInMan(Integer userId);
	ServerResponse<Map<String, Object>>  aggGroupByEduInWomen(Integer userId);
	 ServerResponse<Map<String, Object>> aggGroupByEduInGenderAndAge(Integer userId);
	 ServerResponse<List<Map<String, Object>>> getByNote(Integer userId,String note);
	 ServerResponse<List> getNoteList(Integer userId);
	 ServerResponse addNote(Integer userId, Integer resumeId, String note);
	 ServerResponse delete( String resumeId);
	 
	 ServerResponse suggestName(String namePrefix);
}
