package com.vonnie.game.v2048.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.umeng.socialize.UMShareAPI;
import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.constant.Constants;
import com.vonnie.game.v2048.utils.ShareUtil;

/**
 * @author LongpingZou
 * @date 2018/6/25
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initView();
    }

    private void initView() {
        findViewById(R.id.menu_resume).setOnClickListener(this);
        findViewById(R.id.menu_new_game).setOnClickListener(this);
        findViewById(R.id.menu_mode).setOnClickListener(this);
        findViewById(R.id.menu_share).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i("ABC", "click");
        switch (v.getId()) {
            case R.id.menu_resume:
                setResult(Constants.RESULT_CODE_RESUME);
                finish();
                break;
            case R.id.menu_new_game:
                setResult(Constants.RESULT_CODE_NEW_GAME);
                finish();
                break;
            case R.id.menu_mode:
                Intent intent = new Intent(this, ModeActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.menu_share:
                Log.i("ABC", "share");
                ShareUtil.getInstance().showShareDialog(this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(Constants.RESULT_CODE_MODE_CHOOSE, data);
        }
        finish();
    }
}
