package com.xiaobailong.bean;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "STUDENT".
 */
@Entity
public class Student implements java.io.Serializable {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String xuehao;

    @NotNull
    private String username;
    private long semester;
    private long classes;
    private Integer results;

    @NotNull
    private String mobile;

    @NotNull
    private String ids;
    private String sex;
    private String devices;

    @Generated
    public Student() {
    }

    public Student(Long id) {
        this.id = id;
    }

    @Generated
    public Student(Long id, String xuehao, String username, long semester, long classes, Integer results, String mobile, String ids, String sex, String devices) {
        this.id = id;
        this.xuehao = xuehao;
        this.username = username;
        this.semester = semester;
        this.classes = classes;
        this.results = results;
        this.mobile = mobile;
        this.ids = ids;
        this.sex = sex;
        this.devices = devices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getXuehao() {
        return xuehao;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setXuehao(@NotNull String xuehao) {
        this.xuehao = xuehao;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public long getSemester() {
        return semester;
    }

    public void setSemester(long semester) {
        this.semester = semester;
    }

    public long getClasses() {
        return classes;
    }

    public void setClasses(long classes) {
        this.classes = classes;
    }

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    @NotNull
    public String getMobile() {
        return mobile;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMobile(@NotNull String mobile) {
        this.mobile = mobile;
    }

    @NotNull
    public String getIds() {
        return ids;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setIds(@NotNull String ids) {
        this.ids = ids;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

}
