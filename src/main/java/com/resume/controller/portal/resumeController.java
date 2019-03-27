package com.resume.controller.portal;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.resume.common.Const;
import com.resume.common.ResponseCode;
import com.resume.common.ServerResponse;
import com.resume.pojo.ResumeBasicInfo;
import com.resume.pojo.ResumeSchoolExp;
import com.resume.pojo.ResumeWorkExp;
import com.resume.pojo.User;
import com.resume.service.Impl.EsServiceImpl;
import com.resume.service.Impl.FileService;
import com.resume.service.Impl.ResumeBasicInfoServiceImpl;
import com.resume.service.Impl.ResumeSchoolExpServiceImpl;
import com.resume.service.Impl.ResumeService;
import com.resume.service.Impl.ResumeWorkExpServiceImpl;
import com.resume.util.JSONUtil;
import com.resume.util.PropertiesUtil;
import com.resume.vo.PythonResumeVo;
/**
 * to do ：python端的接口 、 支付模块 、订单模块、简历导出作为pdf
 * 要规定日期的格式
 * 主要功能：
 * 1.组合简历显示下的增删改功能
 * 2.简历上传 -> 调用算法解析入库 -> 生成es索引
 * @author mac
 *问题：应该针对哪类型数据进行处理
 */
@Controller
@RequestMapping("/resume/")
public class resumeController {
	private static Logger logger = LoggerFactory.getLogger(resumeController.class);
	private String resume[];
	private int count=1;
	@Autowired
	private ResumeService iResumeService;
	@Autowired
	private FileService iFileService;
	@Autowired
	private EsServiceImpl iEsService;
	@Autowired
	private ResumeBasicInfoServiceImpl iResumeBasicInfoService;
	@Autowired
	private ResumeSchoolExpServiceImpl iResumeSchoolExpService;
	@Autowired
	private ResumeWorkExpServiceImpl iResumeWorkExpService;
	/**
	 * 解析简历并入库，生成索引
	 * @param session
	 * @return
	 * todo 
		 * 1.将json分别入库 => json转为实体类 =>成功转为PythonResume类 => todo 分别存入库
		 * 2.将三个表的service层简单实现
		 * 3.由三个表的数据建立索引，存入es
		 * 4.将三个表组成一个大的resume类，作为个人完整简历的显示类
	 * @throws IOException 
	 */
	@RequestMapping("parse.do")
	@ResponseBody
	public ServerResponse parseResume(HttpSession session) throws IOException {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		//数据取1～count-1
		if(resume != null) {
			for(int i=1;i<count;i++) {
				ServerResponse<PythonResumeVo> response = JSONUtil.send(resume[i]);
				logger.info("*********解析简历成功*********");
				PythonResumeVo pythonResumeVo =  response.getData();
				iResumeService.resume_enter(user.getId(),pythonResumeVo);
				logger.info("*********成功录入一份简历*********");
			}
			//解析后数组清空+count置1
			count = 1;
			resume = null;
			return ServerResponse.createBySuccess("解析成功");
		}
		return ServerResponse.createByErrorMessage("简历为空");
		
	}
	
	/**
	 * 组合简历详情
	 * @param session
	 * @param resume_id
	 * @return
	 */
	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse<?> getResumeDetail(HttpSession session,Integer resumeId) {
		//判断用户，然后调用显示一个简历具体信息
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iResumeService.getResumeDetail(resumeId);
		
	}
	
	/**
	 * 手动录入ResumeBasicInfo基本信息类
	 * 组合显示页面下修改教育经历
	 * 未定：哪个位置手动录入
	 * @param session
	 * @param resume_id
	 * @return
	 */
	@RequestMapping("insert_or_update_basic_info.do")
	@ResponseBody
	public ServerResponse insertOrUpdateBasicInfo(HttpSession session,ResumeBasicInfo resumeBasicInfo) {
		//判断用户，然后调用显示一个简历具体信息
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iResumeBasicInfoService.saveOrUpdate(user.getId(), resumeBasicInfo);
	}

	
	
