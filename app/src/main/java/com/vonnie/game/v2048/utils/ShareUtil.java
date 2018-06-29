package com.vonnie.game.v2048.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.weiget.CustomBottomDialog;

/**
 * @author LongpingZou
 * @date 2018/6/27
 */
public class ShareUtil implements View.OnClickListener {
    private static ShareUtil shareUtil;
    private CustomBottomDialog dialog;

    private ShareUtil() {

    }

    public static ShareUtil getInstance() {
        if (shareUtil == null) {
            shareUtil = new ShareUtil();
        }
        return shareUtil;
    }

    public CustomBottomDialog showShareDialog(Context context) {
        dialog = new CustomBottomDialog(context, R.layout.layout_share);
        dialog.findViewById(R.id.share_panel_close).setOnClickListener(this);
        dialog.findViewById(R.id.share_goto_kj).setOnClickListener(this);
        dialog.findViewById(R.id.share_goto_pengyouquan).setOnClickListener(this);
        dialog.findViewById(R.id.share_goto_qq).setOnClickListener(this);
        dialog.findViewById(R.id.share_goto_weibo).setOnClickListener(this);
        dialog.findViewById(R.id.share_goto_weixin).setOnClickListener(this);
        Log.i("ABC", "show");
        dialog.show();
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_goto_kj:
                break;
            case R.id.share_goto_pengyouquan:
                break;
            case R.id.share_goto_qq:
                break;
            case R.id.share_goto_weibo:
                break;
            case R.id.share_goto_weixin:
                break;
            case R.id.share_panel_close:
                break;
            default:
                break;
        }
    }
}
