package com.resume.vo;

import java.util.Date;

/**
 * es������ʵ����
 * ��������Ϣѡȡһ������Ϊ������Ԫ��
 * @author mac
 *
 */

public class EsResumeVo {
	
	private Integer resumeId;//����id
	
	private Integer userId;//�û�ӵ����id����ǰ�û�ֻ�������������ļ���
	
	private String name; //����������
	
	private Integer age;
	
	private String gender;
	
	private String nation;
	
	private String nationality;
	
	private String  edu;//���ѧ��
	
	private String school; //��ӦѧУ
	
	private String pro;//��Ӧרҵ
	
	private Integer schoolTime; 
	
	private String currentCompany;//������ڹ�˾
	
	private String job;//������ڹ�˾��ְλ
	
	private String workTime;
	
	private String expectJob;//����ְλ
	
	private String city; //���ڳ���
	 
	private Date uploadTime;//�ϴ�ʱ��
	
	private String note;//��ǩ
	
	private String  JobStatus; //��ְ״̬ 0:��ְ 1:��ְ
	
	
	
	

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSchoolTime() {
		return schoolTime;
	}

	public void setSchoolTime(Integer schoolTime) {
		this.schoolTime = schoolTime;
	}

	
	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

	public Integer getResumeId() {
		return resumeId;
	}

	public void setResumeId(Integer resumeId) {
		this.resumeId = resumeId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCurrentCompany() {
		return currentCompany;
	}

	public void setCurrentCompany(String currentCompany) {
		this.currentCompany = currentCompany;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getExpectJob() {
		return expectJob;
	}

	public void setExpectJob(String expectJob) {
		this.expectJob = expectJob;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getJobStatus() {
		return JobStatus;
	}

	public void setJobStatus(String jobStatus) {
		JobStatus = jobStatus;
	}
	
}
