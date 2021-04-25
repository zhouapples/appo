package com.meeting.appo.entities;

import java.util.Date;

public class User {
    private Integer uid;
    private String username;
    private String mobile;
    private int deptId;
    private Date create_date;
    private boolean Admin;
    private String password;
    private String pinyin_name;
    private Dept dept;
    private boolean available;

    public User() {
    }

    public User(Integer uid, String username, String password,String mobile, int deptId, Date create_date, boolean admin) {
        this.uid = uid;
        this.username = username;
        this.mobile = mobile;
        this.deptId = deptId;
        this.create_date = create_date;
        this.Admin = admin;
        this.password = password;
    }

    public User(String username, String password,String mobile, int deptId, Date create_date, boolean admin,String pinyin_name) {
        this.uid = null;
        this.password = password;
        this.username = username;
        this.mobile = mobile;
        this.deptId = deptId;
        this.create_date = create_date;
        this.Admin = admin;
        this.pinyin_name = pinyin_name;
    }

    //更新用户信息用
    public User(int uid ,String username, String password,String mobile, int deptId, boolean admin,String pinyin_name) {
        this.uid = uid;
        this.username = username;
        this.mobile = mobile;
        this.deptId = deptId;
        this.Admin = admin;
        this.password = password;
        this.pinyin_name = pinyin_name;
    }


    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getPinyin_name() {
        return pinyin_name;
    }

    public void setPinyin_name(String pinyin_name) {
        this.pinyin_name = pinyin_name;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public boolean isAdmin() {
        return Admin;
    }

    public void setAdmin(boolean admin) {
        Admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", deptId='" + deptId + '\'' +
                ", create_date=" + create_date +
                ", Admin=" + Admin +
                ", password='" + password + '\'' +
                ", pinyin_name='" + pinyin_name + '\'' +
                '}';
    }
}
