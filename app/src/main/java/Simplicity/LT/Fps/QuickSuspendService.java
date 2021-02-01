package Simplicity.LT.Fps;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

public class QuickSuspendService extends TileService {
    private final int STATE_OFF = 0;
    private final int STATE_ON = 1;
    private final String LOG_TAG = "QuickSuspendService";
    private int toggleState = STATE_ON;
    //当用户从Edit栏添加到快速设定中调用
    @Override
    public void onTileAdded() {
        Log.d(LOG_TAG, "onTileAdded");
    }
    //当用户从快速设定栏中移除的时候调用
    @Override
    public void onTileRemoved() {
        Log.d(LOG_TAG, "onTileRemoved");
    }
    // 点击的时候
    @Override
    public void onClick() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode("*#*#37263#*#*")));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(QuickSuspendService.this, "启动失败", Toast.LENGTH_SHORT).show();
        }
    }
    // 打开下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    //在TleAdded之后会调用一次
    @Override
    public void onStartListening () {
        Log.d(LOG_TAG, "onStartListening");
    }
    // 关闭下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    // 在onTileRemoved移除之前也会调用移除
    @Override
    public void onStopListening () {
        Log.d(LOG_TAG, "onStopListening");
    }

}