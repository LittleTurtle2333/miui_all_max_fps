package Simplicity.LT.Fps;

import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class QuickSettingService extends TileService {
    private final int STATE_OFF = 0;
    private final int STATE_ON = 1;
    private final String LOG_TAG = "QuickSettingService";
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
        Log.d(LOG_TAG, "onClick state = " + getQsTile().getState());
        Icon icon;
        if (Settings.System.getInt(this.getContentResolver(), "littleturtle_max_fps", 0)==1) {
            Settings.System.putInt(this.getContentResolver(), "littleturtle_max_fps", 0);
            toggleState = STATE_OFF;
            getQsTile().setState(Tile.STATE_INACTIVE);// 更改成非活跃状态

        } else {
            Settings.System.putInt(this.getContentResolver(), "littleturtle_max_fps", 1);
            toggleState = STATE_ON;
            getQsTile().setState(Tile.STATE_ACTIVE);//更改成活跃状态

        }
//
//        getQsTile().setIcon(icon);//设置图标
        getQsTile().updateTile();//更新Tile
    }

    // 打开下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    //在TleAdded之后会调用一次
    @Override
    public void onStartListening() {
        Log.d(LOG_TAG, "onStartListening");
        if (Settings.System.getInt(this.getContentResolver(), "littleturtle_max_fps", 0)==1) {
            toggleState = STATE_ON;
            getQsTile().setState(Tile.STATE_ACTIVE);//更改成活跃状态
        } else {
            toggleState = STATE_OFF;
            getQsTile().setState(Tile.STATE_INACTIVE);// 更改成非活跃状态
        }
        getQsTile().updateTile();//更新Tile
    }


    // 关闭下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    // 在onTileRemoved移除之前也会调用移除
    @Override
    public void onStopListening() {
        Log.d(LOG_TAG, "onStopListening");
    }

}