package com.resume.pojo;

import java.util.Date;

public class ResumeSchoolExp {
    private Integer id;

    private Integer resumeId;

    private String pro;

    private String edu;

    private String time;

    private String school;

    private Integer status;//0:past 1:current

    private Date createTime;

    private Date updateTime;

    public ResumeSchoolExp(Integer id, Integer resumeId, String pro, String edu, String time, String school, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.resumeId = resumeId;
        this.pro = pro;
        this.edu = edu;
        this.time = time;
        this.school = school;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public ResumeSchoolExp() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResumeId() {
        return resumeId;
    }

    public void setResumeId(Integer resumeId) {
        this.resumeId = resumeId;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro == null ? null : pro.trim();
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu == null ? null : edu.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}