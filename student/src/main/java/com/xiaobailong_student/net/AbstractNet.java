package com.xiaobailong_student.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xiaobailong_student.beans.Student;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by dongyuangui on 2017/12/26.
 */


public class AbstractNet {
    String server = "http://192.168.0.20:8080";
    public static String loginPath = "/login";
    OkHttpClient client = null;

    private static final class Holder {
        private static final AbstractNet instance = new AbstractNet();
    }

    private AbstractNet() {
        client = new OkHttpClient();
    }

    public static final AbstractNet getInstance() {
        return Holder.instance;
    }

    public Student login(String name, String xuehao) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(xuehao)) {
            return null;
        }
        Student student = null;
        String url = server + loginPath + "?name=" + name + "&xuehao=" + xuehao;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                student = new Gson().fromJson(body.toString(), Student.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return student;

    }
}
