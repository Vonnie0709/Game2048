package com.vonnie.game.v2048.weiget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vonnie.game.v2048.R;


/**
 * @author LongpingZou
 * @date 2017/12/19
 */
public class CustomBottomDialog extends Dialog {


    public CustomBottomDialog(@NonNull Context context, int layoutId) {
        super(context, R.style.Theme_Light_Dialog);
        initView(context, layoutId);
    }


    public CustomBottomDialog(@NonNull Context context, int layoutId, int style) {
        super(context, style);
        initView(context, layoutId);
    }

    /**
     * init Dialog View
     *
     * @param context
     * @param layoutId
     */
    private void initView(Context context, int layoutId) {
        View dialogView = LayoutInflater.from(context).inflate(layoutId, null);
        //获得dialog的window窗口
        Window window = getWindow();
        //设置dialog在屏幕底部
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.CustomDialogAnim);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        this.setContentView(dialogView);
    }

}
