package com.resume.service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.resume.common.ResponseCode;
import com.resume.common.ServerResponse;
import com.resume.pojo.ResumeBasicInfo;
import com.resume.pojo.ResumeSchoolExp;
import com.resume.pojo.ResumeWorkExp;
import com.resume.service.IEsService;
import com.resume.util.DateTimeUtil;
import com.resume.vo.EsResumeVo;

import ch.qos.logback.classic.Logger;

@Service("iEsService")
public class EsServiceImpl implements IEsService{
	private static Logger logger = (Logger) LoggerFactory.getLogger(ResumeService.class);
	@Autowired
	private TransportClient client;
	//�ж��ַ��� �Ƿ�Ϊ����
	public static boolean isInteger(String str) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
  }
	
	//��ȡes��note���Ե�Ԫ�ؼ����б�
	public ServerResponse getNoteList(Integer userId){
		logger.info("��ȡnote");
		//�û�����
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		//�ۺ�����
		AggregationBuilder agg=  AggregationBuilders.terms("group").field("note");
		
		//��������
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		List<String> result = Lists.newArrayList();
		 
		 
		 SearchResponse sr = builder.setQuery(qb)
				 .addAggregation(agg)
				 .setExplain(true)
				 .execute()
				 .actionGet();
		 Terms genders = sr.getAggregations().get("group");		
		 
		 
		 
		 for(Bucket entry: genders.getBuckets()) {
			 	logger.info(entry.getKey().toString());
			 	result.add(entry.getKey().toString());
			 }
		 
		 return ServerResponse.createBySuccess(result);
	}
	
	
	
	
	//ͳ���Ա��в�ͬ����ε�ѧ���ֲ�
	public ServerResponse<Map<String, Object>> aggGroupByEduInGenderAndAge(Integer userId){
		//���� 20-30 30-40 40-50 50-60
		Map<String,Object> man = new HashMap<String,Object>();
		Map<String,Object> woman = new HashMap<String,Object>();
		Map<String,Object> result = new HashMap<String,Object>();
		man.put("20~30",this.aggGroupByEduInManAndAge(userId, 20, 30).getData());
		man.put("30-40",this.aggGroupByEduInManAndAge(userId, 30, 40).getData());
		man.put("40-50",this.aggGroupByEduInManAndAge(userId, 40, 50).getData());
		man.put("50-60",this.aggGroupByEduInManAndAge(userId, 50, 60).getData());
		
		woman.put("20~30",this.aggGroupByEduInWomanAndAge(userId, 20, 30).getData());
		woman.put("30-40",this.aggGroupByEduInWomanAndAge(userId, 30, 40).getData());
		woman.put("40-50",this.aggGroupByEduInWomanAndAge(userId, 40, 50).getData());
		woman.put("50-60",this.aggGroupByEduInWomanAndAge(userId, 50, 60).getData());
		
		result.put("man", man);
		result.put("woman", woman);
		return ServerResponse.createBySuccess(result);
	}
	
	
	//����Ů���������ѧ���ķֲ�
		ServerResponse<Map<String, Object>> aggGroupByEduInWomanAndAge(Integer userId,int gt_age,int lte_age){
			RangeQueryBuilder qb = QueryBuilders.rangeQuery("age").gt(gt_age).lte(lte_age);
			BoolQueryBuilder bqb = QueryBuilders.boolQuery();
			bqb.must(QueryBuilders.matchPhraseQuery("userId", userId));
			bqb.must(QueryBuilders.matchPhraseQuery("gender", "Ů"));
			
			
			bqb.filter(qb);
			//�ۺ�����
			AggregationBuilder agg=  AggregationBuilders.terms("group").field("edu");
			
			//��������
			SearchRequestBuilder builder = this.client.prepareSearch("resume")
					.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
			
			 Map<String,Object> result = new HashMap<String,Object>();
			 
			 SearchResponse sr = builder.setQuery(bqb).addAggregation(agg).setExplain(true)
					 .execute()
					 .actionGet();
			 Terms genders = sr.getAggregations().get("group");		
			 
			 
			for(Bucket entry: genders.getBuckets()) {
					 result.put(entry.getKey().toString(), entry.getDocCount());  
				}
			 
			 return ServerResponse.createBySuccess(result);
		}
	
	//���������������ѧ���ķֲ�
	ServerResponse<Map<String, Object>> aggGroupByEduInManAndAge(Integer userId,int gt_age,int lte_age){
		RangeQueryBuilder qb = QueryBuilders.rangeQuery("age").gt(gt_age).lte(lte_age);
		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		bqb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		bqb.must(QueryBuilders.matchPhraseQuery("gender", "��"));
		
		
		bqb.filter(qb);
		//�ۺ�����
		AggregationBuilder agg=  AggregationBuilders.terms("group").field("edu");
		
		//��������
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		 Map<String,Object> result = new HashMap<String,Object>();
		 
		 SearchResponse sr = builder.setQuery(bqb).addAggregation(agg).setExplain(true)
				 .execute()
				 .actionGet();
		 Terms genders = sr.getAggregations().get("group");		
		 
		 
			 for(Bucket entry: genders.getBuckets()) {
				 result.put(entry.getKey().toString(), entry.getDocCount());  
				 }
		 
		 return ServerResponse.createBySuccess(result);
	}
	
	//���Ա�����Ͼۺϲ�ѯѧ��:����
	public ServerResponse<Map<String, Object>> aggGroupByEduInMan(Integer userId){
		//�û�����
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		qb.must(QueryBuilders.matchPhraseQuery("gender","��"));
		
		
		//�ۺ�����
		AggregationBuilder agg=  AggregationBuilders.terms("group_by_man").field("edu");
		
		//��������
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		 Map<String,Object> result = new HashMap<String,Object>();
		 
		 SearchResponse sr = builder.setQuery(qb)
				 .addAggregation(agg)
				 .setExplain(true)
				 .execute()
				 .actionGet();
		 Terms genders = sr.getAggregations().get("group_by_man");		
		 
		 
			 for(Bucket entry: genders.getBuckets()) {
				 result.put(entry.getKey().toString(), entry.getDocCount());  
				 }
		 
		 return ServerResponse.createBySuccess(result);
	}
	
	//���Ա�����Ͼۺϲ�ѯѧ��:Ů��
	public ServerResponse<Map<String, Object>> aggGroupByEduInWomen(Integer userId){
		//�û�����
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		qb.must(QueryBuilders.matchPhraseQuery("gender", "Ů"));
		
		
		//�ۺ�����
		AggregationBuilder agg=  AggregationBuilders.terms("group_by_woman").field("edu");
		
		//��������
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		 Map<String,Object> result = new HashMap<String,Object>();
		 
		 SearchResponse sr = builder.setQuery(qb)
				 .addAggregation(agg)
				 .setExplain(true)
				 .execute()
				 .actionGet();
		 Terms genders = sr.getAggregations().get("group_by_woman");		
		 
		 
			 for(Bucket entry: genders.getBuckets()) {
				 result.put(entry.getKey().toString(), entry.getDocCount());  
				 }
		 
		 return ServerResponse.createBySuccess(result);
		 
	}
	//��һ�ľۺϲ�ѯ
	public ServerResponse<Map<String, Object>> aggGroupByField(Integer userId,String field){
		//�û�����
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		//�ۺ�����
		AggregationBuilder agg=  AggregationBuilders.terms("group").field(field);
		
		//��������
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		 Map<String,Object> result = new HashMap<String,Object>();
		 
		 
		 SearchResponse sr = builder.setQuery(qb)
				 .addAggregation(agg)
				 .setExplain(true)
				 .execute()
				 .actionGet();
		 Terms genders = sr.getAggregations().get("group");		
		 
		 
		 
			 for(Bucket entry: genders.getBuckets()) {
				 	result.put(entry.getKey().toString(), entry.getDocCount());  
				 	logger.info(entry.getKey().toString()+"-"+entry.getDocCount());
				 }
		 
		 return ServerResponse.createBySuccess(result);
	}
	
	
	
	public ServerResponse<List<Map<String, Object>>> multipleQuery(Integer userId,String company,
			String job,String edu,String gender,
			Integer gt_age , Integer lt_age){
		
		
		QueryBuilder queryBuilder = QueryBuilders.termQuery("userId", userId);
		
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		
		
		if(company != null ) {
			qb.must(QueryBuilders.matchPhraseQuery("currentCompany", company));
		}
		
		if(job != null ) {
			qb.must(QueryBuilders.matchPhraseQuery("job", job));
		}
		if(edu != null ) {
			qb.must(QueryBuilders.matchPhraseQuery("edu", edu));
		}
		if(gender != null ) {
			qb.must(QueryBuilders.matchPhraseQuery("currentCompany", gender));
		}
//		RangeQueryBuilder rangQuery = QueryBuilders.rangeQuery("age").from(gt_age);
//		
//		if(lt_age  != null && lt_age >16) {
//			rangQuery.to(lt_age);
//		}
//		
//		qb.filter(rangQuery);
		
		
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb).setPostFilter(queryBuilder).setSize(10000);
				
		SearchResponse response = builder.get();
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		SearchHits searchHits = response.getHits();
		String time = response.getTook().toString();
        System.out.println("���ѵ�:"+searchHits.getTotalHits()+"�����!");
		
        String msg = searchHits.getTotalHits() + " result be found in " +time;
		
		for(SearchHit hit : response.getHits()) {
			  Map<String, Object> source = hit.getSourceAsMap();
	          result.add(source);
		}
		return ServerResponse.createBySuccess(msg, result);
	}
	
	/**
	 * ȫ�ļ���
	 */
	public ServerResponse<List<Map<String, Object>>> queryString(Integer userId,String string) {
		//����ָ���ֶΣ�ȫ������ 
		//�û�����
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		
		HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);;
		
		
		
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");     
       
        //��ѯ����	
		
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(string);//��ѯ����ȡ���
		
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(queryBuilder).setSize(10000).setPostFilter(qb)
				.highlighter(highlightBuilder);
		
		SearchResponse response = builder.get();
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		SearchHits searchHits = response.getHits();
		String time = response.getTook().toString();
        System.out.println("���ѵ�:"+searchHits.getTotalHits()+"�����!");
		
        String msg = searchHits.getTotalHits() + " result be found in " +time;
		
		for(SearchHit hit : response.getHits()) {
			  Map<String, Object> source = hit.getSourceAsMap();
					  
	            //�������Ƭ��
	            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	            
	            System.out.print(highlightFields);
	            
	            HighlightField nameField = highlightFields.get("name");
	            if(nameField!=null){
	                Text[] fragments = nameField.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                source.put("name",nameTmp);
	                System.out.println("name:"+nameTmp);
	            }
	            HighlightField currentCompany = highlightFields.get("currentCompany");
	            if(currentCompany!=null){
	                Text[] fragments = currentCompany.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                System.out.println("currentCompany:"+nameTmp);
	                source.put("currentCompany",nameTmp);
	            }
	            HighlightField job = highlightFields.get("job");
	            if(job!=null){
	                Text[] fragments = job.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                System.out.println("job:"+nameTmp);
	                source.put("job",nameTmp);
	            }
	            HighlightField  age= highlightFields.get("age");
	            if(age!=null){
	                Text[] fragments = age.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                System.out.println("age:"+nameTmp);
	                source.put("age",nameTmp);
	            }
	            HighlightField  gender= highlightFields.get("gender");
	            if(gender!=null){
	                Text[] fragments = gender.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                System.out.println("gender:"+nameTmp);
	                source.put("gender",nameTmp);
	            }
	            HighlightField  pro= highlightFields.get("pro");
	            if(pro!=null){
	                Text[] fragments = pro.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                System.out.println("pro:"+nameTmp);
	                source.put("pro",nameTmp);
	            }
	            HighlightField  edu= highlightFields.get("edu");
	            if(edu!=null){
	                Text[] fragments = edu.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                System.out.println("edu:"+nameTmp);
	                source.put("edu",nameTmp);
	            }
	            HighlightField  school= highlightFields.get("school");
	            if(school!=null){
	                Text[] fragments = school.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                System.out.println("school:"+nameTmp);
	                source.put("school",nameTmp);
	            }
	            HighlightField  nationality= highlightFields.get("nationality");
	            if(nationality!=null){
	                Text[] fragments = nationality.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                System.out.println("nationality:"+nameTmp);
	                source.put("nationality",nameTmp);
	            }
	            result.add(source);
		}
		return ServerResponse.createBySuccess(msg, result);
	}
	
	/**
	 * ��������
	 * match_by_name
	 * @param name
	 * @return
	 */
	public ServerResponse<List<Map<String, Object>>> getByName(Integer userId,String name) {
		//�û�����
		QueryBuilder queryBuilder = QueryBuilders.termQuery("userId", userId);
		//��ѯ����
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("name", name));
		
		
		HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field("name");
		//��ѯ����ȡ���
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(boolQuery).setPostFilter(queryBuilder).highlighter(highlightBuilder);
		SearchResponse response = builder.get();
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		SearchHits searchHits = response.getHits();
		String time = response.getTook().toString();
        System.out.println("���ѵ�:"+searchHits.getTotalHits()+"�����!");
		
        String msg = searchHits.getTotalHits() + " result be found in " +time;
		
		for(SearchHit hit : response.getHits()) {
			  Map<String, Object> source = hit.getSourceAsMap();
					  
	            //�������Ƭ��
	            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	            HighlightField nameField = highlightFields.get("name");
	            if(nameField!=null){
	                Text[] fragments = nameField.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //������Ƭ����װ�������ȥ
	                source.put("name",nameTmp);
	            }
	            result.add(source);
		}
		return ServerResponse.createBySuccess(msg, result);
	}
	
	
	/**
	 * match_by_note
	 * @param name
	 * @return
	 */
	public ServerResponse<List<Map<String, Object>>> getByNote(Integer userId,String note) {
		//�û�����
		QueryBuilder queryBuilder = QueryBuilders.termQuery("userId", userId);
		//��ѯ����
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("note", note));
		
		//��ѯ����ȡ���
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(boolQuery).setPostFilter(queryBuilder);
		SearchResponse response = builder.get();
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		for(SearchHit hit : response.getHits()) {
			 result.add(hit.getSourceAsMap());
		}
		String time = response.getTook().toString();
	    Integer count = result.size();
		String total = count+ " result be found in " +time;
		return ServerResponse.createBySuccess(total, result);
	}
	
	/**
	 * ��ȡ���м�¼
	 * match_all
	 * @return
	 */
	public ServerResponse<List<Map<String, Object>>> getList(Integer userId) {
		//�û�����
		QueryBuilder queryBuilder = QueryBuilders.termQuery("userId", userId);
		//��ѯ����
		QueryBuilder qb = QueryBuilders.matchAllQuery();
		//��ѯ����ȡ���
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
						.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
						.setQuery(qb).setPostFilter(queryBuilder).setSize(10000);
	    SearchResponse response = builder.get();
        

        
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
       
		for(SearchHit hit : response.getHits()) {
			 result.add(hit.getSourceAsMap());
		}
		String time = response.getTook().toString();
	    Integer count = result.size();
		String total = count+ " result be found in " +time;
		return ServerResponse.createBySuccess(total, result);
	}
	
	
	/**
	 * �����ĵ�
	 * �ڽ����ĵ����ʱ
	 * �ĵ����ݣ�basic_info ������Ϣ��pastѧϰ����������ѧ����current�������������¸�λ
	 */
	public ServerResponse insert(ResumeBasicInfo resumeBasicInfo,
			List<ResumeSchoolExp>  resumeSchoolExpList,
			List<ResumeWorkExp>  resumeWorkExpList) {
		logger.info("��ʼ����es");
		EsResumeVo esResumeVo;
		try {
			logger.info("��װesResumeVo��");
			esResumeVo = this.assembleEsResumeVo(resumeBasicInfo, resumeSchoolExpList, resumeWorkExpList);
			
			ServerResponse re = this.add(esResumeVo);
			logger.info("��������");
			if(re.getStatus() == ResponseCode.SUCCESS.getCode()) {
				return  ServerResponse.createBySuccess("���ӳɹ�");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ServerResponse.createByErrorMessage("����ʧ��");
	
		
	}
	
	ServerResponse add(EsResumeVo esResumeVo) {
		try {
			XContentBuilder content = XContentFactory.jsonBuilder().
					startObject().
					field("resumeId",esResumeVo.getResumeId()).
					field("userId", esResumeVo.getUserId()).
					field("name",esResumeVo.getName()).
					field("gender",esResumeVo.getGender()).
					field("nation",esResumeVo.getNation()).
					field("nationality",esResumeVo.getNationality()).
					field("note", esResumeVo.getNote()).
					field("age",esResumeVo.getAge()).
					field("edu",esResumeVo.getEdu()).
					field("pro", esResumeVo.getPro()).
					field("school",esResumeVo.getSchool()).
					field("schoolYear",esResumeVo.getSchoolTime()).
					field("job",esResumeVo.getJob()).
					field("currentCompany",esResumeVo.getCurrentCompany()).
					field("workYear",esResumeVo.getWorkTime()).
					field("expectJob",esResumeVo.getExpectJob()).
					field("city",esResumeVo.getCity()).
					field("uploadTime",esResumeVo.getUploadTime().getTime()).
					field("JobStatus",esResumeVo.getJobStatus()).endObject();
			
			logger.info("--׼������es---");
			
			IndexResponse result = this.client.prepareIndex("resume", "message",esResumeVo.getResumeId().toString()).setSource(content).get();
			return  ServerResponse.createBySuccess("���ӳɹ�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("����ʧ��");
		}
	}
	
	/**
	 * ɾ���ĵ�
	 * @param id
	 * @return
	 */
	public ServerResponse delete(String resumeId) {
		DeleteResponse result = this.client.prepareDelete("resume", "message", resumeId.toString()).get();
		return ServerResponse.createBySuccess(result.getResult());
	}

	public ServerResponse update(String title, String author,String id) {
		UpdateRequest update = new UpdateRequest("book","novel",id);
		
		
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().
					startObject();
			if( title != null) {
				builder.field("title", title);
			}
			if( author != null) {
				builder.field("author", author);
			}
			builder.endObject();
			update.doc(builder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("�޸�ʧ��");
		}
		try {
			UpdateResponse  result = this.client.update(update).get();
			return ServerResponse.createBySuccess("�޸ĳɹ�", result.getResult().toString());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("�޸�ʧ��");
		}
	}
	
	
	public EsResumeVo assembleEsResumeVo(ResumeBasicInfo resumeBasicInfo,
			List<ResumeSchoolExp>  resumeSchoolExpList,
			List<ResumeWorkExp>  resumeWorkExpList) throws Exception {
		
		EsResumeVo esResumeVo = new EsResumeVo();
		
		esResumeVo.setResumeId(resumeBasicInfo.getId());
		esResumeVo.setUserId(resumeBasicInfo.getUserId());
		esResumeVo.setName(resumeBasicInfo.getName());
		esResumeVo.setNote(resumeBasicInfo.getNote());
		if( resumeBasicInfo.getSex() == 0) {
			esResumeVo.setGender("��");
		}else {
			esResumeVo.setGender("Ů");
		}
		esResumeVo.setNation(resumeBasicInfo.getNation());
		esResumeVo.setNationality(resumeBasicInfo.getNationality());
		esResumeVo.setUploadTime(resumeBasicInfo.getCreateTime());
		//�������ڱ���Ϊyyyy��mm��dd�ղſɼ�������
		if(!resumeBasicInfo.getBorn().equals("") && resumeBasicInfo.getBorn().length() >=9) {
			
			String date = resumeBasicInfo.getBorn();
			int a = DateTimeUtil.getYear(date);
			String  a1 = date.substring(0,a);
			System.out.println(a1);
			int b = DateTimeUtil.getMonth(date);
			String b1 = date.substring(a+1,b);
			System.out.println(b1);
			int c = DateTimeUtil.getDay(date);
			String c1 = date.substring(b+1,c);
			System.out.println(c1);
			date = a1+"-"+b1+"-"+c1;
			
			esResumeVo.setAge(DateTimeUtil.getAge(DateTimeUtil.strToDate_1(date)));
			}
		esResumeVo.setCity(resumeBasicInfo.getCity());
		esResumeVo.setExpectJob(resumeBasicInfo.getExpectJob());
		//��ֵѧϰ����
		//ѡ������ɵ�ѧϰ����
		List<ResumeSchoolExp>  schoolExpPastList = Lists.newArrayList();
		
		for(int i=0; i<resumeSchoolExpList.size();i++) {
			if(resumeSchoolExpList.get(i).getStatus() == 0) {
				schoolExpPastList.add(resumeSchoolExpList.get(i));
			}
		}
		logger.info("ѡ��past��ѧϰ�б�");
		//ɸѡ�����µľ�����Ϊes��������
		if(schoolExpPastList.size()>0) {
			Date temp= DateTimeUtil.strToDateYear("1000");
			int current = 0;
			String date = null;
			if(!schoolExpPastList.get(0).getTime().equals("")) {
				 date = schoolExpPastList.get(0).getTime().substring(0,4);
				 temp = DateTimeUtil.strToDateYear(date);
			}
			//���ֻ��һ����¼��ֱ�Ӹ�ֵ������ѭ���Ƚ�
			for(int j=1;j<schoolExpPastList.size();j++) {
				
				if(!schoolExpPastList.get(j).getTime().equals("")) {
					 date = schoolExpPastList.get(j).getTime().substring(0,4);
					logger.info(date);
					int compare = temp.compareTo(DateTimeUtil.strToDateYear(date));
					if(compare <0) {
						current = j;
						temp = DateTimeUtil.strToDateYear(date);
					}
				}
				
			}
			//����ѧϰ������ֵ
			esResumeVo.setSchool(schoolExpPastList.get(current).getSchool());
			esResumeVo.setEdu(schoolExpPastList.get(current).getEdu());
			esResumeVo.setPro(schoolExpPastList.get(current).getPro());
			esResumeVo.setSchoolTime(Integer.valueOf(date));
		}
		/**************************/
		//��ֵ��������
		List<ResumeWorkExp>  workExpCurrentList =  Lists.newArrayList();
		if(resumeWorkExpList.size() > 0) {
			//�������ξ�����ѡ�����εĹ�������
			for(int i=0; i<resumeWorkExpList.size();i++) {
				if(resumeWorkExpList.get(i).getStatus() == 1) {
					workExpCurrentList.add(resumeWorkExpList.get(i));
				}
			}
		}
	
			//ɸѡ�����µľ�����Ϊes��������
		if(workExpCurrentList.size()>0) {
			logger.info("��������ְλ");
			
			int current1 = workExpCurrentList.size()-1;
			
			//���ι���������ֵ
			
			esResumeVo.setCurrentCompany(workExpCurrentList.get(current1).getCompany());
			esResumeVo.setJob(workExpCurrentList.get(current1).getJob());
			esResumeVo.setJobStatus("��ְ");
			esResumeVo.setWorkTime(workExpCurrentList.get(current1).getTime());
			
		}else {
			//�������ι�������
			//ѡ������ɵĹ�������
			List<ResumeWorkExp>  workExpPastList =  Lists.newArrayList();
			logger.info("����������ְλ");
			for(int i=0; i<resumeWorkExpList.size();i++) {
				if(resumeWorkExpList.get(i).getStatus() == 0) {
					workExpPastList.add(resumeWorkExpList.get(i));
				}
			}
			
			//ɸѡ�����µľ�����Ϊes��������
			if(workExpPastList.size()>0) {
			Date temp2= DateTimeUtil.strToDateYear("1000");
			int current2 = 0;
			String date2 = null;
			for(int j=1;j<workExpPastList.size();j++) {
				
				if(!workExpPastList.get(j).getTime().equals("")) {
					date2 = workExpPastList.get(j).getTime().substring(0,4);
					logger.info(date2);
					if(isInteger(date2)) {
						int compare = temp2.compareTo(DateTimeUtil.strToDateYear(date2));
						if(compare <0) {
							current2 = j;
							temp2 = DateTimeUtil.strToDateYear(date2);
						}
					}
				}
			}
			//���ι���������ֵ
			
			esResumeVo.setCurrentCompany(workExpPastList.get(current2).getCompany());
			esResumeVo.setJob(workExpPastList.get(current2).getJob());
			esResumeVo.setJobStatus("��ְ");
			esResumeVo.setWorkTime(workExpPastList.get(current2).getTime());
			}
			
		}
		return esResumeVo;
		
	}

	//����ÿ����¼�ı�ǩ
	public ServerResponse addNote(Integer userId, Integer resumeId, String note) {
		
		UpdateRequest update = new UpdateRequest("resume","message",resumeId.toString());
		
		
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().
					startObject();
			
			if( note != null) {
				builder.field("note", note);
			}
			builder.endObject();
			update.doc(builder);
		} catch (IOException e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("�޸�ʧ��");
		}
		try {
			UpdateResponse  result = this.client.update(update).get();
			return ServerResponse.createBySuccess("�޸ĳɹ�", result.getResult().toString());
		} catch (InterruptedException | ExecutionException e) {
			
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("�޸�ʧ��");
		}
	}

	@Override
	public ServerResponse suggestName(String namePrefix) {
		// TODO Auto-generated method stub
		return null;
	}

	
	


	
}
