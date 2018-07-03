package com.vonnie.game.v2048.utils;

import android.content.Context;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

/**
 * @author LongpingZou
 * @date 2018/6/28
 */
public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    /**
     * 读取文件权限
     *
     * @param context
     * @param permissionListener
     */
    public static void checkGamePermission(Context context, final PermissionListener permissionListener) {
        AndPermission.with(context)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE, Permission.READ_PHONE_STATE, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (permissionListener != null) {
                            permissionListener.onFinish();
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                    }
                })
                .start();
    }


    public interface PermissionListener {
        /**
         * 申请完成回调
         */
        void onFinish();
    }
}
