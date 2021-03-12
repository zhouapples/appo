package com.meeting.appo.entities;

public class Dept {
    private int did;
    private String dept_name;
    private int before_level_id;
    private int after_level_id;
    private String comment;
    private boolean is_first_level;

    public Dept() {
    }

    public Dept(int did, String dept_name, int before_level_id, int after_level_id, String comment, boolean is_first_level) {
        this.did = did;
        this.dept_name = dept_name;
        this.before_level_id = before_level_id;
        this.after_level_id = after_level_id;
        this.comment = comment;
        this.is_first_level = is_first_level;
    }

    public Dept(String dept_name, int before_level_id, int after_level_id, String comment, boolean is_first_level) {
        this.dept_name = dept_name;
        this.before_level_id = before_level_id;
        this.after_level_id = after_level_id;
        this.comment = comment;
        this.is_first_level = is_first_level;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public int getBefore_level_id() {
        return before_level_id;
    }

    public void setBefore_level_id(int before_level_id) {
        this.before_level_id = before_level_id;
    }

    public int getAfter_level_id() {
        return after_level_id;
    }

    public void setAfter_level_id(int after_level_id) {
        this.after_level_id = after_level_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIs_first_level() {
        return is_first_level;
    }

    public void setIs_first_level(boolean is_first_level) {
        this.is_first_level = is_first_level;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "did=" + did +
                ", dept_name='" + dept_name + '\'' +
                ", before_level_id=" + before_level_id +
                ", after_level_id=" + after_level_id +
                ", comment='" + comment + '\'' +
                ", is_first_level=" + is_first_level +
                '}';
    }
}
