package com.vonnie.game.v2048.cell;

/**
 * @author LongpingZou
 * @date 2018/6/19
 */
public class AnimCell extends Cell {
    private int animationType;
    private long timeElapsed;
    private long animationTime;
    private long delayTime;
    public int[] extras;

    public AnimCell(int x, int y, int animationType, long length, long delay, int[] extras) {
        super(x, y);
        this.animationType = animationType;
        animationTime = length;
        delayTime = delay;
        this.extras = extras;
    }

    public int getAnimationType() {
        return animationType;
    }

    public void tick(long timeElapsed) {
        this.timeElapsed = this.timeElapsed + timeElapsed;
    }

    public boolean animationDone() {
        return animationTime + delayTime < timeElapsed;
    }

    public double getPercentageDone() {
        return Math.max(0, 1.0 * (timeElapsed - delayTime) / animationTime);
    }

    public boolean isActive() {
        return (timeElapsed >= delayTime);
    }
}
