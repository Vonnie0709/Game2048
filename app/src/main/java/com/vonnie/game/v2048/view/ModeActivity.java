package com.vonnie.game.v2048.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.adapter.ModeAdapter;
import com.vonnie.game.v2048.constant.IntentConstant;

import java.util.Arrays;
import java.util.List;

/**
 * @author LongpingZou
 * @date 2018/6/26
 */
public class ModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initView();
    }

    private void initView() {
        List<String> data = Arrays.asList(getResources().getStringArray(R.array.mode_list));
        RecyclerView mRecyclerView = findViewById(R.id.mode_list_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ModeAdapter modeAdapter = new ModeAdapter(data);
        mRecyclerView.setAdapter(modeAdapter);

        modeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(IntentConstant.INTENT_MODE_TYPE, position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
