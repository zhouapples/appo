package com.meeting.appo.entities;

import java.util.Date;

public class Status {
    private Integer sid;
    private Date create_date;
    private Date start_date;
    private Date end_date;
    private Integer rid;
    private String participants;
    private String meeting_theme;
    private Integer uid;
    private Boolean status;
    private User user;
    private Room room;

    public Status() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status(Integer sid, Date create_date, Date start_date, Date end_date, Integer rid, String participants, String meeting_theme, Integer uid, Boolean status) {
        this.sid = sid;
        this.create_date = create_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.rid = rid;
        this.participants = participants;
        this.meeting_theme = meeting_theme;
        this.uid = uid;
        this.status = status;
        this.user = new User();
        this.room = new Room();
    }

    public Status(Date create_date, Date start_date, Date end_date, Integer rid, String participants, String meeting_theme, Integer uid, Boolean status) {
        this.create_date = create_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.rid = rid;
        this.participants = participants;
        this.meeting_theme = meeting_theme;
        this.uid = uid;
        this.status = status;
        this.user = new User();
        this.room = new Room();
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getMeeting_theme() {
        return meeting_theme;
    }

    public void setMeeting_theme(String meeting_theme) {
        this.meeting_theme = meeting_theme;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Status{" +
                "sid=" + sid +
                ", create_date=" + create_date +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", rid=" + rid +
                ", participants='" + participants + '\'' +
                ", meeting_theme='" + meeting_theme + '\'' +
                ", uid=" + uid +
                ", status=" + status +
                '}';
    }
}
