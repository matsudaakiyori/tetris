package com.example.akiyori.down_grade;

/**
 * Created by akiyori on 2015/08/26.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

//ゲームに関するクラス
public class Game {
    //ゲームサイズ
    public static final int WIDTH = 20;
    public static final int HEIGHT = 38;
    private String game_over = null;

    /**
     * ゲッター
     */
    public String getGame_over() {
        return game_over;
    }

    //セッター
    public void setGame_over(String game_over) {
        this.game_over = game_over;
    }

    private interface Transformable {
        void transform();
    }

    //ゲーム状態
    enum GameMode {
        NEW,
        ACTIVE,
        GAME_OVER
    }

    private GameMode m_mode;
    private Board m_board;
    private Board m_mini;
    private Block m_currentBlock;
    private Block m_nextBlock;

    //コンストラクタ
    public Game(Context context) {
        m_mode = GameMode.NEW;
        m_board = new Board(new Point(WIDTH, HEIGHT), new Point(10, 10));
        //予測画面のサイズと位置
        m_mini = new Board(new Point(5, 5), new Point(430, 10));
        loadNextBlock();
        loadNewBlock();
    }

    //描画
    public void draw(Canvas a_canvas) {
        m_board.draw(a_canvas);
        m_board.drawBlock(a_canvas, m_currentBlock);
        m_mini.draw(a_canvas);
    }
    //予測ブロック画面のブロックの位置

    public boolean loadNextBlock() {
        m_nextBlock = new Block();
        m_nextBlock.moveTo(m_mini.getSize().x / 2,
                m_mini.getSize().y - 3);

        m_mini.clear();
        if (!m_mini.isBlockInBoundary(m_nextBlock)
                || !m_mini.isBlockCollide(m_nextBlock)) {
            return false;
        }
        return m_mini.load(m_nextBlock);
    }

    //現在のブロックの位置
    public boolean loadNewBlock() {
        m_currentBlock = m_nextBlock;
        loadNextBlock();
        //落ちてくるブロックの位置
        m_currentBlock.moveTo(m_board.getSize().x / 2,
                m_board.getSize().y - 3);
        if (!m_board.isBlockInBoundary(m_currentBlock)
                || !m_board.isBlockCollide(m_currentBlock)) {
            m_mode = GameMode.GAME_OVER;
            this.setGame_over("ゲームオーバー");
            return false;
        }
        return m_board.load(m_currentBlock);
    }

    //ゲームが動いているか
    public boolean moveBy(final int a_x, final int a_y) {
        if (m_mode == GameMode.NEW) {
            m_mode = GameMode.ACTIVE;
        } // if

        return transform(new Transformable() {
            @Override
            public void transform() {
                m_currentBlock.moveBy(a_x, a_y);
            }
        });
    }

    //回転
    public boolean rotate() {
        return transform(new Transformable() {
            @Override
            public void transform() {
                m_currentBlock.rotate(Math.PI / 2.0);
            }
        });
    }

    //ゲーム遷移
    private boolean transform(Transformable a_tansformable) {
        if (m_mode != GameMode.ACTIVE) {
            return false;
        } // if

        boolean retval = true;
        Block temp = new Block(m_currentBlock);
        m_board.unload(m_currentBlock);
        a_tansformable.transform();
        if (!m_board.isBlockInBoundary(m_currentBlock)
                || !m_board.isBlockCollide(m_currentBlock)) {
            m_currentBlock = temp;
            retval = false;
        } // if

        m_board.load(m_currentBlock);
        return retval;
    }

    //ゲームチェック
    public boolean tick() {
        if (m_mode != GameMode.ACTIVE) {
            return false;
        } // if

        if (moveBy(0, -1)) {
            return true;
        } // if

        m_board.checkRows();

        return loadNewBlock();
    }

    //移動
    public boolean drop() {
        while (moveBy(0, -1)) {
        } // while
        return false;
    }

    //ゲッター
    public int getScore() {
        return m_board.getScore();
    }
}