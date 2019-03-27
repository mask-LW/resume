package com.resume.pojo;

import java.util.Date;

public class ResumeBasicInfo {
    private Integer id;

    private Integer userId;

    private String name;

    private String nationality;

    private String note;

    private Integer sex;//0:ÄÐ 1:Å®

    private String email;

    private String phone;

    private String image;

    private String born;

    private String nation;

    private String city;

    private String expectJob;

    private Date createTime;

    private Date updateTime;

    public ResumeBasicInfo(Integer id, Integer userId, String name, String nationality, String note, Integer sex, String email, String phone, String image, String born, String nation, String city, String expectJob, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.nationality = nationality;
        this.note = note;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.born = born;
        this.nation = nation;
        this.city = city;
        this.expectJob = expectJob;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public ResumeBasicInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.name = name == null ? null : name.trim();
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality == null ? null : nationality.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born == null ? null : born.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getExpectJob() {
        return expectJob;
    }

    public void setExpectJob(String expectJob) {
        this.expectJob = expectJob == null ? null : expectJob.trim();
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