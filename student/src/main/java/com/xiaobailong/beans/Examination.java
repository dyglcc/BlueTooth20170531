package com.xiaobailong.beans;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "EXAMINATION".
 */
public class Examination implements java.io.Serializable {

    private Long id;
    private boolean expired;
    private String break_;
    private String false_;
    private String short_;
    private Integer minutes;
    private String devices;

    public Examination() {
    }

    public Examination(Long id) {
        this.id = id;
    }

    public Examination(Long id, boolean expired, String break_, String false_, String short_, Integer minutes, String devices) {
        this.id = id;
        this.expired = expired;
        this.break_ = break_;
        this.false_ = false_;
        this.short_ = short_;
        this.minutes = minutes;
        this.devices = devices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getBreak_() {
        return break_;
    }

    public void setBreak_(String break_) {
        this.break_ = break_;
    }

    public String getFalse_() {
        return false_;
    }

    public void setFalse_(String false_) {
        this.false_ = false_;
    }

    public String getShort_() {
        return short_;
    }

    public void setShort_(String short_) {
        this.short_ = short_;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

}
