package com.edu.scnu.a2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2018/3/29.
 */

public class GameView extends GridLayout {

    private Card[][] cardMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();

    private int cardWidth = 0;

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    private void initGameView() {

        setColumnCount(4);     //一行只有4列
        setBackgroundColor(0xffbbada0);   //一个灰色的背景
        cardWidth = GetCardWidth();
        addCards();   //向布局中加入卡片

        startGame();

        setOnTouchListener(new OnTouchListener() {

            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = motionEvent.getX() - startX;
                        offsetY = motionEvent.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                                Log.e("TOUCH","left");
                            } else if (offsetX > 5) {
                                swipeRight();
                                Log.e("TOUCH","right");
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                                Log.e("TOUCH","up");
                            } else if (offsetY > 5) {
                                swipeDown();
                                Log.e("TOUCH","down");
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cardWidth = (int) ((Math.min(w, h) - 10) / 4);

        addCards();
    }

    private void addCards() {

        Card card;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                card = new Card(getContext());
                card.setNum(2);
                addView(card, cardWidth, cardWidth);

                cardMap[x][y] = card;
            }
        }
    }

    private void startGame() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardMap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
        addRandomNum();

    }

    private void addRandomNum() {

        emptyPoints.clear();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x,y));
                }
            }
        }

        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2: 4);
    }

    private void swipeLeft() {

        boolean merge = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                for (int j = x + 1; j < 4; j++) {
                    if (cardMap[j][y].getNum() > 0) {

                        if (cardMap[x][y].getNum() <= 0) {
                            cardMap[x][y].setNum(cardMap[j][y].getNum());
                            cardMap[j][y].setNum(0);
                            x--;

                            merge = true;

                        } else if (cardMap[x][y].equals(cardMap[j][y])) {
                            cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
                            cardMap[j][y].setNum(0);

                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeRight() {

        boolean merge = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {

                for (int j = x - 1; j >= 0; j--) {
                    if (cardMap[j][y].getNum() > 0) {

                        if (cardMap[x][y].getNum() <= 0) {
                            cardMap[x][y].setNum(cardMap[j][y].getNum());
                            cardMap[j][y].setNum(0);
                            x++;

                            merge = true;
                        } else if (cardMap[x][y].equals(cardMap[j][y])) {
                            cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
                            cardMap[j][y].setNum(0);

                            merge = true;

                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeUp() {

        boolean merge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int j = y + 1; j < 4; j++) {
                    if (cardMap[x][j].getNum() > 0) {

                        if (cardMap[x][y].getNum() <= 0) {
                            cardMap[x][y].setNum(cardMap[x][j].getNum());
                            cardMap[x][j].setNum(0);
                            y--;
                            merge = true;

                        } else if (cardMap[x][y].equals(cardMap[x][j])) {
                            cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
                            cardMap[x][j].setNum(0);

                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeDown() {

        boolean merge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {

                for (int j = y - 1; j >= 0; j--) {
                    if (cardMap[x][j].getNum() > 0) {

                        if (cardMap[x][y].getNum() <= 0) {
                            cardMap[x][y].setNum(cardMap[x][j].getNum());
                            cardMap[x][j].setNum(0);
                            y--;

                            merge = true;

                        } else if (cardMap[x][y].equals(cardMap[x][j])) {
                            cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
                            cardMap[x][j].setNum(0);

                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete(){

        boolean complete = true;

        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardMap[x][y].getNum()==0||
                        (x>0 && cardMap[x][y].equals(cardMap[x-1][y])) ||
                        (x<3 && cardMap[x][y].equals(cardMap[x+1][y])) ||
                        (y>0 && cardMap[x][y].equals(cardMap[x][y-1])) ||
                        (y<3 && cardMap[x][y].equals(cardMap[x][y+1]))) {

                    complete = false;
                    break ALL;
                }
            }
        }

        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }

    }

    private int GetCardWidth()
    {
        //屏幕信息的对象
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();

        //获取屏幕信息
        int cardWidth;
        cardWidth = displayMetrics.widthPixels;

        //一行有四个卡片，每个卡片占屏幕的四分之一
        return ( cardWidth - 10 ) / 4;

    }
}
