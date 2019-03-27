package com.resume.pojo;

import java.util.Date;

public class ResumeWorkExp {
    private Integer id;

    private Integer resumeId;

    private Integer status;

    private String job;

    private String company;

    private String department;

    private String time;

    private Date createTime;

    private Date updateTime;

    public ResumeWorkExp(Integer id, Integer resumeId, Integer status, String job, String company, String department, String time, Date createTime, Date updateTime) {
        this.id = id;
        this.resumeId = resumeId;
        this.status = status;
        this.job = job;
        this.company = company;
        this.department = department;
        this.time = time;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    
    public ResumeWorkExp() {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
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