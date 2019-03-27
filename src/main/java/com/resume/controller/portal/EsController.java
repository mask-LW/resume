package com.resume.controller.portal;

import javax.servlet.http.HttpSession;

import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resume.common.Const;
import com.resume.common.ResponseCode;
import com.resume.common.ServerResponse;
import com.resume.pojo.User;
import com.resume.service.Impl.EsServiceImpl;
import com.resume.service.Impl.ResumeService;

import ch.qos.logback.classic.Logger;

/**
 * es操作模块
 * 1.人才列表显示，从es中读出而不是数据库
 * 2.简单的统计分析数据交给前端画图
 * 3.搜索引擎（全文索引、姓名查询、条件查询）
 * 4.直接删除操作
 * 5.人名检索和全文检索时检索内容高亮显示️
 * 6.标签管理模块
 * 待做：
 * 2.查询建议 --api找不到
 * 3.多选操作️
 * 5.批量操作： 删除、增加、查看️
 * @author mac
 *
 */
@Controller
@RequestMapping("/es/")
public class EsController {
	private static Logger logger = (Logger) LoggerFactory.getLogger(ResumeService.class);
	@Autowired
	private TransportClient client;
	@Autowired
	private EsServiceImpl iEsService;
	
	//统计男女中不同年龄段的学历分布
	@RequestMapping("agg_by_edu_in_render_and_age.do")
	@ResponseBody
	public ServerResponse aggByEduInRenderAndAge(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.aggGroupByEduInGenderAndAge(user.getId());
	}
	
	
	/**
	 * 删除索引
	 * @param resumeId
	 * @return
	 */
	@RequestMapping("delete.do")
	@ResponseBody
	public ServerResponse delete(HttpSession session,Integer resumeId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.delete(resumeId.toString());
	}
	
	
	/**
	 * 统计男性中的学历
	 * @return
	 */
	@RequestMapping("agg_by_edu_in_man.do")
	@ResponseBody
	public ServerResponse aggByEduInMan(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.aggGroupByEduInMan(user.getId());
	}
	/**
	 * 统计女性中的学历
	 * @return
	 */
	@RequestMapping("agg_by_edu_in_woman.do")
	@ResponseBody
	public ServerResponse aggByEduInWoman(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.aggGroupByEduInWomen(user.getId());
	}
	/**
	 * 按性别统计
	 * @return
	 */
	@RequestMapping("agg_by_render.do")
	@ResponseBody
	public ServerResponse aggByRender(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.aggGroupByField(user.getId(), "render");
	}
	
	/**
	 * 按学历统计
	 * @return
	 */
	@RequestMapping("agg_by_edu.do")
	@ResponseBody
	public ServerResponse aggByEdu(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.aggGroupByField(user.getId(), "edu");
	}
	
	
	
	
	
	/**
	 * 精确查询
	 * @param company
	 * @param job
	 * @param edu
	 * @param note
	 * @param render
	 * @param gt_age
	 * @param lt_age
	 * @return
	 */
	@RequestMapping("multiple_query.do")
	@ResponseBody
	public ServerResponse multipleQuery(
			@RequestParam(value="company",required = false)String company,
			@RequestParam(value="job",required = false)String job,
			@RequestParam(value="edu",required = false)String edu,
			HttpSession session,
			@RequestParam(value="gender",required = false)String gender,
			@RequestParam(value="gt_age",required = false)Integer gt_age,
			@RequestParam(value="lt_age",required = false)Integer lt_age) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }	
		return iEsService.multipleQuery(user.getId(),company,job,edu,gender,gt_age,lt_age);
	}
	
	/**
	 * 根据人名查询
	 * @param name
	 * @return
	 */
	@RequestMapping("select_by_name.do")
	@ResponseBody
	public ServerResponse selectByName(HttpSession session,String name) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }	
		return iEsService.getByName(user.getId(),name);
	}
	
	/**
	 * 人名建议
	 */
	@RequestMapping("suggest_name.do")
	@ResponseBody
	public ServerResponse suggestName(HttpSession session,String namePrefix) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.suggestName(namePrefix);
	}
	/**
	 * 全文检索
	 * @param string
	 * @return
	 */
	@RequestMapping("query_string.do")
	@ResponseBody
	public ServerResponse selectString(HttpSession session,String string) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.queryString(user.getId(),string);
	}
	

	/**
	 * 查询所有 match_all
	 * @return
	 */
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse list(HttpSession session) {
		
		//todo 
		//后端获取传进来的json，进行顺序、筛选
		//前端：条件查询的页面
		//如何解决react跨域访问的问题
		//全文搜索的结果如何进行筛选、排序
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.getList(user.getId());
	}
	
	//标签管理模块
	/**
	 * 获取note列表
	 * @return
	 */
	@RequestMapping("get_note_list.do")
	@ResponseBody
	public ServerResponse getNoteList(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.getNoteList(user.getId());
	}
	
	/**
	 * get_by_note
	 * @return
	 */
	@RequestMapping("get_by_note.do")
	@ResponseBody
	public ServerResponse getByNote(HttpSession session,@RequestParam(value="note", defaultValue="", required=false) String note) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.getByNote(user.getId(),note);
	}
	
	/**
	 * 增加标签
	 * @param resumeId
	 * @param note
	 * @return
	 */
	@RequestMapping("add_note.do")
	@ResponseBody
	public ServerResponse addNote(HttpSession session,Integer resumeId, String note) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return iEsService.addNote(user.getId(),resumeId,note);
	}
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


}
