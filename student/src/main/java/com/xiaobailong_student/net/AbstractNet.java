package com.xiaobailong_student.net;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.xiaobailong_student.beans.Student;
import com.xiaobailong_student.result.ResponseLoadExamData;
import com.xiaobailong_student.result.ResponseLoginData;
import com.xiaobailong_student.result.ResponseSaveScoreData;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by dongyuangui on 2017/12/26.
 */


public class AbstractNet {
    String server = "http://192.168.1.3:8080";
    public static String loginPath = "/login";
    public static String examination = "/examination";
    public static String saveScores = "/saveScores";
    OkHttpClient client = null;
    private static final String TAG = "AbstractNet";

    private static final class Holder {
        private static final AbstractNet instance = new AbstractNet();
    }

    private AbstractNet() {
        client = new OkHttpClient();
    }

    public static final AbstractNet getInstance() {
        return Holder.instance;
    }

    public ResponseLoginData login(String name, String xuehao) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(xuehao)) {
            return null;
        }
        ResponseLoginData student = null;
        String url = server + loginPath + "?username=" + name + "&xuehao=" + xuehao;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                student = new Gson().fromJson(body.string(), ResponseLoginData.class);
            }
        } catch (IOException e) {
            String msg = e == null ? "unknown error" : e.getMessage();
            Log.e("newwork", msg);
        }

        return student;

    }

    public ResponseSaveScoreData saveScores(Student student) {
        if (student == null) {
            Log.d(TAG, "request para error when saveScores ");
            return null;
        }
        String username = student.getUsername();
        String xuehao = student.getXuehao();
        String classes = student.getClasses() + "";
        String id = student.getId() + "";
        String sex = student.getSex();
        String results = student.getResults() + "";
        String cousumeTime = student.getConsume_time();
        String devices = student.getDevices();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(xuehao)
                || TextUtils.isEmpty(id) || TextUtils.isEmpty(results)) {
            Log.d(TAG, "request para error when saveScores ");
            return null;
        }
        ResponseSaveScoreData responseSaveScoreData = null;
        String url = server + saveScores + "?username=" + username + "&xuehao=" + xuehao
                + "&classes=" + classes + "&id=" + id
                + "&sex=" + sex + "&results="
                + results + "&cousumetime="
                + cousumeTime+ "&devices="
                + devices;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                responseSaveScoreData = new Gson().fromJson(body.string(), ResponseSaveScoreData.class);
            }
        } catch (IOException e) {
            String msg = e == null ? "unknown error" : e.getMessage();
            Log.e("newwork", msg);
        }

        return responseSaveScoreData;

    }

    public ResponseLoadExamData loadExamination() {
        ResponseLoadExamData examData = null;
        String url = server + examination;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                examData = new Gson().fromJson(body.string(), ResponseLoadExamData.class);
            }
        } catch (IOException e) {
            String msg = e == null ? "unknown error" : e.getMessage();
            Log.e("newwork", msg);
        }

        return examData;

    }
}
