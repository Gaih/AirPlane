package com.example.gaih.airplane;

import android.content.DialogInterface;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    GameView view = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        view = new GameView(this);
        setContentView(view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode ==KeyEvent.KEYCODE_BACK){
            view.stop();

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("确定退出？");
            alert.setNeutralButton("退出",this);
            alert.setNegativeButton("继续",this);
            alert.create().show();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which ==-2){
            view.start();
        }else {
            Process.killProcess(Process.myPid());
        }
    }
}
