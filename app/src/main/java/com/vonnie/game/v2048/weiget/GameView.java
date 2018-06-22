package com.vonnie.game.v2048.weiget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.cell.AnimCell;
import com.vonnie.game.v2048.cell.Tile;
import com.vonnie.game.v2048.logic.MainGame;
import com.vonnie.game.v2048.logic.OperationListener;

import java.util.ArrayList;

/**
 * @author LongpingZou
 * @date 2018/6/19
 */
public class GameView extends View {
    /**
     * paint
     */
    private Paint paint = new Paint();
    /**
     * game object
     */
    public MainGame game;
    public final int numCellTypes = 18;
    public boolean continueButtonEnabled = false;

    /**
     * width and height of cell
     */
    private int cellSize = 0;
    private float textSize = 0;
    /**
     * text size of cell
     */
    private float cellTextSize = 0;

    /**
     * width of grid spacing
     */
    private int gridWidth = 0;
    /**
     * black text color
     */
    private int blackTextColor;
    /**
     * white text color
     */
    private int whiteTextColor;
    /**
     * brown text color
     */
    private int brownTextColor;
    /**
     * The origin of the X axis of a grid table
     */
    public int tableOriginalX;

    /**
     * The origin of the Y axis of a grid table
     */
    public int tableOriginalY;
    /**
     * The end of the X axis of a grid table
     */
    public int tableEndingX;
    /**
     * The end of the Y axis of a grid table
     */
    public int tableEndingY;
    /**
     * The origin of the y axis of info panel
     */
    private int infoPanelOriginalY;

    /**
     * The origin of the x axis of info panel
     */
    private int infoPanelOriginalX;
    /**
     * The origin of the Y axis of title text
     */
    private int titleTextOriginY;
    private int bodyStartYAll;
    private int eYAll;
    private int textPaddingSize;
    private int iconPaddingSize;

    private Drawable backgroundRectangle;
    private Drawable modeRectangleBg;
    private BitmapDrawable[] bitmapCell = new BitmapDrawable[numCellTypes];

    private Drawable lightUpRectangle;
    private Drawable fadeRectangle;
    private Bitmap background = null;
    private BitmapDrawable loseGameOverlay;
    private BitmapDrawable winGameContinueOverlay;
    private BitmapDrawable winGameFinalOverlay;


    private int titleWidthHighScore;
    private int titleWidthScore;

    public int sYIcons;
    /**
     * the start x of new game button
     */
    public int sXNewGame;

    /**
     * the start x of undo button
     */
    public int sXUndo;
    /**
     * refresh button and undo button size
     */
    public int functionButtonSize;


    /**
     * mode tips button size
     */
    public int modeButtonSize;
    long lastFPSTime = System.nanoTime();
    long currentTime = System.nanoTime();

    /**
     * text size of title,such as 'currentScore & high currentScore'
     */
    private float titleTextSize;
    private float bodyTextSize;

    /**
     * text size of header 2048
     */
    private float headerTextSize;
    /**
     * text size of introduce
     */
    private float instructionsTextSize;
    private float gameOverTextSize;

    public boolean refreshLastTime = true;

    /**
     * Internal Constants
     */
    public static final int BASE_ANIMATION_TIME = 100000000;
    static final float MERGING_ACCELERATION = (float) -0.5;
    static final float INITIAL_VELOCITY = (1 - MERGING_ACCELERATION) / 4;

    @Override
    public void onDraw(Canvas canvas) {
        //Reset the transparency of the screen

        canvas.drawBitmap(background, 0, 0, paint);

        drawScoreText(canvas);

        if (!game.isActive() && !game.animGrid.isAnimationActive()) {
            drawNewGameButton(canvas, true);
        }

        drawCells(canvas);

        if (!game.isActive()) {
            drawEndGameState(canvas);
        }

        if (!game.canContinue()) {
            drawEndlessText(canvas);
        }

        //Refresh the screen if there is still an animation running
        if (game.animGrid.isAnimationActive()) {
            invalidate(tableOriginalX, tableOriginalY, tableEndingX, tableEndingY);
            tick();
            //Refresh one last time on game end.
        } else if (!game.isActive() && refreshLastTime) {
            invalidate();
            refreshLastTime = false;
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        Log.i("ABC", "onSizeChanged");
        getLayout(width, height);
        createBitmapCells();
        createBackgroundBitmap(width, height);
        createOverlays();
    }

    private void drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY) {
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }

