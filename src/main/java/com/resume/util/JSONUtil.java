package com.resume.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.resume.common.ServerResponse;
import com.resume.vo.PythonResumeVo;

/**
 * json���ݹ�����
 * @author mac
 *
 */
public class JSONUtil {
	private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	
	
	public static ServerResponse<PythonResumeVo> send(String txt) throws IOException {
		
		
		String url = "http://119.23.44.138:11000/single_extract";
	//	txt = "��������,����,�й�����,�޾������þ���Ȩ,1962��12��8�ճ���,�о���ѧ��,1986��7�±��Ʊ�ҵ���Ͼ��ƾ���ѧ������ϵ,2000���ҵ������ŦԼ������ѧ�����޷�УUB���й������ѧ��ѧԺEMBA�߼����̹���˶ʿ��2003���ҵ�����������ѧ���廪��ѧ�߼���Ӫ�ܲð�,2015����ɽ��ѧ�ʱ������߼����ް��ڶ���1986��9����1989��1��,���α��������������޹�˾�ɱ����Ա���������;1989��1����1992��1��,�����й��������޹�˾����˾�⡢�߼��ƻ����;1992��1����1996��2��,������DEC�����й����޹�˾(�������ͼ������˾,��Compaq�չ�,Compaq�ֱ������չ�)�й���������������;1996��2����2002��3��,��΢���й����޹�˾�й������������ܼ�(CFO);2002��3����2006��5��,�����й���ͨ�ع����޹�˾(ԭС��ͨ)��Ʒ�����ܲá���Ӫ���ܲ�;2006��6����2008��5��,���й������ҵ����(��ͨ����ϵ���ӻ���)��ϯ�����(CFO);2008��6����2009��12��,�λ�����ý�弯����������(�й������ҵ����Ͷ����ҵ)�����ܲ�;2010��2����2013��12��,���α�����ʤ��������Ƽ����޹�˾����ϯ��Ӫ��(COO)����ϯ�����(CFO)����ϯִ�й�(CEO);2014��2����2015��12��,�α���������ѯ�������޹�˾�������;2016��1������,�������г�������Ϣ�Ƽ��ɷ����޹�˾����,���������г�������Ϣ�Ƽ��ɷ����޹�˾���¡������ܼࡢ���»����顣";
		txt = "{\"resume\":\""+txt+"\"}";
		//txt="{\"resume\":\"��������,����,�й�����,�޾������þ���Ȩ,1962��12��8�ճ���,�о���ѧ��,1986��7�±��Ʊ�ҵ���Ͼ��ƾ���ѧ������ϵ,2000���ҵ������ŦԼ������ѧ�����޷�УUB���й������ѧ��ѧԺEMBA�߼����̹���˶ʿ��2003���ҵ�����������ѧ���廪��ѧ�߼���Ӫ�ܲð�,2015����ɽ��ѧ�ʱ������߼����ް��ڶ���1986��9����1989��1��,���α��������������޹�˾�ɱ����Ա���������;1989��1����1992��1��,�����й��������޹�˾����˾�⡢�߼��ƻ����;1992��1����1996��2��,������DEC�����й����޹�˾(�������ͼ������˾,��Compaq�չ�,Compaq�ֱ������չ�)�й���������������;1996��2����2002��3��,��΢���й����޹�˾�й������������ܼ�(CFO);2002��3����2006��5��,�����й���ͨ�ع����޹�˾(ԭС��ͨ)��Ʒ�����ܲá���Ӫ���ܲ�;2006��6����2008��5��,���й������ҵ����(��ͨ����ϵ���ӻ���)��ϯ�����(CFO);2008��6����2009��12��,�λ�����ý�弯����������(�й������ҵ����Ͷ����ҵ)�����ܲ�;2010��2����2013��12��,���α�����ʤ��������Ƽ����޹�˾����ϯ��Ӫ��(COO)����ϯ�����(CFO)����ϯִ�й�(CEO);2014��2����2015��12��,�α���������ѯ�������޹�˾�������;2016��1������,�������г�������Ϣ�Ƽ��ɷ����޹�˾����,���������г�������Ϣ�Ƽ��ɷ����޹�˾���¡������ܼࡢ���»����顣\"}";
		//String resume = "{\"data\": {\"regresume\": {\"school_exp\": {\"past\": [{\"pro\": \"\", \"time\": \"\", \"school\": \"\", \"edu\": \"�о���\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"1986��7��\", \"school\": \"�Ͼ��ƾ���ѧ������ϵ\", \"edu\": \"���Ʊ�ҵ\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2000��\", \"school\": \"����ŦԼ������ѧ�����޷�У\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2000��\", \"school\": \"�й������ѧ��ѧԺ\", \"edu\": \"\", \"degree\": \"EMBA\"}, {\"pro\": \"\", \"time\": \"2000��\", \"school\": \"�й������ѧ��ѧԺ\", \"edu\": \"\", \"degree\": \"�߼����̹���˶ʿ\"}, {\"pro\": \"\", \"time\": \"2003��\", \"school\": \"���������ѧ\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2003��\", \"school\": \"�廪��ѧ�߼���Ӫ�ܲð�\", \"edu\": \"\", \"degree\": \"\"}, {\"pro\": \"\", \"time\": \"2015��\", \"school\": \"��ɽ��ѧ�ʱ������߼����ް�\", \"edu\": \"\", \"degree\": \"\"}], \"current\": []}, \"basic_info\": {\"sex\": \"��\", \"nationality\": \"�й�\", \"nation\": \"����\", \"name\": \"����\", \"born\": \"1962��12��8��\"}, \"work_exp\": {\"past\": [{\"job\": \"\", \"time\": \"2000��\", \"place\": \"����ŦԼ������ѧ�����޷�У\", \"department\": \"\"}, {\"job\": \"\", \"time\": \"2003��\", \"place\": \"���������ѧ\", \"department\": \"\"}, {\"job\": \"�ɱ����Ա\", \"time\": \"1986��9����1989��1��\", \"place\": \"���������������޹�˾\", \"department\": \"\"}, {\"job\": \"�������\", \"time\": \"1986��9����1989��1��\", \"place\": \"���������������޹�˾\", \"department\": \"\"}, {\"job\": \"����˾��\", \"time\": \"1989��1����1992��1��\", \"place\": \"�й��������޹�˾\", \"department\": \"\"}, {\"job\": \"�߼��ƻ����\", \"time\": \"1989��1����1992��1��\", \"place\": \"�й��������޹�˾\", \"department\": \"\"}, {\"job\": \"������������\", \"time\": \"1992��1����1996��2��\", \"place\": \"����DEC�����й����޹�˾�й���\", \"department\": \"\"}, {\"job\": \"���������ܼ�\", \"time\": \"1996��2����2002��3��\", \"place\": \"΢���й����޹�˾�й���\", \"department\": \"\"}, {\"job\": \"��Ʒ�����ܲ�\", \"time\": \"2002��3����2006��5��\", \"place\": \"�й���ͨ�ع����޹�˾\", \"department\": \"\"}, {\"job\": \"��Ӫ���ܲ�\", \"time\": \"2002��3����2006��5��\", \"place\": \"�й���ͨ�ع����޹�˾\", \"department\": \"\"}, {\"job\": \"��ϯ�����\", \"time\": \"2006��6����2008��5��\", \"place\": \"�й������ҵ����\", \"department\": \"\"}, {\"job\": \"�����ܲ�\", \"time\": \"2008��6����2009��12��\", \"place\": \"������ý�弯����������\", \"department\": \"\"}, {\"job\": \"��ϯ��Ӫ��\", \"time\": \"2010��2����2013��12��\", \"place\": \"������ʤ��������Ƽ����޹�˾\", \"department\": \"\"}, {\"job\": \"��ϯ�����\", \"time\": \"2010��2����2013��12��\", \"place\": \"������ʤ��������Ƽ����޹�˾\", \"department\": \"\"}, {\"job\": \"��ϯִ�й�\", \"time\": \"2010��2����2013��12��\", \"place\": \"������ʤ��������Ƽ����޹�˾\", \"department\": \"\"}, {\"job\": \"�������\", \"time\": \"2014��2����2015��12��\", \"place\": \"����������ѯ�������޹�˾\", \"department\": \"\"}], \"current\": [{\"job\": \"\", \"time\": \"2016��1������\", \"place\": \"�����г�������Ϣ�Ƽ��ɷ����޹�˾\", \"department\": \"\"}, {\"job\": \"����\", \"time\": \"��\", \"place\": \"�����г�������Ϣ�Ƽ��ɷ����޹�˾\", \"department\": \"\"}, {\"job\": \"�����ܼ�\", \"time\": \"��\", \"place\": \"�����г�������Ϣ�Ƽ��ɷ����޹�˾\", \"department\": \"\"}, {\"job\": \"���»�����\", \"time\": \"��\", \"place\": \"�����г�������Ϣ�Ƽ��ɷ����޹�˾\", \"department\": \"\"}]}}}, \"msg\": \"�����ɹ�\", \"code\": 1}";
		//String resume= "{\"data\": {\"regresume\": {\"school_exp\": {\"past\":[{\"pro\": \"\", \"time\": \"\", \"school\": \"\", \"edu\": \"��ר\", \"degree\": \"\" } ], \"current\": [] }, \"basic_info\": { \"sex\": \"Ů\", \"nationality\": \"�й�\", \"nation\": \"\", \"name\": \"�췼��\", \"born\": \"1982��9��1��\" }, \"work_exp\": { \"past\": [ ], \"current\": [ { \"job\": \"��Ӧ������������������ְԱ\", \"time\": \"2002������\", \"place\": \"��������ʱ���\", \"department\": \"\" }, { \"job\": \"���»���ϯ��ְ��������º�Ӫ������ҵ�񲿲��������ݵ����»���ϯ\", \"time\": \"��\", \"place\": \"\", \"department\": \"\" } ] } } }, \"msg\": \"�����ɹ�\", \"code\": 1}";

		String resume = HttpClienUtil.postRequest(url, txt);
		logger.info("��ȡǰ"+resume);
		resume = resume.substring(22,resume.length()-28);
		logger.info("������"+resume);
		//ȡ��regresume��
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
		logger.info("ת����ɹ�");
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
