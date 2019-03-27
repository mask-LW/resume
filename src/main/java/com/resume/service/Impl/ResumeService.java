package com.resume.service.Impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.resume.common.ResponseCode;
import com.resume.common.ServerResponse;
import com.resume.controller.portal.resumeController;
import com.resume.dao.ResumeBasicInfoMapper;
import com.resume.dao.ResumeSchoolExpMapper;
import com.resume.dao.ResumeWorkExpMapper;
import com.resume.pojo.ResumeBasicInfo;
import com.resume.pojo.ResumeSchoolExp;
import com.resume.pojo.ResumeWorkExp;
import com.resume.service.IResumeService;
import com.resume.vo.PythonResumeVo;
import com.resume.vo.ResumeVo;

import ch.qos.logback.classic.Logger;

@Service("iResumeService")
public class ResumeService implements IResumeService {
	private static Logger logger = (Logger) LoggerFactory.getLogger(ResumeService.class);
	
	@Autowired
	private ResumeBasicInfoServiceImpl iResumeBasicInfoService;
	@Autowired
	private ResumeSchoolExpServiceImpl iResumeSchoolExpService;
	@Autowired
	private ResumeWorkExpServiceImpl iResumeWorkExpService;
	
	
	@Autowired
	private ResumeBasicInfoMapper resumeBasicInfoMapper;
	@Autowired
	private ResumeSchoolExpMapper resumeSchoolExpMapper;
	@Autowired
	private ResumeWorkExpMapper resumeWorkExpMapper;
	@Autowired 
	private EsServiceImpl iEsService;
	
	
	
