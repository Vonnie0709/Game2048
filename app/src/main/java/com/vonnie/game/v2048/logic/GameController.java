package com.vonnie.game.v2048.logic;

import android.content.Context;
import android.media.SoundPool;

import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.cell.Cell;
import com.vonnie.game.v2048.cell.Tile;
import com.vonnie.game.v2048.constant.SpConstant;
import com.vonnie.game.v2048.grid.AnimGrid;
import com.vonnie.game.v2048.grid.Grid;
import com.vonnie.game.v2048.listener.OnFunctionClickListener;
import com.vonnie.game.v2048.utils.SharedPreferenceUtil;
import com.vonnie.game.v2048.weiget.GameView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LongpingZou
 * @date 2018/6/19
 */
public class GameController {

    private OnFunctionClickListener onFunctionClickListener;
    public static final int SPAWN_ANIMATION = -1;
    public static final int MOVE_ANIMATION = 0;
    public static final int MERGE_ANIMATION = 1;

    public static final int FADE_GLOBAL_ANIMATION = 0;

    private static final long MOVE_ANIMATION_TIME = GameView.BASE_ANIMATION_TIME;
    private static final long SPAWN_ANIMATION_TIME = GameView.BASE_ANIMATION_TIME;
    private static final long NOTIFICATION_ANIMATION_TIME = GameView.BASE_ANIMATION_TIME * 5;
    private static final long NOTIFICATION_DELAY_TIME = MOVE_ANIMATION_TIME + SPAWN_ANIMATION_TIME;

    private static final int STARTING_MAX_VALUE = 2048;
    private static int endingMaxValue;

    public static final int GAME_WIN = 1;
    public static final int GAME_LOST = -1;
    public static final int GAME_NORMAL = 0;
    public static final int GAME_ENDLESS = 2;
    public static final int GAME_ENDLESS_WON = 3;

    public Grid grid = null;
    public AnimGrid animGrid;
    public final int numSquaresX = 4;
    public final int numSquaresY = 4;
    /**
     * Init start cell count
     */
    private final int startTiles = 2;

    /**
     * Game state ,such as win,normal,lose and so on
     */
    public int gameState = 0;
    public boolean canUndo;

    /**
     * Current score
     */
    public int currentScore = 0;
    public int historyHighScore = 0;

    public int lastScore = 0;
    public int lastGameState = 0;

    private int bufferScore = 0;
    private int bufferGameState = 0;

    private Context mContext;

    private GameView mView;

    private SoundPool soundPool;
    /**
     * if audio enabled or not
     */
    public boolean isAudioEnabled = true;

    private int mergeId;
    private int moveId;

    public GameController(Context context, GameView view) {
        mContext = context;
        mView = view;
        endingMaxValue = (int) Math.pow(2, numSquaresX * numSquaresY);
        soundPool = new SoundPool.Builder().build();
        mergeId = soundPool.load(context, R.raw.merge, 1);
        moveId = soundPool.load(context, R.raw.move, 1);
    }

    public void newGame() {
        historyHighScore = getHistoryHighScore();
        if (currentScore > historyHighScore) {
            historyHighScore = currentScore;
            recordHighScore();
        }
        currentScore = 0;
        if (grid == null) {
            grid = new Grid(numSquaresX, numSquaresY);
        } else {
            prepareUndoState();
            saveUndoState();
            grid.clearGrid();
        }
        animGrid = new AnimGrid(numSquaresX, numSquaresY);
        gameState = GAME_NORMAL;
        addStartTiles();
        canUndo = false;
        mView.refreshLastTime = true;
        mView.syncTime();
        mView.invalidate();
    }

    private void addStartTiles() {
        for (int xx = 0; xx < startTiles; xx++) {
            this.addRandomTile();
        }
    }

    private void addRandomTile() {
        if (grid.isCellsAvailable()) {
            int value = Math.random() < 0.9 ? 2 : 4;
            Tile tile = new Tile(grid.randomAvailableCell(), value);
            spawnTile(tile);
        }
    }

    private void spawnTile(Tile tile) {
        grid.insertTile(tile);
        animGrid.startAnimation(tile.getX(), tile.getY(), SPAWN_ANIMATION, SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null);
    }

    private void recordHighScore() {
        SharedPreferenceUtil.put(mContext, SpConstant.HIGH_SCORE, historyHighScore);
        onRecordHighScore();
    }

