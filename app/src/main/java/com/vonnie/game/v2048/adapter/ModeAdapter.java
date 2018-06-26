package com.vonnie.game.v2048.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vonnie.game.v2048.R;

import java.util.List;

/**
 * @author longp
 * @date 2018/6/26
 */
public class ModeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ModeAdapter(@Nullable List<String> data) {
        super(R.layout.layout_mode, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.mode_name, item);
    }
}
