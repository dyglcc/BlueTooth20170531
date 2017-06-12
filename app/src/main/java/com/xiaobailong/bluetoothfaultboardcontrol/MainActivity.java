package com.xiaobailong.bluetoothfaultboardcontrol;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaobailong.activity.ShowResultActivity;
import com.xiaobailong.base.BaseApplication;
import com.xiaobailong.bean.Examination;
import com.xiaobailong.bean.ExaminationDao;
import com.xiaobailong.bean.Student;
import com.xiaobailong.bluetooth.MediaFileListDialog;
import com.xiaobailong.bluetooth.MediaFileListDialogMainpage;
import com.xiaobailong.model.FaultBean;
import com.xiaobailong.titile.WriteTitleActivity;
import com.xiaobailong.tools.ConstValue;
import com.xiaobailong.tools.SpDataUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;

public class MainActivity extends BaseActivity implements OnClickListener,
        OnTouchListener, OnScrollListener {

    public static final int ShortTable = 0;
    public static final int FalseTable = 1;
    public static final int BreakTable = 2;
    public static int TableState = ShortTable;


    public ExaminationDao dao;
    /**
     * 短路1-120继电器状态数据
     */
    private ArrayList<Relay> shortList = new ArrayList<Relay>();

    /**
     * 虚接1-120继电器状态数据
     */
    private ArrayList<Relay> falseList = new ArrayList<Relay>();

    /**
     * 断路1-120继电器状态数据
     */
    private ArrayList<Relay> breakfaultList = new ArrayList<Relay>();

    /**
     * 1-120继电器操作监听
     */
    private Handler theFailurePointSetGVHandler = null;
    /**
     * 基本功能操作监听
     */
    private Handler faultboardOptionHandler = null;

    private TheFailurePointSetAdapter theFailurePointSetAdapter = null;
    /**
     * 基本操作功能类
     */
    private FaultboardOption faultboardOption = null;

    private Button ignitionButton = null;
    private Button startButton = null;
    private Button shutDownButton = null;
    private boolean hasStarted = false;

    private TabHost tabHost = null;
    private TabWidget tabWidget = null;

    private GridView theFailurePointSetGV = null;
    private AutofitTextView tvFileName = null;
    private ViewGroup layout_teacher, layout_student;

    private Button btnSend = null;
    private EditText etTime;

    // 学生面板
    private TextView tv_exam_tip, tv_lasttime;
    private Button button_start_exam, button_submit;
    private Examination examzation;
    private Student student;
    private int count;
    private int resultCount;
    private String loginType;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = BaseApplication.app.daoSession.getExaminationDao();
        loginType = SpDataUtils.getLoginType();
        initSDcard();
        initHandler();
        initCountHandler();
        initData(0);
        initView();
    }

    private Handler countHandler;

    private void initCountHandler() {
        countHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Bundle data = msg.getData();
                    if (data != null) {
                        int second = data.getInt("time");
                        int min = second / 60;
                        int sss = second % 60;
                        tv_lasttime.setText("剩余时间：" + min + "分 " + sss + " 秒");
                        if (second == 0) {
                            tv_lasttime.setText("剩余时间：" + 0 + "分 " + 0 + " 秒");
                            tv_lasttime.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                            int scroes = getScores();
                            student.setResults(scroes);
                            BaseApplication.app.daoSession.getStudentDao().save(student);
                            new AlertDialog.Builder(MainActivity.this).setMessage("考试结束")
                                    .setPositiveButton("查看成绩", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            showResultActivity();
                                        }
                                    }).show();
                            // 判断答案
                            Toast.makeText(MainActivity.this, "考试时间结束", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };

    }

    private void showResultActivity() {
        Intent intent = new Intent(MainActivity.this, ShowResultActivity.class);
        intent.putExtra("student", student);
        startActivity(intent);
    }

    private void initView() {
        //创建中部标签浏览界面
        createTabHost();
        // 点火
        ignitionButton = (Button) findViewById(R.id.Button_Ignition);
        ignitionButton.setOnClickListener(this);
        // 启动
        startButton = (Button) findViewById(R.id.Button_Start);
        startButton.setOnClickListener(this);
        startButton.setOnTouchListener(this);
        // 熄火
        shutDownButton = (Button) findViewById(R.id.Button_ShutDown);
        shutDownButton.setOnClickListener(this);
        // 故障点说明
        findViewById(R.id.Button_PointOfFailureThat).setOnClickListener(this);
        // 状态读取
        findViewById(R.id.Button_StateIsRead).setOnClickListener(this);
        // 全部设置
        findViewById(R.id.Button_SetAll).setOnClickListener(this);
        // 全部清除
        findViewById(R.id.Button_ClearAll).setOnClickListener(this);
        // 全部清除
        findViewById(R.id.Button_Mode_Teach02).setOnClickListener(this);
        tvFileName = (AutofitTextView) findViewById(R.id.tv_name);
        btnSend = (Button) findViewById(R.id.button_send);
        btnSend.setOnClickListener(this);
        etTime = (EditText) findViewById(R.id.et_text);
        layout_student = (ViewGroup) findViewById(R.id.layout_student);
        layout_teacher = (ViewGroup) findViewById(R.id.layout_teacher);

        examzation = loadExamnination();
        // 加载考试题数据
        if (examzation != null) {
            loadExamnination2DataList();
        }
        if (SpDataUtils.TYPE_TEACHER.equals(loginType)) {
            layout_student.setVisibility(View.GONE);
            layout_teacher.setVisibility(View.VISIBLE);

            boolean havealreadySendExamination = checkDbHaveExamination();
            if (havealreadySendExamination) {
                findViewById(R.id.tv_tip2).setVisibility(View.VISIBLE);
            }
            if (examzation != null) {
                etTime.setText(examzation.getMinutes() + "");
            }
        } else {
            layout_student.setVisibility(View.VISIBLE);
            layout_teacher.setVisibility(View.GONE);
            button_start_exam = (Button) findViewById(R.id.button_start_exam);
            button_submit = (Button) findViewById(R.id.button_submit);

            button_start_exam.setOnClickListener(this);
            button_submit.setOnClickListener(this);
            tv_exam_tip = (TextView) findViewById(R.id.tv_exam_tip);
            tv_lasttime = (TextView) findViewById(R.id.tv_lasttime);
//            获取考试题
            if (examzation == null) {
                Toast.makeText(this, "教师还未出题", Toast.LENGTH_SHORT).show();
                tv_exam_tip.setVisibility(View.GONE);
            } else {
                // 显示考试时间 只是展示时间
                tv_lasttime.setText("本次考试时间：" + examzation.getMinutes() + "分钟");
                tv_exam_tip.setVisibility(View.INVISIBLE);
//                etTime.setText(examzation.getMinutes()+"");
//                Toast.makeText(this, "time is " + examzation.getMinutes(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void loadExamnination2DataList() {
        String strBreak = examzation.getBreak_();
        if (!TextUtils.isEmpty(strBreak)) {
            String[] breaks = strBreak.split(",");
            for (int i = 0; i < breaks.length; i++) {
                if (!TextUtils.isEmpty(breaks[i])) {
                    try {
                        int position = Integer.parseInt(breaks[i]);
                        Relay relay = breakfaultList.get(position);
                        relay.setExamination(true);
                    } catch (Exception e) {
                        Log.d("MainActivity", "解析题库出错，" + breaks[i]);
                    }
                }
            }
        }
        String strFalse_ = examzation.getFalse_();
        if (!TextUtils.isEmpty(strFalse_)) {
            String[] falses = strFalse_.split(",");
            for (int i = 0; i < falses.length; i++) {
                if (!TextUtils.isEmpty(falses[i])) {
                    try {
                        int position = Integer.parseInt(falses[i]);
                        Relay relay = falseList.get(position);
                        relay.setExamination(true);
                    } catch (Exception e) {
                        Log.d("MainActivity", "解析题库出错，" + falses[i]);
                    }
                }
            }
        }
        String strShort = examzation.getShort_();
        if (!TextUtils.isEmpty(strShort)) {
            String[] shorts = strShort.split(",");
            for (int i = 0; i < shorts.length; i++) {
                if (!TextUtils.isEmpty(shorts[i])) {
                    try {
                        int position = Integer.parseInt(shorts[i]);
                        Relay relay = shortList.get(position);
                        relay.setExamination(true);
                    } catch (Exception e) {
                        Log.d("MainActivity", "解析题库出错，" + shorts[i]);
                    }
                }
            }
        }
        // 设置题目总数
        count = getSourceCount();
        Log.d("MainActivity", "题库加载完毕");
    }


    private int getCurentTab() {
        return tabHost.getCurrentTab();
    }

    /**
     * 初始化继电器状态数据
     */
    private void initData(int pos) {

        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("student");

        if (shortList.isEmpty()) {
            for (int i = 0; i < 6; i++) {
                shortList.add(new Relay(i + 1, i + 1, Relay.Green));
            }

            for (int i = 0; i < 100; i++) {
                breakfaultList.add(new Relay(i + 1, i + 1, Relay.Green));
            }

            for (int i = 0; i < 20; i++) {
                falseList.add(new Relay(i + 1, i + 1, Relay.Green));
            }
        }
        switch (pos) {
            case 0:
                faultboardOption = new FaultboardOption(this, faultboardOptionHandler,
                        breakfaultList);
                break;
            case 1:
                faultboardOption = new FaultboardOption(this, faultboardOptionHandler,
                        falseList);
                break;
            case 2:
                faultboardOption = new FaultboardOption(this, faultboardOptionHandler,
                        shortList);
                break;
        }


    }

    private void initHandler() {
        theFailurePointSetGVHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Relay relay = (Relay) msg.obj;
                int state = relay.getState();
                relay.setState(Relay.Yellow);
                if (state == Relay.Red) {
                    faultboardOption.shutDown((byte) relay.getId(), msg.arg1);
                } else if (state == Relay.Green) {
                    int id = relay.getId();
                    faultboardOption.start((byte) id, msg.arg1);
                } else if (state == Relay.Yellow) {
                    Toast.makeText(MainActivity.this, "Command had send !",
                            Toast.LENGTH_SHORT).show();
                }
                // add by dyg
                if (SpDataUtils.TYPE_TEACHER.equals(loginType)) {
                    int examNum = getSourceCount();
                    if (examNum < 5) {
                        relay.setExamination(true);
                    } else {
                        Toast.makeText(MainActivity.this, "最多出5道题目", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (relay.isStdentClick()) {
                        relay.setStdentClick(false);
                    } else {
                        relay.setStdentClick(true);
                    }
                    // 设置剩余答题数
                    int clickCount = getClickCount();
                    String str = getString(R.string.already_start2, count + "", clickCount + "");
                    tv_exam_tip.setText(str);
                }

                theFailurePointSetAdapter.notifyDataSetChanged();
            }
        };

        faultboardOptionHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                theFailurePointSetAdapter.notifyDataSetChanged();
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initSDcard() {
        if (ConstValue.haveSdcard()) {
            File file = new File(ConstValue.get_DIR());
            if (!file.exists()) {
                file.mkdirs();
            }
            File title = new File(ConstValue.get_title_File());
            if (!title.exists()) {
                try {
                    title.createNewFile();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "创建文件失败了！！", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            nosdcard();
        }
    }


    public static String readTitleStr(Context context, File file) {
        String str = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((str = reader.readLine()) != null) {
                return str;
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "读取文件出错！", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "读取文件出错！", Toast.LENGTH_LONG).show();
        }
//                todo 创建修改标题的文件，读取文件，修改文件更新标题。
        return str;
    }

    private Examination loadExamnination() {
        return dao.queryBuilder()
                .where(ExaminationDao.Properties.Expired.eq(false))
                .orderDesc(ExaminationDao.Properties.Id).unique();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 开始考试
            case R.id.button_start_exam:
                // 显示倒计时，
                // 显示红色提示view，
                // 时间结束显示对话框，
                // 考试结束，自动提交考试结果。
                boolean canStartExam = checkDbHaveExamination();
                if (canStartExam) {
                    tv_exam_tip.setVisibility(View.VISIBLE);
                    startCountNumber();
                    button_start_exam.setVisibility(View.GONE);
                    tv_exam_tip.setText(getString(R.string.already_start2, count + "", count + ""));
                } else {
                    Toast.makeText(this, "教师还未出题，不能考试", Toast.LENGTH_SHORT).show();
                }

                break;
            // 提交答案
            case R.id.button_submit:
                if (examzation == null) {
                    Toast.makeText(this, "教师还未出题，不能计算成绩", Toast.LENGTH_SHORT).show();
                    return;
                }
                int scores = getScores();
                // 判断分数写入数据库，跳转到显示分数界面
                student.setResults(scores);
                BaseApplication.app.daoSession.getStudentDao().save(student);
                showResultActivity();

                break;
            case R.id.Button_Ignition:// 点火
                if (hasStarted) {
                    Toast.makeText(this, "The machine had be started !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    hasStarted = true;
                    Toast.makeText(this, "The machine be started !",
                            Toast.LENGTH_SHORT).show();
                    faultboardOption.ignition(R.id.Button_Ignition);
                }
                break;
            case R.id.Button_ShutDown:// 熄火
                if (hasStarted) {
                    hasStarted = false;
                    Toast.makeText(this, "The machine be shut down !",
                            Toast.LENGTH_SHORT).show();
                    faultboardOption.fireDown((byte) 0x65, R.id.Button_ShutDown);
                } else {
                    Toast.makeText(this, "The machine had not be started !",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.Button_PointOfFailureThat:// 故障点说明
//                PointOfFailureThatDialog pointOfFailureThatDialog = new PointOfFailureThatDialog(
//                        this);
//                pointOfFailureThatDialog.show();
                MediaFileListDialogMainpage dialog = new MediaFileListDialogMainpage(this);
                dialog.show();
                break;
            case R.id.Button_StateIsRead:// 状态读取
                faultboardOption.stateIsRead(R.id.Button_StateIsRead);
                break;
            case R.id.Button_SetAll:// 全部设置
                faultboardOption.setAll(R.id.Button_SetAll);
                break;
            case R.id.Button_ClearAll:// 全部清除
                faultboardOption.clearAll(R.id.Button_ClearAll);
                break;
            case R.id.Button_Mode_Teach02:// 教学模式
//			todo 打开指定文件夹 /sdcard/
                if (ConstValue.haveSdcard()) {
                    File file = new File(ConstValue.get_DIR());
                    if (!file.exists()) {
                        Toast.makeText(MainActivity.this, "没有文件可以显示！文件路径 " + ConstValue.get_DIR(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    MediaFileListDialog bluetoothDevicesListDialog = new MediaFileListDialog(
                            MainActivity.this);
                    bluetoothDevicesListDialog.show();
                } else {
                    nosdcard();
                }

                break;
            case R.id.button_send:
                // 检查是否可以出题，设备名称，考试时间，考题内容
                boolean isCanstart = checkCanMakeExamination();
                if (!isCanstart) {
                    return;
                }
                String str = etTime.getText().toString();
                String devicesName = tvFileName.getText().toString();
                if (examzation != null) {
                    examzation.setMinutes(Integer.parseInt(str));
                    examzation.setDevices(devicesName);
                    dao.update(examzation);
                } else {
                    if (isCanstart) {
                        StringBuilder fasleString = new StringBuilder();
                        StringBuilder breakString = new StringBuilder();
                        StringBuilder shortString = new StringBuilder();
                        for (int i = 0; i < falseList.size(); i++) {
                            Relay relay = falseList.get(i);
                            if (relay.isExamination()) {
                                fasleString.append(i + ",");
                            }
                        }
                        for (int i = 0; i < breakfaultList.size(); i++) {
                            Relay relay = breakfaultList.get(i);
                            if (relay.isExamination()) {
                                breakString.append(i + ",");
                            }
                        }
                        for (int i = 0; i < shortList.size(); i++) {
                            Relay relay = shortList.get(i);
                            if (relay.isExamination()) {
                                shortString.append(i + ",");
                            }
                        }
                        Examination examination = new Examination();
                        examination.setBreak_(breakString.toString());
                        examination.setFalse_(fasleString.toString());
                        examination.setShort_(shortString.toString());
                        examination.setMinutes(Integer.parseInt(str));
                        examination.setExpired(false);
                        examination.setDevices(devicesName);
                        BaseApplication.app.daoSession.getExaminationDao().save(examination);
                    }
                    Toast.makeText(this, "考题发送完毕，学生可以考试", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.tv_tip2).setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    private int getClickCount() {
        int resultCount = 0;
        for (int i = 0; i < breakfaultList.size(); i++) {
            Relay relay = breakfaultList.get(i);
            if (!relay.isStdentClick()) {
                continue;
            } else {
                resultCount++;
            }
        }
        for (int i = 0; i < falseList.size(); i++) {
            Relay relay = falseList.get(i);
            if (!relay.isStdentClick()) {
                continue;
            } else {
                resultCount++;
            }
        }
        for (int i = 0; i < shortList.size(); i++) {
            Relay relay = shortList.get(i);
            if (!relay.isStdentClick()) {
                continue;
            } else {
                resultCount++;
            }
        }
        return resultCount;
    }

    private int getSourceCount() {
        int sourceCount = 0;
        for (int i = 0; i < breakfaultList.size(); i++) {
            Relay relay = breakfaultList.get(i);
            if (!relay.isExamination()) {
                continue;
            } else {
                sourceCount++;
            }
        }
        for (int i = 0; i < falseList.size(); i++) {
            Relay relay = falseList.get(i);
            if (!relay.isExamination()) {
                continue;
            } else {
                sourceCount++;
            }
        }
        for (int i = 0; i < shortList.size(); i++) {
            Relay relay = shortList.get(i);
            if (!relay.isExamination()) {
                continue;
            } else {
                sourceCount++;
            }
        }
        return sourceCount;
    }

    private int getScores() {
        int sourceCount = 0;
        int rightCount = 0;
        for (int i = 0; i < breakfaultList.size(); i++) {
            Relay relay = breakfaultList.get(i);
            if (!relay.isExamination()) {
                continue;
            } else {
                sourceCount++;
                if (relay.isStdentClick()) {
                    rightCount++;
                }
            }
        }
        for (int i = 0; i < falseList.size(); i++) {
            Relay relay = falseList.get(i);
            if (!relay.isExamination()) {
                continue;
            } else {
                sourceCount++;
                if (relay.isStdentClick()) {
                    rightCount++;
                }
            }
        }
        for (int i = 0; i < shortList.size(); i++) {
            Relay relay = shortList.get(i);
            if (!relay.isExamination()) {
                continue;
            } else {
                sourceCount++;
                if (relay.isStdentClick()) {
                    rightCount++;
                }
            }
        }
        if (sourceCount == 0) {
            Toast.makeText(this, "教师没有出题，无法计算分数", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return (100 / sourceCount) * rightCount;
    }

    private void startCountNumber() {

        int minutes = examzation.getMinutes();
        int seconds = 0;
        if (minutes > 0) {
            seconds = minutes * 60;
        } else {
            Toast.makeText(this, "考试时间异常", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                int seconds = examzation.getMinutes() * 60;
                while (seconds >= 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    seconds--;
                    Message msg = countHandler.obtainMessage();
                    msg.what = 1;
                    Bundle bun = new Bundle();
                    bun.putInt("time", seconds);
                    msg.setData(bun);
                    countHandler.sendMessage(msg);
                }
            }
        }).start();

    }

    private boolean checkDbHaveExamination() {
        Examination examination = dao.queryBuilder()
                .where(ExaminationDao.Properties.Expired
                        .eq(new Boolean(false))).orderDesc(ExaminationDao.Properties.Id).unique();
        if (examination != null && !examination.getExpired()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkCanMakeExamination() {

//        Examination examination = dao.queryBuilder()
//                .where(ExaminationDao.Properties.Expired
//                        .eq(new Boolean(false))).orderDesc(ExaminationDao.Properties.Id).unique();
//        if (examination != null && !examination.getExpired()) {
//            Toast.makeText(this, "考题已经发送完毕!", Toast.LENGTH_SHORT).show();
//        }
        String devicesName = tvFileName.getText().toString();
        if (TextUtils.isEmpty(devicesName)) {
            Toast.makeText(this, "请点击\"故障点说明\"选择对应设备文件", Toast.LENGTH_SHORT).show();
            return false;
        }
        String str = etTime.getText().toString();
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, "请设置考试时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 已经发送完了考题
        int count = getSourceCount();

        if (count == 0) {
            Toast.makeText(this, "还没有选择考题", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void nosdcard() {
        Toast.makeText(MainActivity.this, "没有sdcard,无法显示教学文件", Toast.LENGTH_LONG).show();
    }

    public static ArrayList<String> getDirFilesDir(File file) {
        ArrayList<String> list = new ArrayList<String>();
        for (File f : file.listFiles()) {
            list.add(f.getAbsolutePath());
        }
        return list;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                this.faultboardOption.bluetoothConnect();
                break;
            case R.id.action_exit:
                finish();
                break;
            case R.id.action_close:
                this.faultboardOption.closeBluetoothSocket();
                Toast.makeText(MainActivity.this, "连接已断开",
                        Toast.LENGTH_SHORT).show();
                initData(getCurentTab());
                break;
            case R.id.action_edit_title:
                editTitle();
                break;
            default:
                break;
        }
        return true;
    }

    private void editTitle() {
        startActivityForResult(new Intent(MainActivity.this, WriteTitleActivity.class), 100);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                File file = new File(ConstValue.get_title_File());
                String title = readTitleStr(MainActivity.this, file);
                if (title == null || title.equals("")) {
                    title = getString(R.string.app_name);
                }
                getActionBar().setTitle(title);
            }
        }
    }

    @Override
    public void finish() {
        faultboardOption.closeBluetoothSocket();
        super.finish();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            Toast.makeText(this, getString(R.string.Button_Back_BeCanceled),
//                    Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.Button_Start:// 启动
                if (hasStarted) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Toast.makeText(this, "StartDown", Toast.LENGTH_SHORT).show();
                        faultboardOption.startDown((byte) 0x66, R.id.Button_Start);
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(this, "StartUp", Toast.LENGTH_SHORT).show();
                        faultboardOption.startUp((byte) 0x66, R.id.Button_Start);
                    }
                } else {
                    Toast.makeText(this, "The machine had not be started !",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    private void createTabHost() {
        {
            tabHost = (TabHost) findViewById(R.id.tabhost);
            tabWidget = (TabWidget) findViewById(android.R.id.tabs);
            tabHost.setup();
            tabHost.bringToFront();

            ArrayList<TabHost.TabSpec> hostlist = new ArrayList<TabHost.TabSpec>(
                    2);

            TabHost.TabSpec tabspec1 = tabHost.newTabSpec("0");
            tabspec1.setContent(R.id.GridView_TheFailurePointSet);
            TextView indicatorV = new TextView(this);
            indicatorV.setGravity(Gravity.CENTER);
            indicatorV.setBackgroundResource(R.drawable.channelsbg);
            indicatorV.setTextSize(16);
            indicatorV.setText(R.string.breakfault);
            tabspec1.setIndicator(indicatorV);
            hostlist.add(tabspec1);


            tabspec1 = tabHost.newTabSpec("2");
            tabspec1.setContent(R.id.GridView_TheFailurePointSet);
            indicatorV = new TextView(this);
            indicatorV.setGravity(Gravity.CENTER);
            indicatorV.setBackgroundResource(R.drawable.channelsbg);
            indicatorV.setTextSize(16);
            indicatorV.setText(R.string.falsefault);
            tabspec1.setIndicator(indicatorV);
            hostlist.add(tabspec1);

            tabspec1 = tabHost.newTabSpec("1");
            tabspec1.setContent(R.id.GridView_TheFailurePointSet);
            indicatorV = new TextView(this);
            indicatorV.setGravity(Gravity.CENTER);
            indicatorV.setBackgroundResource(R.drawable.channelsbg);
            indicatorV.setTextSize(16);
            indicatorV.setText(R.string.shortfault);
            tabspec1.setIndicator(indicatorV);
            hostlist.add(tabspec1);


            int j = hostlist.size();
            for (int i = 0; i < j; i++) {
                tabHost.addTab(hostlist.get(i));
            }

            tabHost.setCurrentTab(0);
            View view = tabWidget.getChildAt(0);
            view.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.presschannelbg));

            theFailurePointSetAdapter = new TheFailurePointSetAdapter(this,
                    shortList, theFailurePointSetGVHandler);

            theFailurePointSetGV = (GridView) findViewById(R.id.GridView_TheFailurePointSet);
            theFailurePointSetGV.setAdapter(theFailurePointSetAdapter);
            theFailurePointSetGV.setOnScrollListener(this);
            theFailurePointSetGV.setVisibility(View.VISIBLE);
            theFailurePointSetGV.setNumColumns(3);

            tabHost.setOnTabChangedListener(new OnTabChangeListener() {
                public void onTabChanged(String tabId) {

                    changeListData(tabId);
                    for (int i = 0; i < tabWidget.getChildCount(); i++) {
                        View view = tabWidget.getChildAt(i);
                        if (tabHost.getCurrentTab() == i) {
                            view.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.presschannelbg));
                        } else {
                            view.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.channelsbg));
                        }
                    }
                }
            });

            changeListData("0");
        }

    }

    private void changeListData(String tabId) {
        // 短路故障
        if (tabId.equals("1")) {
            TableState = ShortTable;
            theFailurePointSetAdapter = new TheFailurePointSetAdapter(this,
                    shortList, theFailurePointSetGVHandler);
            theFailurePointSetGV.setAdapter(theFailurePointSetAdapter);
            faultboardOption.setArray(shortList);
        } else if (tabId.equals("2")) { // 虚接故障
            TableState = FalseTable;
            theFailurePointSetAdapter = new TheFailurePointSetAdapter(this,
                    falseList, theFailurePointSetGVHandler);
            theFailurePointSetGV.setAdapter(theFailurePointSetAdapter);
            faultboardOption.setArray(falseList);
        } else {// 断路故障
            TableState = BreakTable;
            theFailurePointSetAdapter = new TheFailurePointSetAdapter(this,
                    breakfaultList, theFailurePointSetGVHandler);
            theFailurePointSetGV.setAdapter(theFailurePointSetAdapter);
            faultboardOption.setArray(breakfaultList);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    public void setFileName(File file) {
        if (file != null && !this.isFinishing()) {
            tvFileName.setVisibility(View.VISIBLE);
            String name = file.getName().toString();
            String names[] = name.split("\\.");
            if (names.length > 0) {
                tvFileName.setText(names[0]);
            }
        } else {
            tvFileName.setVisibility(View.GONE);
        }
    }

    public void setValues(List<FaultBean> datas) {
        if (datas == null) {
            return;
        }

        for (int i = 0; i < breakfaultList.size(); i++) {
            Relay relay = breakfaultList.get(i);
            for (int j = 0; j < datas.size(); j++) {
                FaultBean faultBean = datas.get(j);
                if (faultBean.getType() == ConstValue.type_duanlu) {
                    relay.setValue(faultBean.getValue());
                    relay.setType(faultBean.getType());
                    datas.remove(faultBean);
                    break;
                }

            }
        }
        for (int i = 0; i < falseList.size(); i++) {
            Relay relay = falseList.get(i);
            for (int j = 0; j < datas.size(); j++) {
                FaultBean faultBean = datas.get(j);
                if (faultBean.getType() == ConstValue.type_xujie) {
                    relay.setValue(faultBean.getValue());
                    relay.setType(faultBean.getType());
                    datas.remove(faultBean);
                    break;
                }

            }
        }
        for (int i = 0; i < shortList.size(); i++) {
            Relay relay = shortList.get(i);
            for (int j = 0; j < datas.size(); j++) {
                FaultBean faultBean = datas.get(j);
                if (faultBean.getType() == ConstValue.type_shortFault) {
                    relay.setValue(faultBean.getValue());
                    relay.setType(faultBean.getType());
                    datas.remove(faultBean);
                    break;
                }

            }
        }
        theFailurePointSetAdapter.notifyDataSetChanged();
        Toast.makeText(this, "替换完毕", Toast.LENGTH_SHORT).show();

    }
}
