package Simplicity.LT.Fps;

import android.content.ComponentName;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import miui.app.AlertDialog;
import miui.os.SystemProperties;
import miui.preference.PreferenceActivity;

/**
 *
 */

public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

//    private CheckBoxPreference fpsSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getDarkModeStatus(this)) {
            setTheme(miui.R.style.Theme_Dark_Settings);
        } else {
            setTheme(miui.R.style.Theme_Light_Settings);
        }
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.miui);
//
//        String[] command = new String[]{""};
//        ShellUtils.execCommand(command, true);


    }


    public static boolean getDarkModeStatus(Context context) {
        int mode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {


        return true;
    }


    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, android.preference.Preference preference) {
//        this.fpsSwitch = (CheckBoxPreference) findPreference("fpsSwitch");

//        if (preference == this.fpsSwitch) {
//            if (miui.os.SystemProperties.getBoolean("persist.lt_maxfps",true)) {
//                miui.os.SystemProperties.set("persist.lt_maxfps", false);
//            } else {
//                miui.os.SystemProperties.set("persist.lt_maxfps", true);
//            }
//        }


        if (preference.getKey().equals("ca")) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coolapk.com/u/883441")));
                Toast.makeText(this, "乌堆小透明：靓仔，点个关注吧！", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "本机未安装酷安应用", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("http://www.coolapk.com/u/883441");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }

        if (preference.getKey().equals("xfc")) {

            AlertDialog.Builder dialog = new AlertDialog.
                    Builder(MainActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("点击拨号，即可打开");
            dialog.setCancelable(false);
            dialog.setPositiveButton("我知道了", new DialogInterface.
                    OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode("*#*#37263#*#*")));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "启动失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();


        }


        if (preference.getKey().equals("sybz")) {

            AlertDialog.Builder dialog = new AlertDialog.
                    Builder(MainActivity.this);
            dialog.setTitle("使用帮助");
            dialog.setMessage("使用帮助：\n" +
                    "下拉状态栏，在控制中心中添加“冻结帧率”，“帧率浮窗”即可使用。\n" +
                    "\n" +
                    "冻结帧率指冻结目前的帧率，如：\n" +
                    "在60Hz上限的软件冻结将会全局60hz\n" +
                    "在90hz上限的软件冻结将会全局90hz\n" +
                    "……" +
                    "\n" +
                    "\n" +
                    "实现原理：\n" +
                    "通过注入控制器Smali代码到PowerKeeper实现实时冻结。"
            );
            dialog.setCancelable(false);
            dialog.setPositiveButton("我知道了", new DialogInterface.
                    OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();


        }


        return false;
    }


}