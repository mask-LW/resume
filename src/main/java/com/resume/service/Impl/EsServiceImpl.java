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
	//判断字符串 是否为数字
	public static boolean isInteger(String str) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
  }
	
	//获取es中note属性的元素集合列表
	public ServerResponse getNoteList(Integer userId){
		logger.info("获取note");
		//用户过滤
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		//聚合属性
		AggregationBuilder agg=  AggregationBuilders.terms("group").field("note");
		
		//建立连接
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
	
	
	
	
	//统计性别中不同年龄段的学历分布
	public ServerResponse<Map<String, Object>> aggGroupByEduInGenderAndAge(Integer userId){
		//男性 20-30 30-40 40-50 50-60
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
	
	
	//返回女性年龄段中学历的分布
		ServerResponse<Map<String, Object>> aggGroupByEduInWomanAndAge(Integer userId,int gt_age,int lte_age){
			RangeQueryBuilder qb = QueryBuilders.rangeQuery("age").gt(gt_age).lte(lte_age);
			BoolQueryBuilder bqb = QueryBuilders.boolQuery();
			bqb.must(QueryBuilders.matchPhraseQuery("userId", userId));
			bqb.must(QueryBuilders.matchPhraseQuery("gender", "女"));
			
			
			bqb.filter(qb);
			//聚合属性
			AggregationBuilder agg=  AggregationBuilders.terms("group").field("edu");
			
			//建立连接
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
	
	//返回男性年龄段中学历的分布
	ServerResponse<Map<String, Object>> aggGroupByEduInManAndAge(Integer userId,int gt_age,int lte_age){
		RangeQueryBuilder qb = QueryBuilders.rangeQuery("age").gt(gt_age).lte(lte_age);
		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		bqb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		bqb.must(QueryBuilders.matchPhraseQuery("gender", "男"));
		
		
		bqb.filter(qb);
		//聚合属性
		AggregationBuilder agg=  AggregationBuilders.terms("group").field("edu");
		
		//建立连接
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
	
	//在性别基础上聚合查询学历:男性
	public ServerResponse<Map<String, Object>> aggGroupByEduInMan(Integer userId){
		//用户过滤
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		qb.must(QueryBuilders.matchPhraseQuery("gender","男"));
		
		
		//聚合属性
		AggregationBuilder agg=  AggregationBuilders.terms("group_by_man").field("edu");
		
		//建立连接
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
	
	//在性别基础上聚合查询学历:女性
	public ServerResponse<Map<String, Object>> aggGroupByEduInWomen(Integer userId){
		//用户过滤
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		qb.must(QueryBuilders.matchPhraseQuery("gender", "女"));
		
		
		//聚合属性
		AggregationBuilder agg=  AggregationBuilders.terms("group_by_woman").field("edu");
		
		//建立连接
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
	//单一的聚合查询
	public ServerResponse<Map<String, Object>> aggGroupByField(Integer userId,String field){
		//用户过滤
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		//聚合属性
		AggregationBuilder agg=  AggregationBuilders.terms("group").field(field);
		
		//建立连接
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
        System.out.println("共搜到:"+searchHits.getTotalHits()+"条结果!");
		
        String msg = searchHits.getTotalHits() + " result be found in " +time;
		
		for(SearchHit hit : response.getHits()) {
			  Map<String, Object> source = hit.getSourceAsMap();
	          result.add(source);
		}
		return ServerResponse.createBySuccess(msg, result);
	}
	
	/**
	 * 全文检索
	 */
	public ServerResponse<List<Map<String, Object>>> queryString(Integer userId,String string) {
		//不用指定字段，全文搜索 
		//用户过滤
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.matchPhraseQuery("userId", userId));
		
		HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);;
		
		
		
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");     
       
        //查询条件	
		
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(string);//查询并获取结果
		
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(queryBuilder).setSize(10000).setPostFilter(qb)
				.highlighter(highlightBuilder);
		
		SearchResponse response = builder.get();
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		SearchHits searchHits = response.getHits();
		String time = response.getTook().toString();
        System.out.println("共搜到:"+searchHits.getTotalHits()+"条结果!");
		
        String msg = searchHits.getTotalHits() + " result be found in " +time;
		
		for(SearchHit hit : response.getHits()) {
			  Map<String, Object> source = hit.getSourceAsMap();
					  
	            //处理高亮片段
	            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	            
	            System.out.print(highlightFields);
	            
	            HighlightField nameField = highlightFields.get("name");
	            if(nameField!=null){
	                Text[] fragments = nameField.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //将高亮片段组装到结果中去
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
	                //将高亮片段组装到结果中去
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
	                //将高亮片段组装到结果中去
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
	                //将高亮片段组装到结果中去
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
	                //将高亮片段组装到结果中去
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
	                //将高亮片段组装到结果中去
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
	                //将高亮片段组装到结果中去
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
	                //将高亮片段组装到结果中去
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
	                //将高亮片段组装到结果中去
	                System.out.println("nationality:"+nameTmp);
	                source.put("nationality",nameTmp);
	            }
	            result.add(source);
		}
		return ServerResponse.createBySuccess(msg, result);
	}
	
	/**
	 * 人名检索
	 * match_by_name
	 * @param name
	 * @return
	 */
	public ServerResponse<List<Map<String, Object>>> getByName(Integer userId,String name) {
		//用户过滤
		QueryBuilder queryBuilder = QueryBuilders.termQuery("userId", userId);
		//查询条件
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("name", name));
		
		
		HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        highlightBuilder.field("name");
		//查询并获取结果
		SearchRequestBuilder builder = this.client.prepareSearch("resume")
				.setTypes("message").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(boolQuery).setPostFilter(queryBuilder).highlighter(highlightBuilder);
		SearchResponse response = builder.get();
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		SearchHits searchHits = response.getHits();
		String time = response.getTook().toString();
        System.out.println("共搜到:"+searchHits.getTotalHits()+"条结果!");
		
        String msg = searchHits.getTotalHits() + " result be found in " +time;
		
		for(SearchHit hit : response.getHits()) {
			  Map<String, Object> source = hit.getSourceAsMap();
					  
	            //处理高亮片段
	            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	            HighlightField nameField = highlightFields.get("name");
	            if(nameField!=null){
	                Text[] fragments = nameField.fragments();
	                String nameTmp ="";
	                for(Text text:fragments){
	                    nameTmp+=text;
	                }
	                //将高亮片段组装到结果中去
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
		//用户过滤
		QueryBuilder queryBuilder = QueryBuilders.termQuery("userId", userId);
		//查询条件
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("note", note));
		
		//查询并获取结果
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
	 * 获取所有记录
	 * match_all
	 * @return
	 */
	public ServerResponse<List<Map<String, Object>>> getList(Integer userId) {
		//用户过滤
		QueryBuilder queryBuilder = QueryBuilders.termQuery("userId", userId);
		//查询条件
		QueryBuilder qb = QueryBuilders.matchAllQuery();
		//查询并获取结果
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
	 * 创建文档
	 * 在解析文档入库时
	 * 文档内容：basic_info 所有信息、past学习经历中最新学历、current工作经历中最新岗位
	 */
	public ServerResponse insert(ResumeBasicInfo resumeBasicInfo,
			List<ResumeSchoolExp>  resumeSchoolExpList,
			List<ResumeWorkExp>  resumeWorkExpList) {
		logger.info("开始插入es");
		EsResumeVo esResumeVo;
		try {
			logger.info("组装esResumeVo类");
			esResumeVo = this.assembleEsResumeVo(resumeBasicInfo, resumeSchoolExpList, resumeWorkExpList);
			
			ServerResponse re = this.add(esResumeVo);
			logger.info("增加索引");
			if(re.getStatus() == ResponseCode.SUCCESS.getCode()) {
				return  ServerResponse.createBySuccess("增加成功");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ServerResponse.createByErrorMessage("增加失败");
	
		
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
			
			logger.info("--准备插入es---");
			
			IndexResponse result = this.client.prepareIndex("resume", "message",esResumeVo.getResumeId().toString()).setSource(content).get();
			return  ServerResponse.createBySuccess("增加成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("增加失败");
		}
	}
	
	/**
	 * 删除文档
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
			return ServerResponse.createByErrorMessage("修改失败");
		}
		try {
			UpdateResponse  result = this.client.update(update).get();
			return ServerResponse.createBySuccess("修改成功", result.getResult().toString());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("修改失败");
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
			esResumeVo.setGender("男");
		}else {
			esResumeVo.setGender("女");
		}
		esResumeVo.setNation(resumeBasicInfo.getNation());
		esResumeVo.setNationality(resumeBasicInfo.getNationality());
		esResumeVo.setUploadTime(resumeBasicInfo.getCreateTime());
		//出生日期必须为yyyy年mm月dd日才可计算年龄
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
		//赋值学习经历
		//选出已完成的学习经历
		List<ResumeSchoolExp>  schoolExpPastList = Lists.newArrayList();
		
		for(int i=0; i<resumeSchoolExpList.size();i++) {
			if(resumeSchoolExpList.get(i).getStatus() == 0) {
				schoolExpPastList.add(resumeSchoolExpList.get(i));
			}
		}
		logger.info("选出past的学习列表");
		//筛选出最新的经历作为es索引数据
		if(schoolExpPastList.size()>0) {
			Date temp= DateTimeUtil.strToDateYear("1000");
			int current = 0;
			String date = null;
			if(!schoolExpPastList.get(0).getTime().equals("")) {
				 date = schoolExpPastList.get(0).getTime().substring(0,4);
				 temp = DateTimeUtil.strToDateYear(date);
			}
			//如果只有一个记录，直接赋值，否则循环比较
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
			//最新学习经历赋值
			esResumeVo.setSchool(schoolExpPastList.get(current).getSchool());
			esResumeVo.setEdu(schoolExpPastList.get(current).getEdu());
			esResumeVo.setPro(schoolExpPastList.get(current).getPro());
			esResumeVo.setSchoolTime(Integer.valueOf(date));
		}
		/**************************/
		//赋值工作经历
		List<ResumeWorkExp>  workExpCurrentList =  Lists.newArrayList();
		if(resumeWorkExpList.size() > 0) {
			//若有现任经历，选出现任的工作经历
			for(int i=0; i<resumeWorkExpList.size();i++) {
				if(resumeWorkExpList.get(i).getStatus() == 1) {
					workExpCurrentList.add(resumeWorkExpList.get(i));
				}
			}
		}
	
			//筛选出最新的经历作为es索引数据
		if(workExpCurrentList.size()>0) {
			logger.info("存在现任职位");
			
			int current1 = workExpCurrentList.size()-1;
			
			//现任工作经历赋值
			
			esResumeVo.setCurrentCompany(workExpCurrentList.get(current1).getCompany());
			esResumeVo.setJob(workExpCurrentList.get(current1).getJob());
			esResumeVo.setJobStatus("在职");
			esResumeVo.setWorkTime(workExpCurrentList.get(current1).getTime());
			
		}else {
			//若无现任工作经历
			//选出已完成的工作经历
			List<ResumeWorkExp>  workExpPastList =  Lists.newArrayList();
			logger.info("不存在现任职位");
			for(int i=0; i<resumeWorkExpList.size();i++) {
				if(resumeWorkExpList.get(i).getStatus() == 0) {
					workExpPastList.add(resumeWorkExpList.get(i));
				}
			}
			
			//筛选出最新的经历作为es索引数据
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
			//曾任工作经历赋值
			
			esResumeVo.setCurrentCompany(workExpPastList.get(current2).getCompany());
			esResumeVo.setJob(workExpPastList.get(current2).getJob());
			esResumeVo.setJobStatus("离职");
			esResumeVo.setWorkTime(workExpPastList.get(current2).getTime());
			}
			
		}
		return esResumeVo;
		
	}

	//增加每个记录的标签
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
			return ServerResponse.createByErrorMessage("修改失败");
		}
		try {
			UpdateResponse  result = this.client.update(update).get();
			return ServerResponse.createBySuccess("修改成功", result.getResult().toString());
		} catch (InterruptedException | ExecutionException e) {
			
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("修改失败");
		}
	}

	@Override
	public ServerResponse suggestName(String namePrefix) {
		// TODO Auto-generated method stub
		return null;
	}

	
	


	
}
