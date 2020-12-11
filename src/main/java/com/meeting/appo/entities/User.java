package com.meeting.appo.entities;

import java.util.Date;

public class User {
    private Integer uid;
    private String username;
    private String mobile;
    private String dept;
    private Date create_date;
    private boolean Admin;

    public User() {
    }

    public User(Integer uid, String username, String mobile, String dept, Date create_date, boolean admin) {
        this.uid = uid;
        this.username = username;
        this.mobile = mobile;
        this.dept = dept;
        this.create_date = create_date;
        Admin = admin;
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

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
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
                ", dept='" + dept + '\'' +
                ", create_date=" + create_date +
                ", Admin=" + Admin +
                '}';
    }
}
