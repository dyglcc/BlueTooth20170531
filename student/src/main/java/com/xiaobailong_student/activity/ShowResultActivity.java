package com.xiaobailong_student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaobailong_student.bluetoothfaultboardcontrol.BaseActivity;
import com.xiaobailong_student.bluetoothfaultboardcontrol.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dongyuangui on 2017/6/6.
 */

public class ShowResultActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_name_value)
    TextView tvNameValue;
    @BindView(R.id.tv_xuehao)
    TextView tvXuehao;
    @BindView(R.id.tv_xuehao_value)
    TextView tvXuehaoValue;
    @BindView(R.id.tv_devices)
    TextView tvDevices;
    @BindView(R.id.tv_devices_value)
    TextView tvDevicesValue;
    @BindView(R.id.tv_scores)
    TextView tvScores;
    @BindView(R.id.tv_scores_value)
    TextView tvScoresValue;
    @BindView(R.id.button_save)
    Button buttonSave;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_show_result);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        str = intent.getStringExtra("view");
//        if (str != null) {
//            buttonSave.setVisibility(View.GONE);
//        }
//        todo 需要接口获取分钟,学生姓名，学号，成绩。
        String minutes = null;
        String xuehao = null;
        String username = null;
        String result__ = null;
        if (minutes == null) {
            minutes = "暂无时间";
        } else {
            minutes += "分钟";
        }
        tvDevicesValue.setText(minutes);
        tvNameValue.setText(username);
        tvXuehaoValue.setText(xuehao + "");
        String result =  result__ ==null ? "暂无成绩" : result__  + "";
        tvScoresValue.setText(result);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(str)) {
            setResult(Activity.RESULT_OK, new Intent());
        }
        super.onBackPressed();
    }
}
