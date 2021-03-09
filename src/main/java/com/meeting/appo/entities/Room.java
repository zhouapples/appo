package com.meeting.appo.entities;

import java.util.Date;

public class Room {
    private Integer rid;
    private Integer rflood;
    private String rserial;
    private Integer seats;
    private boolean Available;
    private String comm;
    private Date create_date;

    public Room() {
    }

    public Room(Integer rid, Integer rflood, String rserial, Integer seats, boolean available, String comm, Date create_date) {
        this.rid = rid;
        this.rflood = rflood;
        this.rserial = rserial;
        this.seats = seats;
        Available = available;
        this.comm = comm;
        this.create_date = create_date;
    }

    public Room( Integer rflood, String rserial, Integer seats, boolean available, String comm, Date create_date) {
        this.rflood = rflood;
        this.rserial = rserial;
        this.seats = seats;
        Available = available;
        this.comm = comm;
        this.create_date = create_date;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getRflood() {
        return rflood;
    }

    public void setRflood(Integer rflood) {
        this.rflood = rflood;
    }

    public String getRserial() {
        return rserial;
    }

    public void setRserial(String rserial) {
        this.rserial = rserial;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public boolean isAvailable() {
        return Available;
    }

    public void setAvailable(boolean available) {
        Available = available;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    @Override
    public String toString() {
        return "Room{" +
                "rid=" + rid +
                ", rflood=" + rflood +
                ", rserial='" + rserial + '\'' +
                ", seats=" + seats +
                ", Available=" + Available +
                ", comm='" + comm + '\'' +
                ", create_date=" + create_date +
                '}';
    }
}
