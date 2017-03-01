package com.example.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.List;

public class GridLineView extends View implements OnTouchListener{
    private static final int DEFAULT_PAINT_COLOR = Color.GRAY;
    private static final int DEFAULT_NUMBER_OF_ROWS = 12;
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 12;

    private int numRows = DEFAULT_NUMBER_OF_ROWS, numColumns = DEFAULT_NUMBER_OF_COLUMNS;
    private boolean showGrid = false;

    private final Paint paint = new Paint();

    private boolean[][]  dots = new boolean[numRows][numColumns];
    private boolean[][]  nextDotState = new boolean[numRows][numColumns];


    Paint pDot= new Paint();
    Paint pFalseDot= new Paint();
    float[] xCoords = new float[numColumns];
    float[] yCoords = new float[numRows];

    float xStep = 0;
    float yStep = 0;

    private List<Point> circlePoints;


    public GridLineView(Context context) {
        super(context);
        init();
    }

    public GridLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        circlePoints = new ArrayList<Point>();
        init();
    }

    public GridLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setColor(DEFAULT_PAINT_COLOR);
        pDot.setColor(Color.RED);
        pFalseDot.setColor(Color.WHITE);
        pFalseDot.setAlpha(125);
    }

    public void setDots(boolean[][] dotSelected)
    {
        this.dots = dotSelected;
    }

    public boolean[][] getDots()
    {
        return dots;
    }

    public void setLineColor(int color){
        paint.setColor(color);
    }

    public void setStrokeWidth(int pixels){
        paint.setStrokeWidth(pixels);
    }

    public int getNumberofRows(){
        return numRows;
    }

    public void setNumberofRows(int numRows) {
        if (numRows > 0) {
            this.numRows = numRows;
        } else {
            throw new RuntimeException("Cannot have a negative number of rows");
        }
    }

    public int getNumberOfColumns() {
        return numColumns;
    }

    public void setNumberOfColumns(int numColumns) {
        if (numColumns > 0) {
            this.numColumns = numColumns;
        } else {
            throw new RuntimeException("Cannot have a negative number of columns");
        }
    }

    public void resetGrid(){

        circlePoints.clear();
        for(int y = 0; y < numRows; y++){
            for(int x = 0; x < numRows; x++){
                dots[x][y]= false;
            }
        }
        invalidate();
    }

    public boolean isGridShown() {
        return showGrid;
    }

    public void toggleGrid() {
        showGrid = !showGrid;
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_DOWN) {
            return true;
        }
        int xNear = 0;
        int yNear = 0;
        float xMin = getMeasuredWidth();
        float yMin = getMeasuredWidth();

        for(int x=0; x < numRows ;x++) {
            if(Math.abs(xCoords[x] - event.getX()) < xMin)
            {
                xMin = Math.abs(xCoords[x] - event.getX());
                xNear = x;
            }
        }
        for (int y = 0; y < numColumns; y++)
        {
            if (Math.abs(yCoords[y] - event.getY()) < yMin)
            {
                yMin = Math.abs(yCoords[y] - event.getY());
                yNear = y;
            }
        }

        dots[xNear][yNear] = !dots[xNear][yNear];
        circlePoints.add(new Point(xNear, yNear));
        invalidate();

        return true;

    }

    @Override
    protected void onDraw(Canvas canvas){

        if (showGrid) {
            int width = getMeasuredWidth();
            int height = getMeasuredWidth();

            // Vertical lines
            for (int i = 1; i < numColumns; i++) {
                canvas.drawLine(width * i / numColumns, 0, width * i / numColumns, height, paint);
            }

            // Horizontal lines
            for (int i = 1; i < numRows; i++) {
                canvas.drawLine(0, height * i / numRows, width, height * i / numRows, paint);
            }

            xStep = width / (numColumns);
            yStep = width / (numRows);

            yCoords[0] = yStep/2;
            xCoords[0] = xStep/2;

            float plotX;
            float plotY;

            //plot clicked Dots
            for (Point p : circlePoints) {
                plotX = (p.x * xStep) + (xStep/2);
                plotY = (p.y * xStep) + (xStep /2);

                if(dots[p.x][p.y]){
                    canvas.drawCircle(plotX , plotY , xStep/2, pDot);
                }
                else{
                    canvas.drawCircle(plotX , plotY , xStep/2, pFalseDot);
                }
            }

            float x1,y1;
            for(int y = 0; y < numRows; y++) //rows
            {
                for(int x = 0; x < numColumns; x++) //cols
                {
                    x1 = (x * xStep) + (xStep/2);
                    y1 = (y * xStep) + (xStep /2);

                    if(dots[x][y]){
                        canvas.drawCircle(x1 , y1 , xStep/2, pDot);
                    }
                }
            }


            for (int y = 1; y < numRows; y++)
            {
                yCoords[y] = yCoords[y-1] +yStep;
            }

            for (int x = 1; x < numColumns; x++)
            {
                xCoords[x] = xCoords[x-1] + xStep;
            }
        }

    }

    public void setNextState()
    {
        for (int y = 0; y < numRows; y++)
            for (int x = 0; x < numColumns; x++){
                dots[x][y] = nextDotState[x][y];
            }

    }
    public int calculateNeighbors(int x, int y) {

        int count = 0;

        // Check cell on the right.
        if (x != numRows - 1){
            if (dots[x + 1][y])
                count++;
        }


        // Check cell on the bottom right.
        if (x != numRows - 1 && y != numColumns - 1){
            if (dots[x + 1][y + 1])
                count++;
        }


        // Check cell on the bottom.
        if (y != numColumns - 1){
            if (dots[x][y + 1])
                count++;
        }


        // Check cell on the bottom left.
        if (x != 0 && y != numColumns - 1){
            if (dots[x - 1][ y + 1])
                count++;
        }


        // Check cell on the left.
        if (x != 0){
            if (dots[x - 1][y])
                count++;
        }


        // Check cell on the top left.
        if (x != 0 && y != 0){
            if (dots[x - 1][ y - 1])
                count++;
        }


        // Check cell on the top.
        if (y != 0){
            if (dots[x][y - 1])
                count++;
        }


        // Check cell on the top right.
        if (x != numRows - 1 && y != 0){
            if (dots[x + 1][ y - 1])
                count++;
        }


        return count;

    }



    public void nextStep(){

        for(int y = 0; y < numRows; y++){
            for(int x = 0; x < numColumns; x++){
                boolean living = dots[x][y];
                int count = calculateNeighbors(x,y);
                boolean result = false;

                //apply rules
                if (living && count < 2)
                    result = false;
                if (living && (count == 2 || count == 3))
                    result = true;
                if (living && count > 3)
                    result = false;
                if (!living && count == 3)
                    result = true;

                nextDotState[x][y] = result;
            }
        }
        setNextState();
        invalidate();
    }


}
