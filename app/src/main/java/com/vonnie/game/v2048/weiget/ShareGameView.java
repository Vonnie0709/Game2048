package com.vonnie.game.v2048.weiget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vonnie.game.v2048.R;
import com.vonnie.game.v2048.cell.AnimCell;
import com.vonnie.game.v2048.cell.Tile;
import com.vonnie.game.v2048.grid.AnimGrid;
import com.vonnie.game.v2048.grid.Grid;
import com.vonnie.game.v2048.logic.GameController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author LongpingZou
 * @date 2018/6/19
 */
public class ShareGameView extends View {
    /**
     * define cell max number
     */
    public final int numCellTypes = 18;
    private BitmapDrawable[] bitmapCell = new BitmapDrawable[numCellTypes];

    private Bitmap background = null;
    long lastFPSTime = System.nanoTime();
    /**
     * paint
     */
    private Paint paint = new Paint();

    /**
     * width and height of cell
     */
    private int cellSize = 0;
    /**
     * according to cell size ,get a text size standard
     */
    private float baseTextSize = 0;

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
     * The origin of the y axis of all panel
     */
    private int topLine;

    /**
     * The origin of the Y axis of title text
     */
    private int scoreTitleOriginY;
    /**
     * the width and height of mode panel
     */
    private int panelWidth;
    /**
     * the origin x of the axis X of menu
     */
    public int menuStartX;
    /**
     * the end x of the axis X of menu
     */
    public int menuEndX;
    /**
     * the origin y of the axis X of menu
     */
    public int menuTop;
    /**
     * the end y of the axis X of menu
     */
    public int menuBottom;
    /**
     * default text padding
     */
    private int textPaddingSize;

    /**
     * padding between icon and background of function ,such as new game and undo
     */
    private int iconPaddingSize;

    /**
     * background drawable of score
     */
    private Drawable scorePanelBg;
    /**
     * background drawable of mode panel
     */
    private Drawable modePanelBg;

    /**
     * background drawable of menu panel
     */
    private Drawable menuPanelBg;

    /**
     * background drawable of function panel when its enabled
     */
    private Drawable enableFunctionBg;

    /**
     * background drawable of function panel when its disabled
     */
    private Drawable disableFunctionBg;


    /**
     * width of function button
     */
    public int functionButtonWidth;

    /**
     * the origin x of the axis X of function
     */
    public int functionOriginX;

    /**
     * space between function buttons
     */
    public int functionButtonSpace;

    /**
     * the origin of the Y axis of function button
     */
    public int functionButtonTop;

    /**
     * the start x of new game button
     */
    public int newGameFunctionStartX;
    /**
     * the start x of undo game button
     */
    public int undoFunctionStartX;

    /**
     * the start x of audio game button
     */
    public int audioFunctionStartX;

    /**
     * text size of title,such as 'currentScore & high currentScore'
     */
    private float scoreTitleSize;

    /**
     * Internal Constants
     */
    public static final int BASE_ANIMATION_TIME = 100000000;
    private static final float MERGING_ACCELERATION = (float) -0.5;
    private static final float INITIAL_VELOCITY = (1 - MERGING_ACCELERATION) / 4;

    /**
     * mode of game
     */
    private int gameMode;
    public Grid grid = null;
    public AnimGrid animGrid = null;
    public int numSquaresX;
    public int numSquaresY;

    @Override
    public void onDraw(Canvas canvas) {
        //Reset the transparency of the screen
        canvas.drawBitmap(background, 0, 0, paint);
        drawCells(canvas);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        getLayout(width * 4 / 5, height * 4 / 5);
        createBackgroundBitmap(width * 4 / 5, height * 4 / 5);
        createBitmapCells();
    }