    private int getHistoryHighScore() {
        return (int) SharedPreferenceUtil.get(mContext, SpConstant.HIGH_SCORE, 0);
    }

    private void prepareTiles() {
        for (Tile[] array : grid.field) {
            for (Tile tile : array) {
                if (grid.isCellOccupied(tile)) {
                    tile.setMergedFrom(null);
                }
            }
        }
    }

    private void moveTile(Tile tile, Cell cell) {
        grid.field[tile.getX()][tile.getY()] = null;
        grid.field[cell.getX()][cell.getY()] = tile;
        tile.updatePosition(cell);
    }

    private void saveUndoState() {
        grid.saveTiles();
        canUndo = true;
        lastScore = bufferScore;
        lastGameState = bufferGameState;
    }

    private void prepareUndoState() {
        grid.prepareSaveTiles();
        bufferScore = currentScore;
        bufferGameState = gameState;
    }

    /**
     * undo
     */
    public void undoGame() {
        if (canUndo) {
            canUndo = false;
            animGrid.cancelAnimations();
            grid.revertTiles();
            currentScore = lastScore;
            gameState = lastGameState;
            mView.refreshLastTime = true;
            mView.invalidate();
        }
    }


    /**
     * change mute status
     */
    public void mute() {
        isAudioEnabled = !isAudioEnabled;
        mView.invalidate();
    }

    public boolean gameWon() {
        return (gameState > 0 && gameState % 2 != 0);
    }

    public boolean gameLost() {
        return (gameState == GAME_LOST);
    }

    public boolean isActive() {
        return !(gameWon() || gameLost());
    }

    public void move(int direction) {
        animGrid.cancelAnimations();
        // 0: up, 1: right, 2: down, 3: left
        if (!isActive()) {
            return;
        }
        prepareUndoState();
        Cell vector = getVector(direction);
        List<Integer> traversalsX = buildTraversalsX(vector);
        List<Integer> traversalsY = buildTraversalsY(vector);
        boolean moved = false;

        prepareTiles();

        for (int xx : traversalsX) {
            for (int yy : traversalsY) {
                Cell cell = new Cell(xx, yy);
                Tile tile = grid.getCellContent(cell);

                if (tile != null) {
                    Cell[] positions = findFarthestPosition(cell, vector);
                    Tile next = grid.getCellContent(positions[1]);

                    if (next != null && next.getValue() == tile.getValue() && next.getMergedFrom() == null) {
                        Tile merged = new Tile(positions[1], tile.getValue() * 2);
                        Tile[] temp = {tile, next};
                        merged.setMergedFrom(temp);

                        grid.insertTile(merged);
                        grid.removeTile(tile);

                        // Converge the two tiles' positions
                        tile.updatePosition(positions[1]);
                        if (isAudioEnabled) {
                            soundPool.play(mergeId, 1, 1, 1, 0, 1);
                        }
                        int[] extras = {xx, yy};
                        //Direction: 0 = MOVING MERGED
                        animGrid.startAnimation(merged.getX(), merged.getY(), MOVE_ANIMATION, MOVE_ANIMATION_TIME, 0, extras);
                        animGrid.startAnimation(merged.getX(), merged.getY(), MERGE_ANIMATION, SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null);

                        // Update the currentScore
                        currentScore = currentScore + merged.getValue();
                        historyHighScore = Math.max(currentScore, historyHighScore);

                        // The mighty 2048 tile
                        if (merged.getValue() >= winValue() && !gameWon()) {
                            // Set win state
                            gameState = gameState + GAME_WIN;
                            endGame();
                        }


                    } else {
                        if (tile.getX() != positions[0].getX() || tile.getY() != positions[0].getY()) {
                            if (isAudioEnabled) {
                                soundPool.play(moveId, 1, 1, 1, 0, 1);
                            }
                        }
                        moveTile(tile, positions[0]);
                        int[] extras = {xx, yy, 0};
                        //Direction: 1 = MOVING NO MERGE
                        animGrid.startAnimation(positions[0].getX(), positions[0].getY(), MOVE_ANIMATION, MOVE_ANIMATION_TIME, 0, extras);
                    }

                    if (!positionsEqual(cell, tile)) {
                        moved = true;
                    }
                }
            }
        }

        if (moved) {
            saveUndoState();
            addRandomTile();
            checkLose();
        }
        mView.syncTime();
        mView.invalidate();
    }

