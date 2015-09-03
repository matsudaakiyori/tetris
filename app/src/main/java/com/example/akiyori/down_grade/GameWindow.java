package com.example.akiyori.down_grade;

/**
 * Created by akiyori on 2015/08/26.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
//ゲームを描画するクラス
public class GameWindow extends SurfaceView implements SurfaceHolder.Callback {
    private TetrisThread thread;
    //コンストラクタ
    public GameWindow(Context a_context, AttributeSet a_attrs) {
        super(a_context, a_attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new TetrisThread(holder, a_context, new Handler() {
            @Override
            public void handleMessage(Message m) {

            }
        });
        setFocusable(true);
        setLongClickable(true);
        setGesture();
    }
//タッチイベント
    private void setGesture() {
        final GestureDetector gestureDetector = new GestureDetector(
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {

                        thread.addFling(velocityX, velocityY);
                        return true;
                    }
                });

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        thread.setSurfaceSize(width, height);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode

        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {

                thread.join();

                retry = false;
            } catch (InterruptedException e) {
            }
        }
        thread = null;
    }
}