	//获取解析完的简历类，分别录入对应的数据库
	//todo ,要把1改为userid
	public ServerResponse resume_enter(Integer userId,PythonResumeVo pythonResumeVo) {
		if(pythonResumeVo == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		ResumeBasicInfo resumeBasicInfo = new ResumeBasicInfo();
		//组装要插入的ResumeBasicInfo类
		resumeBasicInfo.setName(pythonResumeVo.basic_info.getName());
		resumeBasicInfo.setBorn(pythonResumeVo.basic_info.getBorn());
		resumeBasicInfo.setNation(pythonResumeVo.basic_info.getNation());
		resumeBasicInfo.setNationality(pythonResumeVo.basic_info.getNationality());
		if(pythonResumeVo.basic_info.getSex().equals("男")) {
			resumeBasicInfo.setSex(0);
		}
		else {
			resumeBasicInfo.setSex(1);
		}
		resumeBasicInfo.setCreateTime(new Date());
		//basic_info入库，并获取当前对象的id，即简历id
		ServerResponse re = iResumeBasicInfoService.saveOrUpdate(userId, resumeBasicInfo);
		Integer resumeId = (Integer) re.getData();
		logger.info("新建的id为"+resumeId);
		//若成功，则开始操作学习经历和工作经历类
		
		//edu pro time school degree
		if(re.getStatus() == ResponseCode.SUCCESS.getCode()) {
			//组装要插入的ResumeSchoolExp类
			//todo 取resume_id
			
			for(int i=0; i<pythonResumeVo.school_exp.getPast().size();i++) {
				ResumeSchoolExp resumeSchoolExp = new ResumeSchoolExp();
				//专业、时间、学校都不能为空，专科不用
				if(pythonResumeVo.school_exp.getPast().get(i).getDegree().equals("")) {
					if(!pythonResumeVo.school_exp.getPast().get(i).getEdu().equals("")) {
						resumeSchoolExp.setEdu(pythonResumeVo.school_exp.getPast().get(i).getEdu());
					}
				}else {
					resumeSchoolExp.setEdu(pythonResumeVo.school_exp.getPast().get(i).getDegree());
				}
				logger.info(resumeSchoolExp.getEdu());
				if(!pythonResumeVo.school_exp.getPast().get(i).getPro().equals("")&&
						!pythonResumeVo.school_exp.getPast().get(i).getSchool().equals("")&&
						!pythonResumeVo.school_exp.getPast().get(i).getTime().equals("")) {
					
					resumeSchoolExp.setStatus(0);
					resumeSchoolExp.setPro(pythonResumeVo.school_exp.getPast().get(i).getPro());
					resumeSchoolExp.setCreateTime(new Date());
					resumeSchoolExp.setTime(pythonResumeVo.school_exp.getPast().get(i).getTime());
					resumeSchoolExp.setSchool(pythonResumeVo.school_exp.getPast().get(i).getSchool());
					resumeSchoolExp.setCreateTime(new Date());
					//edu和degree，一般只会说专科、本科（学历）、硕士、博士（学位）=>统一用学历
					
					//时间、专业、学校、学历都不为空时才录入数据库
					if(!resumeSchoolExp.getEdu().equals("")){
						iResumeSchoolExpService.saveOrUpdate(resumeId, resumeSchoolExp);
						logger.info("过去学习经历插入成功一次+id="+resumeSchoolExp.getId());
						}
				
				}
				
			}
		
			
			
			for(int i=0; i<pythonResumeVo.school_exp.getCurrent().size();i++) {
				ResumeSchoolExp resumeSchoolExp = new ResumeSchoolExp();
				if(pythonResumeVo.school_exp.getCurrent().get(i).getDegree().equals("")) {
					if(!pythonResumeVo.school_exp.getCurrent().get(i).getEdu().equals("")) {
						resumeSchoolExp.setEdu(pythonResumeVo.school_exp.getCurrent().get(i).getEdu());
					}
				}else {
					resumeSchoolExp.setEdu(pythonResumeVo.school_exp.getCurrent().get(i).getDegree());
				}
				if(!pythonResumeVo.school_exp.getCurrent().get(i).getPro().equals("")&&
						!pythonResumeVo.school_exp.getCurrent().get(i).getSchool().equals("")&&
						!pythonResumeVo.school_exp.getCurrent().get(i).getTime().equals("")) {
					
					resumeSchoolExp.setStatus(1);
					resumeSchoolExp.setPro(pythonResumeVo.school_exp.getCurrent().get(i).getPro());
					resumeSchoolExp.setCreateTime(new Date());
					resumeSchoolExp.setTime(pythonResumeVo.school_exp.getCurrent().get(i).getTime());
					resumeSchoolExp.setSchool(pythonResumeVo.school_exp.getCurrent().get(i).getSchool());
					//edu和degree，一般只会说专科、本科（学历）、硕士、博士（学位）=>统一用学历
					
					if(!resumeSchoolExp.getEdu().equals("")) {
						iResumeSchoolExpService.saveOrUpdate(resumeId, resumeSchoolExp);
						logger.info("现在学习经历插入成功一次+id="+resumeSchoolExp.getId());
						}
				}
				
			}
			
				//插入学习经历成功，组装要插入的ResumeWorkExp类
				
				
				for(int i=0; i<pythonResumeVo.work_exp.getPast().size();i++) {
					if(!pythonResumeVo.work_exp.getPast().get(i).getJob().equals("")&&
							!pythonResumeVo.work_exp.getPast().get(i).getPlace().equals("")&&
							!pythonResumeVo.work_exp.getPast().get(i).getTime().equals("")) {
					ResumeWorkExp resumeWorkExp = new ResumeWorkExp();
					resumeWorkExp.setStatus(0);
					resumeWorkExp.setCreateTime(new Date());
					resumeWorkExp.setJob(pythonResumeVo.work_exp.getPast().get(i).getJob());
					resumeWorkExp.setTime(pythonResumeVo.work_exp.getPast().get(i).getTime());
					resumeWorkExp.setDepartment(pythonResumeVo.work_exp.getPast().get(i).getDepartment());
					resumeWorkExp.setCompany(pythonResumeVo.work_exp.getPast().get(i).getPlace());
					iResumeWorkExpService.saveOrUpdate(resumeId, resumeWorkExp);
					logger.info("过去工作经历插入成功一次+id="+resumeWorkExp.getId());
					}
				}
				
				
				
				for(int i=0; i<pythonResumeVo.work_exp.getCurrent().size();i++) {
					if(!pythonResumeVo.work_exp.getCurrent().get(i).getJob().equals("")&&
							!pythonResumeVo.work_exp.getCurrent().get(i).getPlace().equals("")&&
							!pythonResumeVo.work_exp.getCurrent().get(i).getTime().equals("")) {
					ResumeWorkExp resumeWorkExp1 = new ResumeWorkExp();
					resumeWorkExp1.setStatus(1);
					resumeWorkExp1.setJob(pythonResumeVo.work_exp.getCurrent().get(i).getJob());
					resumeWorkExp1.setCreateTime(new Date());
					resumeWorkExp1.setTime(pythonResumeVo.work_exp.getCurrent().get(i).getTime());
					resumeWorkExp1.setDepartment(pythonResumeVo.work_exp.getCurrent().get(i).getDepartment());
					resumeWorkExp1.setCompany(pythonResumeVo.work_exp.getCurrent().get(i).getPlace());
					iResumeWorkExpService.saveOrUpdate(resumeId, resumeWorkExp1);
					logger.info("现在工作经历插入成功一次+id="+resumeWorkExp1.getId());
					}
				}
//				return ServerResponse.createBySuccess("入库成功");
				
				//todo
				//建立索引文档
				ResumeBasicInfo resumeBasicInfoNew = resumeBasicInfoMapper.selectByPrimaryKey(resumeId);
				List<ResumeSchoolExp>  resumeSchoolExpListNew = resumeSchoolExpMapper.selectByResumeId(resumeId);
				List<ResumeWorkExp>  resumeWorkExpListNew = resumeWorkExpMapper.selectByResumeId(resumeId);
				iEsService.insert(resumeBasicInfoNew, resumeSchoolExpListNew, resumeWorkExpListNew);
				return ServerResponse.createBySuccessMessage("入库成功且es增加doc成功");
		}
				
		return ServerResponse.createByErrorMessage("basic_info入库失败");
			
	}

	//获取简历详情
	public ServerResponse<ResumeVo> getResumeDetail(Integer id) {
		if(id == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		} 
		ResumeBasicInfo resumeBasicInfo = resumeBasicInfoMapper.selectByPrimaryKey(id);
		List<ResumeSchoolExp>  resumeSchoolExpList = resumeSchoolExpMapper.selectByResumeId(id);
		List<ResumeWorkExp>  resumeWorkExpList = resumeWorkExpMapper.selectByResumeId(id);
		
		
		ServerResponse<ResumeVo> response = this.assembleResumeVo(resumeBasicInfo, resumeSchoolExpList, resumeWorkExpList);
		
		return response;
	}
	
	//组建简历详情类
	public ServerResponse<ResumeVo> assembleResumeVo(ResumeBasicInfo resumeBasicInfo,List<ResumeSchoolExp>  resumeSchoolExpList,List<ResumeWorkExp>  resumeWorkExpList) {
		ResumeVo resumeVo = new ResumeVo();
		
		resumeVo.setResumeId(resumeBasicInfo.getId());
		resumeVo.setUserId(resumeBasicInfo.getUserId());
		resumeVo.setName(resumeBasicInfo.getName());
		resumeVo.setNation(resumeBasicInfo.getNation());
		resumeVo.setNationality(resumeBasicInfo.getNationality());
		resumeVo.setNote(resumeBasicInfo.getNote());
		resumeVo.setSex(resumeBasicInfo.getSex());
		resumeVo.setEmail(resumeBasicInfo.getEmail());
		resumeVo.setPhone(resumeBasicInfo.getPhone());
		resumeVo.setImage(resumeBasicInfo.getImage());
		resumeVo.setBorn(resumeBasicInfo.getBorn());
		resumeVo.setCity(resumeBasicInfo.getCity());
		resumeVo.setExpectJob(resumeBasicInfo.getExpectJob());
		
		resumeVo.setCreateTime(resumeBasicInfo.getCreateTime());
		resumeVo.setUpdateTime(resumeBasicInfo.getUpdateTime());
		
		
		resumeVo.setResumeSchoolExpList(resumeSchoolExpList);
		resumeVo.setResumeWorkExpList(resumeWorkExpList);
		return ServerResponse.createBySuccess(resumeVo);
		
	}
	
	
	
	@RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file",required = false) MultipartFile file,HttpServletRequest request){
		
		return null;
		
	}
}
