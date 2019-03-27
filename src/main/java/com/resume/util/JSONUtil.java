package com.resume.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.resume.common.ServerResponse;
import com.resume.vo.PythonResumeVo;

/**
 * json数据工具类
 * @author mac
 *
 */
public class JSONUtil {
	private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	
	
	public static ServerResponse<PythonResumeVo> send(String txt) throws IOException {
		
		
		String url = "http://119.23.44.138:11000/single_extract";
	//	txt = "郭涛先生,汉族,中国国籍,无境外永久居留权,1962年12月8日出生,研究生学历,1986年7月本科毕业于南京财经大学财务会计系,2000年毕业于美国纽约州立大学布法罗分校UB与中国人民大学商学院EMBA高级工商管理硕士。2003年毕业于美国哈佛大学与清华大学高级运营总裁班,2015年中山大学资本运作高级研修班在读。1986年9月至1989年1月,历任北京正大饲料有限公司成本会计员、会计主管;1989年1月至1992年1月,历任中国惠普有限公司财务司库、高级财会分析;1992年1月至1996年2月,任美国DEC电脑中国有限公司(美国大型计算机公司,后被Compaq收购,Compaq又被惠普收购)中国区财务行政经理;1996年2月至2002年3月,任微软中国有限公司中国区财务行政总监(CFO);2002年3月至2006年5月,历任中国网通控股有限公司(原小网通)产品财务副总裁、运营副总裁;2006年6月至2008年5月,任中国宽带产业基金(网通集团系种子基金)首席财务官(CFO);2008年6月至2009年12月,任华亿新媒体集团旅游卫视(中国宽带产业基金投资企业)财务副总裁;2010年2月至2013年12月,历任北京东胜创新生物科技有限公司任首席运营官(COO)、首席财务官(CFO)、首席执行官(CEO);2014年2月至2015年12月,任北京普利咨询管理有限公司资深顾问;2016年1月至今,在深圳市长城网信息科技股份有限公司工作,现任深圳市长城网信息科技股份有限公司董事、财务总监、董事会秘书。";
		txt = "{\"resume\":\""+txt+"\"}";
		//txt="{\"resume\":\"郭涛先生,汉族,中国国籍,无境外永久居留权,1962年12月8日出生,研究生学历,1986年7月本科毕业于南京财经大学财务会计系,2000年毕业于美国纽约州立大学布法罗分校UB与中国人民大学商学院EMBA高级工商管理硕士。2003年毕业于美国哈佛大学与清华大学高级运营总裁班,2015年中山大学资本运作高级研修班在读。1986年9月至1989年1月,历任北京正大饲料有限公司成本会计员、会计主管;1989年1月至1992年1月,历任中国惠普有限公司财务司库、高级财会分析;1992年1月至1996年2月,任美国DEC电脑中国有限公司(美国大型计算机公司,后被Compaq收购,Compaq又被惠普收购)中国区财务行政经理;1996年2月至2002年3月,任微软中国有限公司中国区财务行政总监(CFO);2002年3月至2006年5月,历任中国网通控股有限公司(原小网通)产品财务副总裁、运营副总裁;2006年6月至2008年5月,任中国宽带产业基金(网通集团系种子基金)首席财务官(CFO);2008年6月至2009年12月,任华亿新媒体集团旅游卫视(中国宽带产业基金投资企业)财务副总裁;2010年2月至2013年12月,历任北京东胜创新生物科技有限公司任首席运营官(COO)、首席财务官(CFO)、首席执行官(CEO);2014年2月至2015年12月,任北京普利咨询管理有限公司资深顾问;2016年1月至今,在深圳市长城网信息科技股份有限公司工作,现任深圳市长城网信息科技股份有限公司董事、财务总监、董事会秘书。\"}";
		//String resume = "{\"data\": {\"regresume\": {\"school_exp\": {\"past\": [{\"pro\": \"\", \"time\": \"\", \"school\": \"\", \"edu\": \"研究生\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"1986年7月\", \"school\": \"南京财经大学财务会计系\", \"edu\": \"本科毕业\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2000年\", \"school\": \"美国纽约州立大学布法罗分校\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2000年\", \"school\": \"中国人民大学商学院\", \"edu\": \"\", \"degree\": \"EMBA\"}, {\"pro\": \"\", \"time\": \"2000年\", \"school\": \"中国人民大学商学院\", \"edu\": \"\", \"degree\": \"高级工商管理硕士\"}, {\"pro\": \"\", \"time\": \"2003年\", \"school\": \"美国哈佛大学\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2003年\", \"school\": \"清华大学高级运营总裁班\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2015年\", \"school\": \"中山大学资本运作高级研修班\", \"edu\": \"\", \"degree\": \"\"}], \"current\": []}, \"basic_info\": {\"sex\": \"男\", \"nationality\": \"中国\", \"nation\": \"汉族\", \"name\": \"郭涛\", \"born\": \"1962年12月8日\"}, \"work_exp\": {\"past\": [{\"job\": \"\", \"time\": \"2000年\", \"place\": \"美国纽约州立大学布法罗分校\", \"department\": \"\"}, {\"job\": \"\", \"time\": \"2003年\", \"place\": \"美国哈佛大学\", \"department\": \"\"}, {\"job\": \"成本会计员\", \"time\": \"1986年9月至1989年1月\", \"place\": \"北京正大饲料有限公司\", \"department\": \"\"}, {\"job\": \"会计主管\", \"time\": \"1986年9月至1989年1月\", \"place\": \"北京正大饲料有限公司\", \"department\": \"\"}, {\"job\": \"财务司库\", \"time\": \"1989年1月至1992年1月\", \"place\": \"中国惠普有限公司\", \"department\": \"\"}, {\"job\": \"高级财会分析\", \"time\": \"1989年1月至1992年1月\", \"place\": \"中国惠普有限公司\", \"department\": \"\"}, {\"job\": \"财务行政经理\", \"time\": \"1992年1月至1996年2月\", \"place\": \"美国DEC电脑中国有限公司中国区\", \"department\": \"\"}, {\"job\": \"财务行政总监\", \"time\": \"1996年2月至2002年3月\", \"place\": \"微软中国有限公司中国区\", \"department\": \"\"}, {\"job\": \"产品财务副总裁\", \"time\": \"2002年3月至2006年5月\", \"place\": \"中国网通控股有限公司\", \"department\": \"\"}, {\"job\": \"运营副总裁\", \"time\": \"2002年3月至2006年5月\", \"place\": \"中国网通控股有限公司\", \"department\": \"\"}, {\"job\": \"首席财务官\", \"time\": \"2006年6月至2008年5月\", \"place\": \"中国宽带产业基金\", \"department\": \"\"}, {\"job\": \"财务副总裁\", \"time\": \"2008年6月至2009年12月\", \"place\": \"华亿新媒体集团旅游卫视\", \"department\": \"\"}, {\"job\": \"首席运营官\", \"time\": \"2010年2月至2013年12月\", \"place\": \"北京东胜创新生物科技有限公司\", \"department\": \"\"}, {\"job\": \"首席财务官\", \"time\": \"2010年2月至2013年12月\", \"place\": \"北京东胜创新生物科技有限公司\", \"department\": \"\"}, {\"job\": \"首席执行官\", \"time\": \"2010年2月至2013年12月\", \"place\": \"北京东胜创新生物科技有限公司\", \"department\": \"\"}, {\"job\": \"资深顾问\", \"time\": \"2014年2月至2015年12月\", \"place\": \"北京普利咨询管理有限公司\", \"department\": \"\"}], \"current\": [{\"job\": \"\", \"time\": \"2016年1月至今\", \"place\": \"深圳市长城网信息科技股份有限公司\", \"department\": \"\"}, {\"job\": \"董事\", \"time\": \"现\", \"place\": \"深圳市长城网信息科技股份有限公司\", \"department\": \"\"}, {\"job\": \"财务总监\", \"time\": \"现\", \"place\": \"深圳市长城网信息科技股份有限公司\", \"department\": \"\"}, {\"job\": \"董事会秘书\", \"time\": \"现\", \"place\": \"深圳市长城网信息科技股份有限公司\", \"department\": \"\"}]}}}, \"msg\": \"操作成功\", \"code\": 1}";
		//String resume= "{\"data\": {\"regresume\": {\"school_exp\": {\"past\":[{\"pro\": \"\", \"time\": \"\", \"school\": \"\", \"edu\": \"大专\", \"degree\": \"\" } ], \"current\": [] }, \"basic_info\": { \"sex\": \"女\", \"nationality\": \"中国\", \"nation\": \"\", \"name\": \"徐芳艳\", \"born\": \"1982年9月1日\" }, \"work_exp\": { \"past\": [ ], \"current\": [ { \"job\": \"供应部、技术部、生产部职员\", \"time\": \"2002年至今\", \"place\": \"电光有限质保部\", \"department\": \"\" }, { \"job\": \"监事会主席、职工代表监事和营销中心业务部部长、宿州电光监事会主席\", \"time\": \"现\", \"place\": \"\", \"department\": \"\" } ] } } }, \"msg\": \"操作成功\", \"code\": 1}";

		String resume = HttpClienUtil.postRequest(url, txt);
		logger.info("截取前"+resume);
		resume = resume.substring(22,resume.length()-28);
		logger.info("解析后"+resume);
		//取得regresume类
		PythonResumeVo pythonResumeVo = new Gson().fromJson(resume, PythonResumeVo.class);
		logger.info("--basic");
//			logger.info(pythonResumeVo.basic_info.getName());
//			logger.info(pythonResumeVo.basic_info.getBorn());
//			logger.info(pythonResumeVo.basic_info.getNation());
//			logger.info(pythonResumeVo.basic_info.getSex());
//			logger.info(pythonResumeVo.basic_info.getNationality());
//			
//			logger.info("--school-past");
//			
//			for(int i=0; i< pythonResumeVo.school_exp.getPast().size();i++) {
//				logger.info(pythonResumeVo.school_exp.getPast().get(i).getDegree());
//				logger.info(pythonResumeVo.school_exp.getPast().get(i).getEdu());
//				logger.info(pythonResumeVo.school_exp.getPast().get(i).getPro());
//				logger.info(pythonResumeVo.school_exp.getPast().get(i).getSchool());
//				logger.info(pythonResumeVo.school_exp.getPast().get(i).getTime());
//				logger.info("--***--");
//			}
//			
//			logger.info("--school-current");
//			
//			for(int i=0; i< pythonResumeVo.school_exp.getCurrent().size();i++) {
//				logger.info(pythonResumeVo.school_exp.getCurrent().get(i).getDegree());
//				logger.info(pythonResumeVo.school_exp.getCurrent().get(i).getEdu());
//				logger.info(pythonResumeVo.school_exp.getCurrent().get(i).getPro());
//				logger.info(pythonResumeVo.school_exp.getCurrent().get(i).getSchool());
//				logger.info(pythonResumeVo.school_exp.getCurrent().get(i).getTime());
//				logger.info("--***--");
//			}
//			
//			logger.info("--work-past");
//			
//			for(int i=0; i< pythonResumeVo.work_exp.getPast().size();i++) {
//				logger.info(pythonResumeVo.work_exp.getPast().get(i).getJob());
//				logger.info(pythonResumeVo.work_exp.getPast().get(i).getDepartment());
//				logger.info(pythonResumeVo.work_exp.getPast().get(i).getPlace());
//				logger.info(pythonResumeVo.work_exp.getPast().get(i).getTime());
//				logger.info("--***--");
//			}
//			
//			logger.info("--work-current");
//			
//			for(int i=0; i< pythonResumeVo.work_exp.getCurrent().size();i++) {
//				logger.info(pythonResumeVo.work_exp.getCurrent().get(i).getJob());
//				logger.info(pythonResumeVo.work_exp.getCurrent().get(i).getDepartment());
//				logger.info(pythonResumeVo.work_exp.getCurrent().get(i).getPlace());
//				logger.info(pythonResumeVo.work_exp.getCurrent().get(i).getTime());
//				logger.info("--***--");
//			}
////			
//			
////			logger.info("---school-current");
////			logger.info(pythonResumeVo.getSchool_exp().past.);
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			
////			logger.info("----work-past");
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			
////			logger.info("--work-current");
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			logger.info(pythonResumeVo.basic_info.getName());
////			
		logger.info("转化类成功");
		return ServerResponse.createBySuccess(pythonResumeVo);
		
//		logger.info("---school-current");
//		logger.info(pythonResumeVo.getSchool_exp().past.);
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		
//		logger.info("----work-past");
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		
//		logger.info("--work-current");
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		
//		logger.info("--basic");
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
//		logger.info(pythonResumeVo.basic_info.getName());
		
		
	}

	
}