	/**
	 * 手动录入ResumeSchoolExp基本信息类
	 * 组合显示页面下增加或修改教育经历
	 * @param session
	 * @param resumeSchoolExp
	 * @return
	 */
	@RequestMapping("insert_or_update_school_exp.do")
	@ResponseBody
	public ServerResponse insertOrUpdateSchoolExp(HttpSession session,Integer resumeId,ResumeSchoolExp resumeSchoolExp ) {
		//判断用户，然后调用显示一个简历具体信息
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iResumeSchoolExpService.saveOrUpdate(resumeId, resumeSchoolExp);
	}
	
	
	/**
	 * 手动录入ResumeWorkExp基本信息类
	 * 组合显示页面下增加或修改工作经历
	 * @param session
	 * @param resumeWorkExp
	 * @return
	 */
	@RequestMapping("insert_or_update_work_exp.do")
	@ResponseBody
	public ServerResponse insertOrUpdateWorkExp(HttpSession session,Integer resumeId,ResumeWorkExp resumeWorkExp ) {
		//判断用户，然后调用显示一个简历具体信息
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iResumeWorkExpService.saveOrUpdate(resumeId, resumeWorkExp);
	}
	
	/**
	 * 删除单条工作经历类
	 * 组合显示页面下删除工作经历
	 * @param session
	 * @param resumeId
	 * @return
	 */
	@RequestMapping("delete_work_exp.do")
	@ResponseBody
	public ServerResponse deleteWorkExp(HttpSession session,Integer resumeId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iResumeWorkExpService.delete(resumeId);
		
	}
	
	/**
	 * 删除学习工作经历类
	 * 组合显示页面下删除单条学习经历
	 * @param session
	 * @param resumeId
	 * @return
	 */
	@RequestMapping("delete_school_exp.do")
	@ResponseBody
	public ServerResponse deleteSchoolExp(HttpSession session,Integer resumeId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iResumeSchoolExpService.delete(resumeId);
	}
	
	
	/**
	 * 删除个人信息即代表删除整个简历
	 */
	@RequestMapping("delete_resume.do")
	@ResponseBody
	public ServerResponse deleteBasicInfo(HttpSession session,Integer resumeId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iResumeBasicInfoService.delete(user.getId(),resumeId);
	        
	}
	
	
	
	/**
	 * 上传整个简历
	 */
	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse upload(HttpSession session,MultipartFile file,HttpServletRequest request) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		String path  = request.getSession().getServletContext().getRealPath("upload");
		logger.info("path"+path);
		//上传并返回文件名
		String targetFileName = iFileService.upload(file, path);
		logger.info("targetFileName"+targetFileName);
		//获取ftp的url
		String url = PropertiesUtil.getProperty("http://img.happymmall.com/")+targetFileName;
//		Map<String, String> fileMap  = new HashMap<String, String>();
//		fileMap.put("url", targetFileName);
//		fileMap.put("url", url);
		
		logger.info("开始读取文件");
		
		ServerResponse  sr = iFileService.readFile(path+"/"+targetFileName);
		
		if(sr.getStatus() == ResponseCode.SUCCESS.getCode()) {
		resume = (String[]) sr.getData();
		logger.info("读取后");
			for(int i=0; i< resume.length;i++) {
				if(resume[i]!= null) {
					count++;
				}
				logger.info(resume[i]);
			}
			if(count > 100) {
				return ServerResponse.createByErrorCodeMessage(ResponseCode.FILE_TOO_LARGE.getCode(), "重复上传简历大于100份");
			}
			return ServerResponse.createBySuccess("上传文件成功", count);
			
			
			
		}else {
			return ServerResponse.createByErrorMessage(sr.getMsg());
		}
	}
	//todo 
	/**
	 * 搭建ftp服务器
	 * 解析读取文件流
	 * 上传文件的前端	get☑️
	 */
	
	
	/**
	 * 图表分析
	 * 看视频
	 * 做图表️
	 */
	
	
	
	
	
	
	
	
	
}
