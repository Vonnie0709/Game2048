package com.vonnie.game.v2048.listener;

/**
 * @author LongpingZou
 * @date 2018/6/25
 */
public interface OnFunctionClickListener {
    /**
     * on menu button click
     */
    void onMenuButtonClick();

    /**
     * on mute button click
     */
    void onMuteButtonClick();

    /**
     * on undo button click
     */
    void onUndoButtonClick();

    /**
     * on game button click
     */
    void onNewGameButtonClick();

    /**
     * end of game
     */
    void onEndOfGame();


}
