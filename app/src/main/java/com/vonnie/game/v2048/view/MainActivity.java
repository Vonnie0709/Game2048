package com.vonnie.game.v2048.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.vonnie.game.v2048.cell.Tile;
import com.vonnie.game.v2048.constant.Constants;
import com.vonnie.game.v2048.constant.IntentConstant;
import com.vonnie.game.v2048.constant.SpConstant;
import com.vonnie.game.v2048.listener.OnFunctionClickListener;
import com.vonnie.game.v2048.logic.GameController;
import com.vonnie.game.v2048.utils.SharedPreferenceUtil;
import com.vonnie.game.v2048.weiget.GameView;

/**
 * @author LongpingZou
 */
public class MainActivity extends AppCompatActivity implements OnFunctionClickListener {


    private GameController gameController;
    private final static int REQUEST_CODE_MENU = 0;
    private final static int REQUEST_CODE_SETTLEMENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        GameView view = new GameView(getBaseContext());
        gameController = new GameController(this, view);
        gameController.setOnFunctionClickListener(this);
        gameController.newGame();
        view.setGameController(gameController);

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
            gameController.move(2);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            gameController.move(0);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            gameController.move(3);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            gameController.move(1);
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
        Tile[][] field = gameController.grid.field;
        Tile[][] undoField = gameController.grid.undoField;
        SharedPreferenceUtil.put(this, SpConstant.WIDTH, field.length);
        SharedPreferenceUtil.put(this, SpConstant.HEIGHT, field.length);
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
        SharedPreferenceUtil.put(this, SpConstant.SCORE, gameController.currentScore);
        SharedPreferenceUtil.put(this, SpConstant.HIGH_SCORE_TEMP, gameController.historyHighScore);
        SharedPreferenceUtil.put(this, SpConstant.UNDO_SCORE, gameController.lastScore);
        SharedPreferenceUtil.put(this, SpConstant.CAN_UNDO, gameController.canUndo);
        SharedPreferenceUtil.put(this, SpConstant.GAME_STATE, gameController.gameState);
        SharedPreferenceUtil.put(this, SpConstant.UNDO_GAME_STATE, gameController.lastGameState);
        SharedPreferenceUtil.put(this, SpConstant.AUDIO_ENABLED, gameController.isAudioEnabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        //Stopping all animations
        gameController.animGrid.cancelAnimations();

        for (int xx = 0; xx < gameController.grid.field.length; xx++) {
            for (int yy = 0; yy < gameController.grid.field[0].length; yy++) {
                int value = (int) SharedPreferenceUtil.get(this, xx + "_" + yy, -1);
                if (value > 0) {
                    gameController.grid.field[xx][yy] = new Tile(xx, yy, value);
                } else if (value == 0) {
                    gameController.grid.field[xx][yy] = null;
                }

                int undoValue = (int) SharedPreferenceUtil.get(this, SpConstant.UNDO_GRID + xx + "_" + yy, -1);
                if (undoValue > 0) {
                    gameController.grid.undoField[xx][yy] = new Tile(xx, yy, undoValue);
                } else if (value == 0) {
                    gameController.grid.undoField[xx][yy] = null;
                }
            }
        }

        gameController.currentScore = (long) SharedPreferenceUtil.get(this, SpConstant.SCORE, gameController.currentScore);
        gameController.historyHighScore = (long) SharedPreferenceUtil.get(this, SpConstant.HIGH_SCORE_TEMP, gameController.historyHighScore);
        gameController.lastScore = (long) SharedPreferenceUtil.get(this, SpConstant.UNDO_SCORE, gameController.lastScore);
        gameController.canUndo = (boolean) SharedPreferenceUtil.get(this, SpConstant.CAN_UNDO, gameController.canUndo);
        gameController.gameState = (int) SharedPreferenceUtil.get(this, SpConstant.GAME_STATE, gameController.gameState);
        gameController.lastGameState = (int) SharedPreferenceUtil.get(this, SpConstant.UNDO_GAME_STATE, gameController.lastGameState);
        gameController.isAudioEnabled = (boolean) SharedPreferenceUtil.get(this, SpConstant.AUDIO_ENABLED, gameController.isAudioEnabled);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MENU) {
            if (resultCode == Constants.RESULT_CODE_RESUME) {

            } else if (resultCode == Constants.RESULT_CODE_NEW_GAME) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        gameController.newGame();
                    }
                });

            } else if (resultCode == Constants.RESULT_CODE_MODE) {

            } else if (resultCode == Constants.RESULT_CODE_SHARE) {

            } else if (resultCode == Constants.RESULT_CODE_MODE_CHOOSE) {
                int mode = data.getIntExtra(IntentConstant.MODE_TYPE, 0);
                gameController.setGameMode(mode);
            }
        } else if (requestCode == REQUEST_CODE_SETTLEMENT) {
            if (resultCode == Constants.RESULT_CODE_ENDLESS) {
                //do nothing
            } else if (resultCode == Constants.RESULT_CODE_NEW_GAME) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        gameController.newGame();
                    }
                });
            }
        }
    }

    @Override
    public void onMenuButtonClick() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivityForResult(intent, REQUEST_CODE_MENU);
    }

    @Override
    public void onMuteButtonClick() {
        gameController.mute();
    }

    @Override
    public void onUndoButtonClick() {
        gameController.undoGame();
    }

    @Override
    public void onNewGameButtonClick() {
        gameController.newGame();
    }

    @Override
    public void onEndOfGame() {
        if (gameController.gameState == GameController.GAME_WIN) {
            gameController.setEndlessMode();
        }
        Intent intent = new Intent(this, SettlementActivity.class);
        intent.putExtra(IntentConstant.INTENT_SCORE, gameController.currentScore);
        intent.putExtra(IntentConstant.INTENT_GAME_STATUS, gameController.gameState);
        startActivityForResult(intent, REQUEST_CODE_SETTLEMENT);
    }


}
