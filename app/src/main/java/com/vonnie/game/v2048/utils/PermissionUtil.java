package com.vonnie.game.v2048.utils;

import android.content.Context;
import android.util.Log;

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
    public static void checkStoragePermission(Context context, final PermissionListener permissionListener) {
        AndPermission.with(context)
                .runtime()
                .permission(
                        Permission.Group.STORAGE
                )
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Log.i(TAG, "所有权限申请完成");
                        if (permissionListener != null) {
                            permissionListener.onFinish();
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Log.i(TAG, "用户拒绝权限申请");
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
