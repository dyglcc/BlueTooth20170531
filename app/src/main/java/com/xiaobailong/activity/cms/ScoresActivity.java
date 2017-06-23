package com.xiaobailong.activity.cms;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaobailong.activity.AddFileActivity;
import com.xiaobailong.base.BaseApplication;
import com.xiaobailong.bean.Classes;
import com.xiaobailong.bean.Student;
import com.xiaobailong.bean.StudentDao;
import com.xiaobailong.bluetoothfaultboardcontrol.BaseActivity;
import com.xiaobailong.bluetoothfaultboardcontrol.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dongyuangui on 2017/6/1.
 */

/**
 * Created by dongyuangui on 2017/6/1.
 */

public class ScoresActivity extends BaseActivity {
    @BindView(R.id.grid)
    GridView grid;
    ScoresAdapter adapter;
    @BindView(R.id.parent)
    LinearLayout parent;
    GestureDetector dec = null;

    Classes classes;
    StudentDao dao;
    private String deviceName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scores);
        ButterKnife.bind(this);
        // get data

        classes = (Classes) getIntent().getSerializableExtra("classes");
        deviceName = getIntent().getStringExtra("device");

        getData();
        adapter = new ScoresAdapter();
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dateStr = groups.get(position);
                Intent intent = new Intent(ScoresActivity.this, ScoresContentActivity.class);
                intent.putExtra("classes", classes);
                intent.putExtra("dateStr", dateStr);
                intent.putExtra("devices", deviceName);
                startActivity(intent);
            }
        });

    }

    private void longlick() {
        Intent intent = new Intent(this, AddFileActivity.class);
        startActivityForResult(intent, 100);
    }


    private int editFilePos = -1;

    // 设备下面不同日期的考试
    List<Student> times;
    List<String> groups;

    private void getData() {
        times = new ArrayList<>();
        groups = new ArrayList<>();
        dao = BaseApplication.app.daoSession.getStudentDao();

        times = dao.queryBuilder().where(StudentDao.Properties.Devices.eq(deviceName))
                .orderDesc(StudentDao.Properties.Semester).build().list();

        HashMap<String, Student> map = new HashMap<>();
        for (int i = 0; i < times.size(); i++) {
            Student stu = times.get(i);
            if (!map.containsKey(stu.getSemester())) {
                map.put(stu.getSemester(), stu);
            }
        }
        for (Map.Entry<String, Student> entry : map.entrySet()) {
            String key = entry.getKey();
            groups.add(key);
        }
    }


    class ScoresAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return groups == null ? 0 : groups.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            YeadViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(ScoresActivity.this).inflate(R.layout.item_grid_scores, null);
                holder = new YeadViewHolder();
                holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
                convertView.setTag(holder);

            } else {
                holder = (YeadViewHolder) convertView.getTag();
            }
            Student s = times.get(position);
            holder.fileName.setText(s.getSemester().toString());
            return convertView;
        }
    }

    final class YeadViewHolder {
        TextView fileName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