    private void drawCellText(Canvas canvas, int value, int sX, int sY) {
        int textShiftY = centerText();
        if (value >= 8) {
            paint.setColor(whiteTextColor);
        } else {
            paint.setColor(blackTextColor);
        }
//        String str = "老罗";
        float tempTextSize = cellTextSize * cellSize * 0.9f / Math.max(cellSize * 0.9f, paint.measureText(String.valueOf(value)));
        paint.setTextSize(tempTextSize);
        canvas.drawText(String.valueOf(value), sX + cellSize / 2, sY + cellSize / 2 - textShiftY, paint);
    }

    private void drawScoreText(Canvas canvas) {
        //Drawing the currentScore text: Ver 2
        paint.setTextSize(bodyTextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        int bodyWidthHighScore = (int) (paint.measureText("" + game.historyHighScore));
        int bodyWidthScore = (int) (paint.measureText("" + game.currentScore));

        int textWidthHighScore = Math.max(titleWidthHighScore, bodyWidthHighScore) + textPaddingSize * 2;
        int textWidthScore = Math.max(titleWidthScore, bodyWidthScore) + textPaddingSize * 2;

        int textMiddleHighScore = textWidthHighScore / 2;
        int textMiddleScore = textWidthScore / 2;

        int eXHighScore = tableEndingX;
        int sXHighScore = eXHighScore - textWidthHighScore;

        int eXScore = sXHighScore - textPaddingSize;
        int sXScore = eXScore - textWidthScore;

        //Outputting high-scores box
        backgroundRectangle.setBounds(sXHighScore, infoPanelOriginalY, eXHighScore, eYAll);
        backgroundRectangle.draw(canvas);
        paint.setTextSize(titleTextSize);
        paint.setColor(brownTextColor);
        canvas.drawText(getResources().getString(R.string.high_score), sXHighScore + textMiddleHighScore, titleTextOriginY, paint);
        paint.setTextSize(bodyTextSize);
        paint.setColor(whiteTextColor);
        canvas.drawText(String.valueOf(game.historyHighScore), sXHighScore + textMiddleHighScore, bodyStartYAll, paint);


        //Outputting scores box
        backgroundRectangle.setBounds(sXScore, infoPanelOriginalY, eXScore, eYAll);
        backgroundRectangle.draw(canvas);
        paint.setTextSize(titleTextSize);
        paint.setColor(brownTextColor);
        canvas.drawText(getResources().getString(R.string.score), sXScore + textMiddleScore, titleTextOriginY, paint);
        paint.setTextSize(bodyTextSize);
        paint.setColor(whiteTextColor);
        canvas.drawText(String.valueOf(game.currentScore), sXScore + textMiddleScore, bodyStartYAll, paint);
    }

    private void drawNewGameButton(Canvas canvas, boolean lightUp) {

        if (lightUp) {
            drawDrawable(canvas, lightUpRectangle, sXNewGame, sYIcons, sXNewGame + functionButtonSize, sYIcons + functionButtonSize);
        } else {
            drawDrawable(canvas, backgroundRectangle, sXNewGame, sYIcons, sXNewGame + functionButtonSize, sYIcons + functionButtonSize);
        }

        Drawable refresh = getResources().getDrawable(R.drawable.ic_action_refresh);
        drawDrawable(canvas, refresh, sXNewGame + iconPaddingSize, sYIcons + iconPaddingSize, sXNewGame + functionButtonSize - iconPaddingSize, sYIcons + functionButtonSize - iconPaddingSize);
    }

    private void drawUndoButton(Canvas canvas) {
        drawDrawable(canvas, backgroundRectangle, sXUndo, sYIcons, sXUndo + functionButtonSize, sYIcons + functionButtonSize);
        drawDrawable(canvas, getResources().getDrawable(R.drawable.ic_action_undo), sXUndo + iconPaddingSize, sYIcons + iconPaddingSize, sXUndo + functionButtonSize - iconPaddingSize, sYIcons + functionButtonSize - iconPaddingSize);
    }

    private void drawHeader(Canvas canvas) {

        drawDrawable(canvas, modeRectangleBg, infoPanelOriginalX, infoPanelOriginalY, infoPanelOriginalX + (cellSize - gridWidth) * 2, infoPanelOriginalY + (cellSize - gridWidth) * 2);
        paint.setTextSize(headerTextSize);
        paint.setColor(whiteTextColor);
        paint.setTextAlign(Paint.Align.LEFT);
        String str = getResources().getString(R.string.header);
        float width = paint.measureText(str);
        float startX = infoPanelOriginalX + (cellSize - gridWidth) * 2 / 2 - width / 2;
        float endY = infoPanelOriginalY + (cellSize - gridWidth) / 3 - centerText() * 2 + textPaddingSize;
        canvas.drawText(str, startX, endY, paint);
    }


    private void drawEndlessText(Canvas canvas) {

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(textSize);
        paint.setColor(blackTextColor);
        String str = getResources().getString(R.string.endless);
        float width = paint.measureText(str);
        float modeTextSize = 0.9f * (cellSize - gridWidth) * 2 / width * textSize;
        paint.setTextSize(modeTextSize);
        width = paint.measureText(str);
        float startX = infoPanelOriginalX + (cellSize - gridWidth) * 2 / 2 - width / 2;
        float endY = infoPanelOriginalY + (cellSize - gridWidth) * 4 / 3 - centerText() * 2;
        canvas.drawText(str, startX, endY, paint);
    }

//    private void drawInstructions(Canvas canvas) {
//
//        //Drawing the instructions
//        paint.setTextSize(instructionsTextSize);
//        paint.setTextAlign(Paint.Align.LEFT);
//        int textShiftY = centerText() * 2;
//        canvas.drawText(getResources().getString(R.string.instructions),
//                tableOriginalX, tableEndingY - textShiftY + textPaddingSize, paint);
//    }

    private void drawBackground(Canvas canvas) {
        drawDrawable(canvas, backgroundRectangle, tableOriginalX, tableOriginalY, tableEndingX, tableEndingY);
    }

    /**
     * Renders the set of 16 background squares.
     */

    private void drawBackgroundGrid(Canvas canvas) {
        Resources resources = getResources();
        Drawable backgroundCell = resources.getDrawable(R.drawable.cell_rectangle);
        // Outputting the game grid
        for (int xx = 0; xx < game.numSquaresX; xx++) {
            for (int yy = 0; yy < game.numSquaresY; yy++) {
                int sX = tableOriginalX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = tableOriginalY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                drawDrawable(canvas, backgroundCell, sX, sY, eX, eY);
            }
        }
    }

    private void drawCells(Canvas canvas) {
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        // Outputting the individual cells
        for (int xx = 0; xx < game.numSquaresX; xx++) {
            for (int yy = 0; yy < game.numSquaresY; yy++) {
                int sX = tableOriginalX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = tableOriginalY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                Tile currentTile = game.grid.getCellContent(xx, yy);
                if (currentTile != null) {
                    //Get and represent the value of the tile
                    int value = currentTile.getValue();
                    int index = log2(value);

                    //Check for any active animations
                    ArrayList<AnimCell> aArray = game.animGrid.getAnimCell(xx, yy);
                    boolean animated = false;
                    for (int i = aArray.size() - 1; i >= 0; i--) {
                        AnimCell aCell = aArray.get(i);
                        //If this animation is not active, skip it
                        if (aCell.getAnimationType() == MainGame.SPAWN_ANIMATION) {
                            animated = true;
                        }
                        if (!aCell.isActive()) {
                            continue;
                        }
                        // Spawning animation
                        if (aCell.getAnimationType() == MainGame.SPAWN_ANIMATION) {
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (percentDone);
                            paint.setTextSize(textSize * textScaleSize);

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                            // Merging Animation
                        } else if (aCell.getAnimationType() == MainGame.MERGE_ANIMATION) {
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (1 + INITIAL_VELOCITY * percentDone + MERGING_ACCELERATION * percentDone * percentDone / 2);
                            paint.setTextSize(textSize * textScaleSize);

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                            // Moving animation
                        } else if (aCell.getAnimationType() == MainGame.MOVE_ANIMATION) {
                            double percentDone = aCell.getPercentageDone();
                            int tempIndex = index;
                            if (aArray.size() >= 2) {
                                tempIndex = tempIndex - 1;
                            }
                            int previousX = aCell.extras[0];
                            int previousY = aCell.extras[1];
                            int currentX = currentTile.getX();
                            int currentY = currentTile.getY();
                            int dX = (int) ((currentX - previousX) * (cellSize + gridWidth) * (percentDone - 1) * 1.0);
                            int dY = (int) ((currentY - previousY) * (cellSize + gridWidth) * (percentDone - 1) * 1.0);
                            bitmapCell[tempIndex].setBounds(sX + dX, sY + dY, eX + dX, eY + dY);
                            bitmapCell[tempIndex].draw(canvas);
                        }
                        animated = true;
                    }

                    //No active animations? Just draw the cell
                    if (!animated) {
                        bitmapCell[index].setBounds(sX, sY, eX, eY);
                        bitmapCell[index].draw(canvas);
                    }
                }
            }
        }
    }

    private void drawEndGameState(Canvas canvas) {
        double alphaChange = 1;
        continueButtonEnabled = false;
        for (AnimCell animation : game.animGrid.globalAnimation) {
            if (animation.getAnimationType() == MainGame.FADE_GLOBAL_ANIMATION) {
                alphaChange = animation.getPercentageDone();
            }
        }
        BitmapDrawable displayOverlay = null;
        if (game.gameWon()) {
            if (game.canContinue()) {
                continueButtonEnabled = true;
                displayOverlay = winGameContinueOverlay;
            } else {
                displayOverlay = winGameFinalOverlay;
            }
        } else if (game.gameLost()) {
            displayOverlay = loseGameOverlay;
        }

        if (displayOverlay != null) {
            displayOverlay.setBounds(tableOriginalX, tableOriginalY, tableEndingX, tableEndingY);
            displayOverlay.setAlpha((int) (255 * alphaChange));
            displayOverlay.draw(canvas);
        }
    }


    private void createEndGameStates(Canvas canvas, boolean win, boolean showButton) {
        int width = tableEndingX - tableOriginalX;
        int length = tableEndingY - tableOriginalY;
        int middleX = width / 2;
        int middleY = length / 2;
        if (win) {
            lightUpRectangle.setAlpha(127);
            drawDrawable(canvas, lightUpRectangle, 0, 0, width, length);
            lightUpRectangle.setAlpha(255);
            paint.setColor(whiteTextColor);
            paint.setAlpha(255);
            paint.setTextSize(gameOverTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            int textBottom = middleY - centerText();
            canvas.drawText(getResources().getString(R.string.you_win), middleX, textBottom, paint);
            paint.setTextSize(bodyTextSize);
            String text = showButton ? getResources().getString(R.string.go_on) : getResources().getString(R.string.for_now);
            canvas.drawText(text, middleX, textBottom + textPaddingSize * 2 - centerText() * 2, paint);
        } else {
            fadeRectangle.setAlpha(127);
            drawDrawable(canvas, fadeRectangle, 0, 0, width, length);
            fadeRectangle.setAlpha(255);
            paint.setColor(blackTextColor);
            paint.setAlpha(255);
            paint.setTextSize(gameOverTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(getResources().getString(R.string.game_over), middleX, middleY - centerText(), paint);
        }
    }

    private void createBackgroundBitmap(int width, int height) {
        background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        drawHeader(canvas);
        drawNewGameButton(canvas, false);
        drawUndoButton(canvas);
        drawBackground(canvas);
        drawBackgroundGrid(canvas);
//        drawInstructions(canvas);

    }

    private void createBitmapCells() {

        Resources resources = getResources();
        int[] cellRectangleIds = getCellRectangleIds();
        paint.setTextAlign(Paint.Align.CENTER);
        for (int xx = 1; xx < bitmapCell.length; xx++) {
            int value = (int) Math.pow(2, xx);
            paint.setTextSize(cellTextSize);
            Bitmap bitmap = Bitmap.createBitmap(cellSize, cellSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawDrawable(canvas, resources.getDrawable(cellRectangleIds[xx]), 0, 0, cellSize, cellSize);
            drawCellText(canvas, value, 0, 0);
            bitmapCell[xx] = new BitmapDrawable(resources, bitmap);
        }
    }

    private int[] getCellRectangleIds() {
        int[] cellRectangleIds = new int[numCellTypes];
        cellRectangleIds[0] = R.drawable.cell_rectangle;
        cellRectangleIds[1] = R.drawable.cell_rectangle_2;
        cellRectangleIds[2] = R.drawable.cell_rectangle_4;
        cellRectangleIds[3] = R.drawable.cell_rectangle_8;
        cellRectangleIds[4] = R.drawable.cell_rectangle_16;
        cellRectangleIds[5] = R.drawable.cell_rectangle_32;
        cellRectangleIds[6] = R.drawable.cell_rectangle_64;
        cellRectangleIds[7] = R.drawable.cell_rectangle_128;
        cellRectangleIds[8] = R.drawable.cell_rectangle_256;
        cellRectangleIds[9] = R.drawable.cell_rectangle_512;
        cellRectangleIds[10] = R.drawable.cell_rectangle_1024;
        cellRectangleIds[11] = R.drawable.cell_rectangle_2048;
        for (int xx = 12; xx < cellRectangleIds.length; xx++) {
            cellRectangleIds[xx] = R.drawable.cell_rectangle_4096;
        }
        return cellRectangleIds;
    }

    private void createOverlays() {
        Resources resources = getResources();
        Bitmap bitmap = Bitmap.createBitmap(tableEndingX - tableOriginalX, tableEndingY - tableOriginalY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        createEndGameStates(canvas, true, true);
        winGameContinueOverlay = new BitmapDrawable(resources, bitmap);
        bitmap = Bitmap.createBitmap(tableEndingX - tableOriginalX, tableEndingY - tableOriginalY, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        createEndGameStates(canvas, true, false);
        winGameFinalOverlay = new BitmapDrawable(resources, bitmap);
        bitmap = Bitmap.createBitmap(tableEndingX - tableOriginalX, tableEndingY - tableOriginalY, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        createEndGameStates(canvas, false, false);
        loseGameOverlay = new BitmapDrawable(resources, bitmap);
    }

    private void tick() {
        currentTime = System.nanoTime();
        game.animGrid.tickAll(currentTime - lastFPSTime);
        lastFPSTime = currentTime;
    }

    public void resyncTime() {
        lastFPSTime = System.nanoTime();
    }

    private static int log2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    private void getLayout(int width, int height) {
        //considering rotating screen ,numSquresY need to add 3 points
        cellSize = Math.min(width / (game.numSquaresX + 1), height / (game.numSquaresY + 3));
        Log.i("ABC", "width / (game.numSquaresX):" + width / (game.numSquaresX) + " - " + height / (game.numSquaresY));
        Log.i("ABC", "cellSize:" + cellSize + " width:" + width + " height:" + height);
        gridWidth = cellSize / (game.numSquaresX + 3);
        Log.i("ABC", "gridWidth:" + gridWidth);
        int screenMiddleX = width / 2;
        int screenMiddleY = height / 2;
        int boardMiddleX = screenMiddleX;
        int boardMiddleY = screenMiddleY + cellSize / 2;
        Log.i("ABC", "boardMiddleX:" + boardMiddleX + "-boardMiddleY:" + boardMiddleY);
        functionButtonSize = Math.min(width / 4, height / 4) / 2;
//        functionButtonSize = cellSize / 2;
        modeButtonSize = Math.min(width / 4, height / 4) * 2;
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(cellSize);
        textSize = cellSize * cellSize / Math.max(cellSize, paint.measureText("000000"));
        Log.i("ABC", "2 * cellSize:" + (2 * cellSize) + " - " + "paint.measureText(\"000000\"):" + paint.measureText("000000"));
        cellTextSize = textSize;
        titleTextSize = textSize / 3;
        bodyTextSize = (int) (textSize / 1.5);
        instructionsTextSize = (int) (textSize / 1.5);
        headerTextSize = textSize * 2;
        gameOverTextSize = textSize * 2;
        textPaddingSize = (int) (textSize / 3);
        iconPaddingSize = (int) (textSize / 5);

        double halfNumSquaresX = game.numSquaresX / 2d;
        double halfNumSquaresY = game.numSquaresY / 2d;

        tableOriginalX = (int) (boardMiddleX - (cellSize + gridWidth) * halfNumSquaresX - gridWidth / 2);
        tableEndingX = (int) (boardMiddleX + (cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2);
        tableOriginalY = (int) (boardMiddleY - (cellSize + gridWidth) * halfNumSquaresY - gridWidth / 2);
        tableEndingY = (int) (boardMiddleY + (cellSize + gridWidth) * halfNumSquaresY + gridWidth / 2);

        paint.setTextSize(titleTextSize);

        int textShiftYAll = centerText();
        //calc info panel origin position
        infoPanelOriginalX = tableOriginalX;
        infoPanelOriginalY = tableOriginalY - cellSize * 2;
//        Log.i("ABC", "tableOriginalY:" + tableOriginalY + " sYALL:" + infoPanelOriginalY);
////        infoPanelOriginalY=0;

        titleTextOriginY = (int) (infoPanelOriginalY + textPaddingSize + titleTextSize / 2 - textShiftYAll);
        bodyStartYAll = (int) (titleTextOriginY + textPaddingSize + titleTextSize / 2 + bodyTextSize / 2);
        Log.i("ABC", "textShiftYAll:" + textShiftYAll + " -titleTextOriginY:" + titleTextOriginY);
        titleWidthHighScore = (int) (paint.measureText(getResources().getString(R.string.high_score)));
        titleWidthScore = (int) (paint.measureText(getResources().getString(R.string.score)));
        paint.setTextSize(bodyTextSize);
//        textShiftYAll = centerText();
        eYAll = (int) (bodyStartYAll + textShiftYAll + bodyTextSize / 2 + textPaddingSize);

        sYIcons = (tableOriginalY + eYAll) / 2 - functionButtonSize / 2;
        sXNewGame = (tableEndingX - functionButtonSize);
        sXUndo = sXNewGame - functionButtonSize * 3 / 2 - iconPaddingSize;
        resyncTime();

    }

    private int centerText() {
        return (int) ((paint.descent() + paint.ascent()) / 2);
    }

    public GameView(Context context) {
        super(context);

        Resources resources = context.getResources();
        //Loading resources
        game = new MainGame(context, this);
        try {

            //Getting assets
            modeRectangleBg = resources.getDrawable(R.drawable.mode_background_rectangle);
            backgroundRectangle = resources.getDrawable(R.drawable.background_rectangle);
            lightUpRectangle = resources.getDrawable(R.drawable.light_up_rectangle);
            fadeRectangle = resources.getDrawable(R.drawable.fade_rectangle);
            whiteTextColor = resources.getColor(R.color.text_white);
            blackTextColor = resources.getColor(R.color.text_black);
            brownTextColor = resources.getColor(R.color.text_brown);
            this.setBackgroundColor(resources.getColor(R.color.background));
            Typeface font = Typeface.createFromAsset(resources.getAssets(), "ClearSans-Bold.ttf");
            paint.setTypeface(font);
            paint.setAntiAlias(true);
        } catch (Exception e) {
            System.out.println("Error getting assets?");
        }
        setOnTouchListener(new OperationListener(this));
        game.newGame();
    }

}