    private void checkLose() {
        if (!movesAvailable() && !gameWon()) {
            gameState = GAME_LOST;
            endGame();
        }
    }


    private Cell getVector(int direction) {
        Cell[] map = {
                // up
                new Cell(0, -1),
                // right
                new Cell(1, 0),
                // down
                new Cell(0, 1),
                // left
                new Cell(-1, 0)
        };
        return map[direction];
    }

    private List<Integer> buildTraversalsX(Cell vector) {
        List<Integer> traversals = new ArrayList<>();

        for (int xx = 0; xx < numSquaresX; xx++) {
            traversals.add(xx);
        }
        if (vector.getX() == 1) {
            Collections.reverse(traversals);
        }

        return traversals;
    }

    private List<Integer> buildTraversalsY(Cell vector) {
        List<Integer> traversals = new ArrayList<>();

        for (int xx = 0; xx < numSquaresY; xx++) {
            traversals.add(xx);
        }
        if (vector.getY() == 1) {
            Collections.reverse(traversals);
        }

        return traversals;
    }

    private Cell[] findFarthestPosition(Cell cell, Cell vector) {
        Cell previous;
        Cell nextCell = new Cell(cell.getX(), cell.getY());
        do {
            previous = nextCell;
            nextCell = new Cell(previous.getX() + vector.getX(), previous.getY() + vector.getY());
        } while (grid.isCellWithinBounds(nextCell) && grid.isCellAvailable(nextCell));

        return new Cell[]{previous, nextCell};
    }

    private boolean movesAvailable() {
        return grid.isCellsAvailable() || tileMatchesAvailable();
    }

    private boolean tileMatchesAvailable() {
        Tile tile;

        for (int xx = 0; xx < numSquaresX; xx++) {
            for (int yy = 0; yy < numSquaresY; yy++) {
                tile = grid.getCellContent(new Cell(xx, yy));

                if (tile != null) {
                    for (int direction = 0; direction < 4; direction++) {
                        Cell vector = getVector(direction);
                        Cell cell = new Cell(xx + vector.getX(), yy + vector.getY());

                        Tile other = grid.getCellContent(cell);

                        if (other != null && other.getValue() == tile.getValue()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean positionsEqual(Cell first, Cell second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }

    private int winValue() {
        if (!canContinue()) {
            return endingMaxValue;
        } else {
            return STARTING_MAX_VALUE;
        }
    }

    public void setEndlessMode() {
        gameState = GAME_ENDLESS;
        mView.invalidate();
        mView.refreshLastTime = true;
    }

    public boolean canContinue() {
        return !(gameState == GAME_ENDLESS || gameState == GAME_ENDLESS_WON);
    }

    private void endGame() {
        animGrid.startAnimation(-1, -1, FADE_GLOBAL_ANIMATION, NOTIFICATION_ANIMATION_TIME, NOTIFICATION_DELAY_TIME, null);
        if (currentScore >= historyHighScore) {
            historyHighScore = currentScore;
            recordHighScore();
        }
        onEndOfGame();

    }

    public void onAudioClick() {
        if (onFunctionClickListener != null) {
            onFunctionClickListener.onMuteButtonClick();
        }
    }

    public void onMenuClick() {
        if (onFunctionClickListener != null) {
            onFunctionClickListener.onMenuButtonClick();
        }
    }

    public void onUndoClick() {
        if (onFunctionClickListener != null) {
            onFunctionClickListener.onUndoButtonClick();
        }
    }

    public void onNewGameClick() {
        if (onFunctionClickListener != null) {
            onFunctionClickListener.onNewGameButtonClick();
        }
    }

    public void onEndOfGame() {
        if (onFunctionClickListener != null) {
            onFunctionClickListener.onEndOfGame();
        }
    }


    public void onRecordHighScore() {
        if (onFunctionClickListener != null) {
            onFunctionClickListener.onRecordHighScore();
        }
    }

    public void setGameMode(int gameMode) {
        mView.setGameMode(gameMode);
    }

    public int getGameMode() {
        return mView.getGameMode();
    }

    public void setOnFunctionClickListener(OnFunctionClickListener onFunctionClickListener) {
        this.onFunctionClickListener = onFunctionClickListener;
    }
}
