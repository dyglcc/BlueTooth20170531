package com.xiaobailong_student.result;

import com.xiaobailong_student.beans.Student;

/**
 * Created by dongyuangui on 2017/12/30.
 */

public class ResponseLoginData {
    private int error ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Student getData() {
        return data;
    }

    public void setData(Student data) {
        this.data = data;
    }

    private Student data;
}
