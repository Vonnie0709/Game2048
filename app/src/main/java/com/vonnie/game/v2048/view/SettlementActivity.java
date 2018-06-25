package com.vonnie.game.v2048.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vonnie.game.v2048.R;

/**
 * @author longping
 * @date 2018/6/23
 */
public class SettlementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        getSupportActionBar().hide();
    }
}
