package com.vonnie.game.v2048.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.vonnie.game.v2048.cell.Tile;
import com.vonnie.game.v2048.constant.SpConstant;
import com.vonnie.game.v2048.utils.SharedPreferenceUtil;
import com.vonnie.game.v2048.weiget.GameView;

/**
 * @author LongpingZou
 */
public class MainActivity extends AppCompatActivity {


    private GameView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = new GameView(getBaseContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(SpConstant.SAVE_INSTANCE)) {
                load();
            }
        }
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            view.game.move(2);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            view.game.move(0);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            view.game.move(3);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            view.game.move(1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(SpConstant.SAVE_INSTANCE, true);
        save();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    private void save() {
        Tile[][] field = view.game.grid.field;
        Tile[][] undoField = view.game.grid.undoField;
        SharedPreferenceUtil.put(this, SpConstant.WIDTH, field.length);
        SharedPreferenceUtil.put(this, SpConstant.HEIGHT, field.length);
        Log.i("ABC", "fieldLength:" + field.length);
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] != null) {
                    SharedPreferenceUtil.put(this, xx + "_" + yy, field[xx][yy].getValue());
                } else {
                    SharedPreferenceUtil.put(this, xx + "_" + yy, 0);
                }

                if (undoField[xx][yy] != null) {
                    SharedPreferenceUtil.put(this, SpConstant.UNDO_GRID + xx + "_" + yy, undoField[xx][yy].getValue());
                } else {
                    SharedPreferenceUtil.put(this, SpConstant.UNDO_GRID + xx + "_" + yy, 0);
                }
            }
        }
        SharedPreferenceUtil.put(this, SpConstant.SCORE, view.game.currentScore);
        SharedPreferenceUtil.put(this, SpConstant.HIGH_SCORE_TEMP, view.game.historyHighScore);
        SharedPreferenceUtil.put(this, SpConstant.UNDO_SCORE, view.game.lastScore);
        SharedPreferenceUtil.put(this, SpConstant.CAN_UNDO, view.game.canUndo);
        SharedPreferenceUtil.put(this, SpConstant.GAME_STATE, view.game.gameState);
        SharedPreferenceUtil.put(this, SpConstant.UNDO_GAME_STATE, view.game.lastGameState);
        Log.i("ABC", "save:view.game.gameState:" + view.game.gameState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        //Stopping all animations
        view.game.animGrid.cancelAnimations();

        for (int xx = 0; xx < view.game.grid.field.length; xx++) {
            for (int yy = 0; yy < view.game.grid.field[0].length; yy++) {
                int value = (int) SharedPreferenceUtil.get(this, xx + "_" + yy, -1);
                if (value > 0) {
                    view.game.grid.field[xx][yy] = new Tile(xx, yy, value);
                } else if (value == 0) {
                    view.game.grid.field[xx][yy] = null;
                }

                int undoValue = (int) SharedPreferenceUtil.get(this, SpConstant.UNDO_GRID + xx + "_" + yy, -1);
                if (undoValue > 0) {
                    view.game.grid.undoField[xx][yy] = new Tile(xx, yy, undoValue);
                } else if (value == 0) {
                    view.game.grid.undoField[xx][yy] = null;
                }
            }
        }

        view.game.currentScore = (long) SharedPreferenceUtil.get(this, SpConstant.SCORE, view.game.currentScore);
        view.game.historyHighScore = (long) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE_TEMP, view.game.historyHighScore);
        view.game.lastScore = (long) SharedPreferenceUtil.get(this, SpConstant.UNDO_SCORE, view.game.lastScore);
        view.game.canUndo = (boolean) SharedPreferenceUtil.get(this, SpConstant.CAN_UNDO, view.game.canUndo);
        view.game.gameState = (int) SharedPreferenceUtil.get(this, SpConstant.GAME_STATE, view.game.gameState);
        view.game.lastGameState = (int) SharedPreferenceUtil.get(this, SpConstant.UNDO_GAME_STATE, view.game.lastGameState);
        Log.i("ABC", "get:view.game.gameState:" + view.game.gameState);
    }

//    /**
//     * 有米广告
//     */
//    private void initYMAds() {
//        AdManager.getInstance(this).init("您的应用发布ID", "您的应用密钥", false);
//    }
//
//    private void loadYMAds() {
//        // 实例化 LayoutParams（重要）
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//
//        // 设置广告条的悬浮位置
//        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
//        // 实例化广告条
//        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
//        adView.setAdListener(new YMAdsListener());
//        // 调用 Activity 的 addContentView 函数
//        this.addContentView(adView, layoutParams);
//    }
//
//    private class YMAdsListener implements AdViewListener {
//
//        @Override
//        public void onReceivedAd(AdView adView) {
//            // 切换广告并展示
//        }
//
//        @Override
//        public void onSwitchedAd(AdView adView) {
//            // 请求广告成功
//        }
//
//        @Override
//        public void onFailedToReceivedAd(AdView adView) {
//            // 请求广告失败
//        }
//    }
//
//    /**
//     * 加载酷果广告
//     */
//    private void loadKGAds() {
//        BManager.showTopBanner(MainActivity.this, BManager.CENTER_BOTTOM, BManager.MODE_APPIN, Const.COOID, Const.QQ_CHID);
//        BManager.setBMListner(new ADSListener());
//    }
//
//    private class ADSListener implements MyBMDevListner {
//
//        @Override
//        public void onInstall(int i) {
//            Log.i(TAG, "安装成功");
//        }
//
//        @Override
//        public void onShowBanner() {
//            Log.i(TAG, "广告显示成功");
//        }
//    }
}
