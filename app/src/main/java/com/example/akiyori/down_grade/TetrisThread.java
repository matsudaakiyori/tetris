package com.example.akiyori.down_grade;

/**
 * Created by akiyori on 2015/08/26.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

//スレッドを継承しているクラス
public class TetrisThread extends Thread {
    private SurfaceHolder holder;
    private Handler handler;
    private Context context;
    private boolean m_isRunning;
    private Paint m_black;
    private long m_fps;
    private int m_targetFps;
    private int m_frameQuantum;
    private int m_canvasWidth;
    private int m_canvasHeight;
    private Game game;
    private Tetris tetris;

    public TetrisThread(SurfaceHolder a_holder, Context a_context,
                        Handler a_handler) {
        // get handles to some important objects
        holder = a_holder;
        handler = a_handler;
        context = a_context;
        tetris = (Tetris) context;
        m_targetFps = 30;
        m_frameQuantum = 1000 / m_targetFps;
        m_black = new Paint();
        m_black.setAntiAlias(false);
        //ゲーム画面の色
        m_black.setColor(Color.BLACK);
        game = new Game(context);
    }

    @Override
    /**
     */
    //スレッド
    public void run() {
        long diffs[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        long diff;
        int diffPos = 0;
        int frame = 0;

        while (m_isRunning) {
            long start = System.currentTimeMillis();

            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas(null);
                synchronized (holder) {
                    if (frame++ == 0) {
                        game.tick();
                    } else {
                        if (frame == m_targetFps) {
                            frame = 0;
                        } // if
                    } // else

                    doDraw(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                } // if

            } // try-finally

            diff = System.currentTimeMillis() - start;
            if (diff < m_frameQuantum && m_isRunning) {
                try {
                    Thread.sleep(m_frameQuantum - diff);
                } catch (InterruptedException e) {
                }
            }

            diff = System.currentTimeMillis() - start;
            if (++diffPos > 9) {
                diffPos = 0;
            } // if
            diffs[diffPos] = diff;
            long total = 0;
            for (long value : diffs) {
                total += value;
            } // for
            m_fps = 10000 / total;

        }

    }

    /**
     */
    //描画
    private void doDraw(Canvas a_canvas) {
        a_canvas.drawRect(0, 0, m_canvasWidth, m_canvasHeight, m_black);
        game.draw(a_canvas);
    }

    //スレッドの動きの判定のセッター
    public void setRunning(boolean b) {
        m_isRunning = b;
    }

    //画面サイズのセッター
    public void setSurfaceSize(int a_width, int a_height) {
        // synchronized to make sure these all change atomically
        synchronized (holder) {
            m_canvasWidth = a_width;
            m_canvasHeight = a_height;
        } // synchronized
    }

    /**
     * @param a_vx The velocity of this fling measured in pixels per second along
     *             the x axis.
     * @param a_vy The velocity of this fling measured in pixels per second along
     *             the y axis.
     */
    //モーションによる操作
    public void addFling(float a_vx, float a_vy) {
        int theta = (int) Math.toDegrees(Math.atan2(a_vy, a_vx));
        if (game.getGame_over() != null) {
            tetris.game_over(game.getScore());
        }

        if (theta < 0) {
            theta += 360;
        } // if
        if (theta < 45 || theta >= 315) {
            synchronized (holder) {
                game.moveBy(1, 0);
            } // synchronized

            return;
        } // if

        if (theta >= 45 && theta < 135) {
            synchronized (holder) {
                game.drop();
            } // synchronized

            return;
        } // if

        if (theta >= 135 && theta < 225) {
            synchronized (holder) {
                game.moveBy(-1, 0);
            } // synchronized

            return;
        } // if

        if (theta >= 225 && theta < 315) {
            synchronized (holder) {
                game.rotate();
            } // synchronized

            return;
        } // if
    }


}