package com.xiaobailong.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaobailong.bluetooth.MediaFileListDialog;
import com.xiaobailong.bluetoothfaultboardcontrol.BaseActivity;
import com.xiaobailong.bluetoothfaultboardcontrol.R;
import com.xiaobailong.tools.ConstValue;
import com.xiaobailong.tools.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstActivity extends BaseActivity {

    @BindView(R.id.img_brand)
    ImageView imgBrand;
    @BindView(R.id.btn_fault_entry)
    Button btnFaultEntry;
    @BindView(R.id.btn_anther_app)
    Button btnAntherApp;
    @BindView(R.id.btn_library)
    Button btnLibrary;
    @BindView(R.id.btn_screening)
    Button btnScreening;
    private String savePath = null;
    private boolean hasSdcard = false;
    private String companyBrandFileName = "brand.png";
    public static String backgroundFileName = "background.jpg";

    ImageView rl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_first);
        ButterKnife.bind(this);

        getSdcardPath();
        rl = (ImageView) findViewById(R.id.login_Background);
        if (savePath != null && hasSdcard) {
            File file = new File(savePath + backgroundFileName);
            if (file.exists()) {
                rl.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(savePath +
                        backgroundFileName)));
            }
        }
//        init brand image
        imgBrand = (ImageView) findViewById(R.id.img_brand);
        if (savePath != null && hasSdcard) {
            File file = new File(savePath + companyBrandFileName);
            if (file.exists()) {
                imgBrand.setBackgroundDrawable(new BitmapDrawable(savePath + companyBrandFileName));
            }
        }
    }

    @OnClick(R.id.btn_fault_entry)
    public void click() {
        startActivity(new Intent(FirstActivity.this, EntryActivity.class));
    }

    @OnClick(R.id.btn_anther_app)
    public void clickAntherApp() {
        Utils.launchapp(this);
    }

    @OnClick(R.id.btn_library)
    public void clickLibrary() {
        if (ConstValue.haveSdcard()) {
            File file = new File(ConstValue.get_DIR());
            if (!file.exists()) {
                Toast.makeText(FirstActivity.this, "没有文件可以显示！文件路径 " + ConstValue.get_DIR(), Toast
                        .LENGTH_LONG).show();
                return;
            }
            MediaFileListDialog bluetoothDevicesListDialog = new MediaFileListDialog(
                    FirstActivity.this);
            bluetoothDevicesListDialog.show();
        } else {
            Utils.nosdcard(this);
        }
    }

    @OnClick(R.id.btn_screening)
    public void btnScreening() {
        Intent intent = new Intent("/");
//        ComponentName cm = new ComponentName("com.android.settings","com.android.settings
// .SubSettings");
        ComponentName cm = new ComponentName("com.huawei.android.airsharing", "com.huawei.android" +
                ".airsharing.player.DeviceListActivity");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        startActivityForResult(intent, 0);
    }

    //    com.android.settings.DeviceAdminSettings 设备管理器
//
//    com.android.settings.DeviceInfoSettings 关于手机
//
//    com.android.settings.Display 显示——设置显示字体大小及预览
//
//    com.android.settings.DisplaySettings 显示设置
    public void getSdcardPath() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            hasSdcard = true;
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/autoblue/";
//			Toast.makeText(this, sdcardPath, Toast.LENGTH_LONG).show();
        }
    }
}
