package com.resume.vo;

import java.util.Date;
import java.util.List;

import com.resume.pojo.ResumeSchoolExp;
import com.resume.pojo.ResumeWorkExp;

public class ResumeVo {
	
	//包含个人基本信息、学习经历列表、工作经历列表
	private Integer resumeId;//简历id
	
    private Integer userId;

    private String name;

    private String nationality;

    private String note;

    private Integer sex;

    private String email;

    private String phone;

    private String image;

    private String born;

    private String nation;

    private String city;

    private String expectJob;

    private Date createTime;

    private Date updateTime;
    
    private List<ResumeSchoolExp> resumeSchoolExpList;
    
    private List<ResumeWorkExp> resumeWorkExpList;

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

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBorn() {
		return born;
	}

	public void setBorn(String born) {
		this.born = born;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getExpectJob() {
		return expectJob;
	}

	public void setExpectJob(String expectJob) {
		this.expectJob = expectJob;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public List<ResumeSchoolExp> getResumeSchoolExpList() {
		return resumeSchoolExpList;
	}

	public void setResumeSchoolExpList(List<ResumeSchoolExp> resumeSchoolExpList) {
		this.resumeSchoolExpList = resumeSchoolExpList;
	}

	public List<ResumeWorkExp> getResumeWorkExpList() {
		return resumeWorkExpList;
	}

	public void setResumeWorkExpList(List<ResumeWorkExp> resumeWorkExpList) {
		this.resumeWorkExpList = resumeWorkExpList;
	}
    
    
}
