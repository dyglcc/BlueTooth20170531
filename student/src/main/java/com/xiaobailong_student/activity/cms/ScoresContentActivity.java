//package com.xiaobailong_student.activity.cms;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.xiaobailong_student.base.BaseApplication;
//import com.xiaobailong_student.bean.Classes;
//import com.xiaobailong_student.bean.Student;
//import com.xiaobailong_student.bean.StudentDao;
//import com.xiaobailong_student.bluetoothfaultboardcontrol.BaseActivity;
//import com.xiaobailong_student.bluetoothfaultboardcontrol.R;
//import com.xiaobailong_student.tools.ExcelUtil;
//import com.xiaobailong_student.tools.FileSelectFragment;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//import static android.content.ContentValues.TAG;
//
//
///**
// * Created by dongyuangui on 2017/6/2.
// */
//
//public class ScoresContentActivity extends BaseActivity implements FileSelectFragment.FileSelectCallbacks {
//
//    @BindView(R.id.xuehao)
//    TextView xuehao;
//    @BindView(R.id.username)
//    TextView username;
//    @BindView(R.id.tv_scores)
//    TextView tvScores;
//    @BindView(R.id.tv_consume)
//    TextView tvConsume;
//    @BindView(R.id.list)
//    ListView list;
//    @BindView(R.id.btn_add)
//    Button btnAdd;
//    @BindView(R.id.btn_export)
//    Button btnExport;
//    @BindView(R.id.btn_import)
//    Button btnImport;
//    @BindView(R.id.btn_view_result)
//    Button btnViewResult;
//    private Classes classes;
//    private List<Student> datalist;
//    private List<Student> importList;
//    private StudentDao dao;
//
//    private LayoutInflater inflater = null;
//
//    private ScoresAdapter adapter = null;
//    private String dateStr, deviceName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_scores_content);
//        ButterKnife.bind(this);
//        classes = (Classes) getIntent().getSerializableExtra("classes");
//        dateStr = getIntent().getStringExtra("dateStr");
//        deviceName = getIntent().getStringExtra("devices");
//        if (classes == null) {
//            return;
//        }
//        dao = BaseApplication.app.daoSession.getStudentDao();
//        inflater = LayoutInflater.from(this);
//        getData();
//
//
//    }
//
//    private void getData() {
//        datalist = dao.queryBuilder()
//                .where(StudentDao.Properties.Classes.eq(classes.getId()),
//                        StudentDao.Properties.Semester.eq(dateStr), StudentDao.Properties.Devices.eq(deviceName))
//                .list();
//        adapter = new ScoresAdapter();
//        list.setAdapter(adapter);
////        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////
////                final Student student = datalist.get(position);
////                BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();
////                List<MenuItem> menuItemList = new ArrayList<MenuItem>();
////                MenuItem item0 = new MenuItem();
////                item0.setText("删除学生");
////                item0.setPosition(0);
////                item0.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, item0) {
////                    @Override
////                    public void onClickMenuItem(View v, MenuItem menuItem) {
////
////                        delStudent(student);
////
////                    }
////                });
////                MenuItem item1 = new MenuItem();
////                item1.setText("删除学生信息");
////                item1.setPosition(1);
////                item1.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, item1) {
////                    @Override
////                    public void onClickMenuItem(View v, MenuItem menuItem) {
////                        delStudent(student);
////                    }
////                });
////                MenuItem item3 = new MenuItem();
////                item3.setText("查看学生成绩");
////                item3.setPosition(3);
////                item3.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, item1) {
////                    @Override
////                    public void onClickMenuItem(View v, MenuItem menuItem) {
////
////                        Intent intent = new Intent(ScoresContentActivity.this, ShowResultActivity.class);
////                        intent.putExtra("student", student);
////                        intent.putExtra("view", "view");
////                        startActivity(intent);
////                    }
////                });
////                menuItemList.add(item0);
////                menuItemList.add(item1);
////                menuItemList.add(item3);
////
////                bottomMenuFragment.setMenuItems(menuItemList);
////                bottomMenuFragment.show(getFragmentManager(), "BottomMenuFragment");
////            }
////        });
//    }
//
////    private void delStudent(Student student) {
////        dao.delete(student);
////        datalist = dao.queryBuilder().where(StudentDao.Properties.Classes.eq(classes.getId())).list();
////        adapter.notifyDataSetChanged();
////    }
//
//
////    @OnClick(R.id.btn_add)
////    public void add() {
////        Intent intent = new Intent(this, AddStudentActivity.class);
////        intent.putExtra("classes", classes);
////        startActivityForResult(intent, 100);
////    }
//
////    @OnClick(R.id.btn_view_result)
////    public void viewResult() {
////        Intent intent = new Intent(this, NewDevicesActivity.class);
////        intent.putExtra("classes", classes);
////        startActivityForResult(intent, 100);
////    }
//
////    @OnClick(R.id.btn_import)
////    public void importStudent() {
////        FileSelectFragment.show(this, new String[]{"xls", "xlsx"}, null, new File(ConstValue.getDirStudent()));
////    }
//
//    @OnClick(R.id.btn_export)
//    public void export() {
//        String fileName = classes.getPath() + "/" + classes.getFilename() + ".xls";
//
//        try {
//            List<Student> students = dao.loadAll();
//            List<List<Object>> strs = new ArrayList<>();
//            for (int i = 0; i < students.size(); i++) {
//                Student student = students.get(i);
//                ArrayList<Object> list = new ArrayList<>();
////            顺序，学号、姓名、性别、联系电话、身份证 成绩
//                list.add(student.getXuehao());
//                list.add(student.getUsername());
//                list.add(student.getSex());
//                list.add(student.getMobile());
//                list.add(student.getIds());
//                list.add(student.getResults() == null ? "" : student.getResults());
//                strs.add(list);
//            }
//            ExcelUtil.writeExcel(fileName, strs);
//            Toast.makeText(this, "导出excel完成，保存路径是：" + fileName.toString(), Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(this, "导出失败", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == 100) {
////            if (resultCode == Activity.RESULT_OK) {
////                if (data != null) {
////                    boolean succ = data.getBooleanExtra("added", false);
////                    if (succ) {
////                        datalist.clear();
////                        adapter.notifyDataSetChanged();
////                        datalist = dao.queryBuilder().where(StudentDao.Properties.Classes.eq(classes.getId())).list();
////                        adapter.notifyDataSetChanged();
////                    }
////                }
////            }
////        }
//    }
//
//    @Override
//    public void onConfirmSelect(String absolutePath, String fileName, Map<String, Object> map_param) {
//        try {
//            String fullPath = String.format("%s/%s", absolutePath, fileName);
//            List<List<Object>> data_list = ExcelUtil.read(fullPath);
//            importList = new ArrayList<>();
//            for (int i = 0; i < data_list.size(); i++) {
//                Student item = new Student();
//                List<Object> data_item = data_list.get(i);
//                for (int j = 0; j < data_item.size(); j++) {
//                    Object obj = data_item.get(j);
//                    Log.d(TAG, "i=" + i + ",j=" + j + ",value=" + (String) obj);
//                    if (j == 0) {
//                        item.setXuehao((String) obj);
//                    } else if (j == 1) {
//                        String str = ((String) obj).trim();
//                        item.setUsername(str);
//                    } else if (j == 2) {
//                        item.setSex(((String) obj).trim());
//                    } else if (j == 3) {
//                        item.setMobile(((String) obj).trim());
//                    } else if (j == 4) {
//                        item.setIds(((String) obj).trim());
//                    } else if (j == 5) {
//                        String str = (String) obj;
//                        if ((String) obj != null && !((String) obj).equals("null")) {
//                            item.setResults(Integer.parseInt(str));
//                        }
//                    }
//                    item.setClasses(classes.getId());
//                }
//                importList.add(item);
//            }
//        } catch (Exception e) {
//            Toast.makeText(this, "导入excel出错", Toast.LENGTH_SHORT).show();
//        }
//
//        try {
//            dao.saveInTx(importList);
//        } catch (Exception e) {
//            Toast.makeText(this, "保存数据库失败", Toast.LENGTH_SHORT).show();
//        }
//
//        // 重新加载数据
//        datalist = dao.queryBuilder().where(StudentDao.Properties.Classes.eq(classes.getId())).list();
//        adapter.notifyDataSetChanged();
//
//        Toast.makeText(this, "已导入excel", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public boolean isFileValid(String absolutePath, String fileName, Map<String, Object> map_param) {
//        return true;
//    }
//
//    private class ScoresAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return datalist == null ? 0 : datalist.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.item_scores, null);
//                holder = new ViewHolder(convertView);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            Student student = datalist.get(position);
//            holder.xuehao.setText(student.getXuehao() + "");
//            holder.username.setText(student.getUsername());
//            holder.tvScores.setText(student.getResults()+"");
//            holder.tvConsume.setText(student.getConsume_time());
//            return convertView;
//        }
//
//
//    }
//
//    static class ViewHolder {
//        @BindView(R.id.xuehao)
//        TextView xuehao;
//        @BindView(R.id.username)
//        TextView username;
//        @BindView(R.id.tv_scores)
//        TextView tvScores;
//        @BindView(R.id.tv_consume)
//        TextView tvConsume;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}
