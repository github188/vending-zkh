package com.mc.vending.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.Tools;
import com.mc.vending.tools.UpdateAPK;

public class VersionActivity extends Activity {
    // public String strURL =
    // "http://mobile.mbaobao.com/files/android/1.5/Maibaobao-MbbMbbwebA5-V1.5.0.apk";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.version);
        ActivityManagerTool.getActivityManager().add(this);
        ((TextView) findViewById(R.id.vermsg))
            .setText(getString(R.string.PUBLIC_UPDATE_NEW_VERSION_TEXT1)
                     + getIntent().getStringExtra("vermsg")
                     + getString(R.string.PUBLIC_UPDATE_NEW_VERSION_TEXT2));
        if (!Tools.isAccessNetwork(this)) {
        } else {
            new UpdateAPK(VersionActivity.this, getIntent().getStringExtra("url")).check();
        }
    }

    /*
     * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
     * Auto-generated method stub if (keyCode == KeyEvent.KEYCODE_BACK &&
     * event.getRepeatCount() == 0) { return true; } return
     * super.onKeyDown(keyCode, event); }
     */

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
    }
}