    /**
     * draw drawable
     *
     * @param canvas
     * @param draw
     * @param startingX
     * @param startingY
     * @param endingX
     * @param endingY
     */
    private void drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY) {
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }


    /**
     * draw cell text
     *
     * @param canvas
     * @param value
     * @param startX
     * @param startY
     */
    private void drawCellText(Canvas canvas, int value, int startX, int startY) {
        int textShiftY = centerText();
        if (value >= 8) {
            paint.setColor(whiteTextColor);
        } else {
            paint.setColor(blackTextColor);
        }

        String str;
        if (gameMode == 0) {
            str = String.valueOf(value);
        } else {
            str = getModeText(value);
        }
        //To maintain font size, get a fixed font size
        float fitTextSize = baseTextSize * cellSize * 0.9f / Math.max(cellSize * 0.9f, paint.measureText(str));
        paint.setTextSize(fitTextSize);

        canvas.drawText(str, startX + cellSize / 2, startY + cellSize / 2 - textShiftY, paint);

    }

    private String getModeText(int value) {
        int i = (int) (Math.log(value) / Math.log(2));
        if (i < modeArray.size()) {
            return modeArray.get(i - 1);
        } else {
            return String.valueOf(value);
        }
    }


    /**
     * draw background
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        Drawable backgroundRectangle = ContextCompat.getDrawable(context, R.drawable.background_rectangle);
        assert backgroundRectangle != null;
        drawDrawable(canvas, backgroundRectangle, tableOriginalX, tableOriginalY, tableEndingX, tableEndingY);
    }

    /**
     * Renders the set of 16 background squares.
     */

    private void drawBackgroundGrid(Canvas canvas) {
        Drawable backgroundCell = ContextCompat.getDrawable(context, R.drawable.cell_rectangle);
        // Outputting the game grid
        for (int xx = 0; xx < numSquaresX; xx++) {
            for (int yy = 0; yy < numSquaresY; yy++) {
                int sX = tableOriginalX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = tableOriginalY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                assert backgroundCell != null;
                drawDrawable(canvas, backgroundCell, sX, sY, eX, eY);
            }
        }
    }

    /**
     * draw each cell
     *
     * @param canvas
     */
    private void drawCells(Canvas canvas) {
        paint.setTextSize(baseTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        // Outputting the individual cells
        for (int xx = 0; xx < numSquaresX; xx++) {
            for (int yy = 0; yy < numSquaresY; yy++) {
                int sX = tableOriginalX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = tableOriginalY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                Tile currentTile = grid.getCellContent(xx, yy);
                if (currentTile != null) {
                    //Get and represent the value of the tile
                    int value = currentTile.getValue();
                    int index = log2(value);

                    //Check for any active animations
                    ArrayList<AnimCell> aArray = animGrid.getAnimCell(xx, yy);
                    boolean animated = false;
                    for (int i = aArray.size() - 1; i >= 0; i--) {
                        AnimCell aCell = aArray.get(i);

                        if (!aCell.isActive()) {
                            continue;
                        }
                        // Spawning animation
                        if (aCell.getAnimationType() == GameController.SPAWN_ANIMATION) {
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (percentDone);
                            paint.setTextSize(baseTextSize * textScaleSize);

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                            // Merging Animation
                        } else if (aCell.getAnimationType() == GameController.MERGE_ANIMATION) {
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (1 + INITIAL_VELOCITY * percentDone + MERGING_ACCELERATION * percentDone * percentDone / 2);
                            paint.setTextSize(baseTextSize * textScaleSize);

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                            // Moving animation
                        } else if (aCell.getAnimationType() == GameController.MOVE_ANIMATION) {
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


    /**
     * create canvas
     *
     * @param width
     * @param height
     */
    private void createBackgroundBitmap(int width, int height) {
        background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        drawBackground(canvas);
        drawBackgroundGrid(canvas);

    }

    /**
     * create  cells bg
     */
    private void createBitmapCells() {
        Log.i("ABC", "cell:" + cellSize);
        Resources resources = getResources();
        int[] cellRectangleIds = getCellRectangleIds();
        paint.setTextAlign(Paint.Align.CENTER);
        for (int xx = 1; xx < bitmapCell.length; xx++) {
            if (bitmapCell[xx] != null && bitmapCell[xx].getBitmap() != null) {
                Bitmap b = bitmapCell[xx].getBitmap();
                b.recycle();
            }
            int value = (int) Math.pow(2, xx);
            paint.setTextSize(baseTextSize);
            Bitmap bitmap = Bitmap.createBitmap(cellSize, cellSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawDrawable(canvas, Objects.requireNonNull(ContextCompat.getDrawable(context, cellRectangleIds[xx])), 0, 0, cellSize, cellSize);
            drawCellText(canvas, value, 0, 0);
            bitmapCell[xx] = new BitmapDrawable(resources, bitmap);
        }
    }

    /**
     * set cell rectangle assets
     *
     * @return
     */
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


    public void syncTime() {
        lastFPSTime = System.nanoTime();
    }

    private static int log2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    private void getLayout(int width, int height) {
        //considering rotating screen ,numSquaresY need to add 3 points
        cellSize = Math.min(width / (numSquaresX + 1), height / (numSquaresY + 3));
        Log.i("ABC", "cellSize:" + cellSize);
        //calc width of grid spacing
        gridWidth = cellSize / (numSquaresX + 3);

        int screenMiddleX = width / 2;
        int screenMiddleY = height / 2;
        int boardMiddleX = screenMiddleX;
        //move table down twice cell size
        int boardMiddleY = screenMiddleY + cellSize / 2;

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(cellSize);
        //calc suitable text size for cell
        baseTextSize = cellSize * cellSize / Math.max(cellSize, paint.measureText("0000"));

        // define (high)score title text size
        scoreTitleSize = baseTextSize / 2;
        //define default text padding
        textPaddingSize = (int) (baseTextSize / 3);

//        bodyTextSize = (int) (baseTextSize / 1.5);
//        gameOverTextSize = baseTextSize * 2;

        //define function button padding
        iconPaddingSize = (int) (baseTextSize / 5);

        double halfNumSquaresX = numSquaresX / 2d;
        double halfNumSquaresY = numSquaresY / 2d;

        tableOriginalX = (int) (boardMiddleX - (cellSize + gridWidth) * halfNumSquaresX - gridWidth / 2);
        tableEndingX = (int) (boardMiddleX + (cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2);
        tableOriginalY = (int) (boardMiddleY - (cellSize + gridWidth) * halfNumSquaresY - gridWidth / 2);
        tableEndingY = (int) (boardMiddleY + (cellSize + gridWidth) * halfNumSquaresY + gridWidth / 2);

        paint.setTextSize(scoreTitleSize);


        // design two kind of size with mode panel
        panelWidth = ((tableEndingX - tableOriginalX) - 2 * tableOriginalX) / 3;
        topLine = (tableOriginalY - panelWidth) / 4;
        //calc (high)score (menu)panel width
        panelWidth = ((tableEndingX - tableOriginalX) - panelWidth - 4 * tableOriginalX) / 2;
        //calc function origin x
        functionOriginX = 3 * tableOriginalX + panelWidth + panelWidth;
        //calc function button width/height
        functionButtonWidth = panelWidth * 3 / 10;
        //calc spacing of function button
        functionButtonSpace = ((tableEndingX - functionOriginX) - 3 * functionButtonWidth) / 2;
        //calc origin y of the  Y axis of function button
        functionButtonTop = topLine + panelWidth * 7 / 10;
        //calc origin x of the X axis of audio button
        audioFunctionStartX = functionOriginX;
        //calc origin x of the  X axis of new game button
        newGameFunctionStartX = tableEndingX - functionButtonWidth;
        //calc origin x of the  X axis of undo game button
        undoFunctionStartX = tableEndingX - functionButtonWidth * 2 - functionButtonSpace;
        //calc origin y of the Y axis of score title
        scoreTitleOriginY = topLine + textPaddingSize - 2 * centerText();

        syncTime();

    }

    private int centerText() {
        return (int) ((paint.descent() + paint.ascent()) / 2);
    }

    /**
     * define mode array to save modes
     */
    private List<String> modeArray;

    private Context context;


    public ShareGameView(Context context, int numSquaresX, int numSquaresY, int mode) {
        super(context);
        this.context = context;
        this.numSquaresX = numSquaresX;
        this.numSquaresY = numSquaresY;
        try {

            //Getting assets
            loadGameModeAssets(mode);
            grid = new Grid(numSquaresX, numSquaresY);
            animGrid = new AnimGrid(numSquaresX, numSquaresY);
            menuPanelBg = ContextCompat.getDrawable(context, R.drawable.menu_background_rectangle);
            scorePanelBg = ContextCompat.getDrawable(context, R.drawable.score_background_rectangle);
            modePanelBg = ContextCompat.getDrawable(context, R.drawable.mode_background_rectangle);

            enableFunctionBg = ContextCompat.getDrawable(context, R.drawable.enable_background_retangle);
            disableFunctionBg = ContextCompat.getDrawable(context, R.drawable.disable_background_retangle);
            whiteTextColor = ContextCompat.getColor(context, R.color.text_white);
            blackTextColor = ContextCompat.getColor(context, R.color.text_black);
            brownTextColor = ContextCompat.getColor(context, R.color.text_brown);
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
            Typeface font = Typeface.createFromAsset(context.getResources().getAssets(), "ClearSans-Bold.ttf");
            paint.setTypeface(font);
            paint.setAntiAlias(true);
        } catch (Exception e) {
            System.out.println("Error getting assets?");
        }
    }


    /**
     * here we load game assets to it's mode
     *
     * @param gameMode
     */
    private void loadGameModeAssets(int gameMode) {
        modeArray = null;
        String[] data = new String[]{};
        switch (gameMode) {
            case 0:
                break;
            case 1:
                data = getResources().getStringArray(R.array.dynasty_mode);
                break;
            case 2:
                data = getResources().getStringArray(R.array.love_mode);
                break;
            case 3:
                data = getResources().getStringArray(R.array.immortal_mode);
                break;
            default:
                break;
        }
        if (data.length > 0) {
            modeArray = Arrays.asList(data);
        }
    }


}
