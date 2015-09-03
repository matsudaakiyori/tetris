package com.example.akiyori.down_grade;

/**
 * Created by akiyori on 2015/08/26.
 */


import android.graphics.Point;
import android.graphics.PointF;

//ブロックの処理をするクラス
public class Block {
    public static int NUM_OF_CELLS = 4;
    private PointF m_pos;
    private BlockType m_type;
    private PointF[] m_locals;
    private Point[] m_cells;

    //コンストラクタ
    public Block() {
        m_pos = new PointF();
        m_pos.x = 0;
        m_pos.y = 0;
        m_type = BlockType.random();

        initPoints();
        update();
    }

    //コンストラクタ
    public Block(Block a_block) {
        m_pos = new PointF();
        m_pos.set(a_block.m_pos);
        m_type = a_block.m_type;
        m_locals = new PointF[NUM_OF_CELLS];
        m_cells = new Point[NUM_OF_CELLS];

        for (int i = 0; i < NUM_OF_CELLS; i++) {
            m_locals[i] = new PointF();
            m_locals[i].set(a_block.m_locals[i]);
            m_cells[i] = new Point(a_block.m_cells[i]);
        } // for
    }

    //座標情報
    private void initPoints() {
        m_locals = new PointF[NUM_OF_CELLS];
        m_cells = new Point[NUM_OF_CELLS];

        for (int i = 0; i < NUM_OF_CELLS; i++) {
            m_locals[i] = new PointF();
            m_locals[i].set(m_type.getPoint(i));
            m_cells[i] = new Point(0, 0);
        } // for i
    }

    //更新
    private void update() {
        for (int i = 0; i < NUM_OF_CELLS; i++) {
            m_cells[i].x = Math.round(m_locals[i].x + m_pos.x);
            m_cells[i].y = Math.round(m_locals[i].y + m_pos.y);
        } // for
    }

    //ゲッター
    public BlockType getType() {
        return m_type;
    }

    //ゲッター
    public Point[] getCells() {
        return m_cells;
    }

    //ブロック前
    public void moveBy(int a_x, int a_y) {
        m_pos.x += a_x;
        m_pos.y += a_y;
        update();
    }

    //ブロック移動後
    public void moveTo(int a_x, int a_y) {
        m_pos.x = a_x;
        m_pos.y = a_y;
        update();
    }

    //ブロック回転
    public void rotate(double a_theta) {
        for (PointF local : m_locals) {
            float x = local.x;
            float y = local.y;
            local.x = (float) (x * Math.cos(a_theta) - y * Math.sin(a_theta));
            local.y = (float) (x * Math.sin(a_theta) + y * Math.cos(a_theta));
        } // local
        update();
    }
}