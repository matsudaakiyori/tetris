package com.example.akiyori.down_grade;

/**
 * Created by akiyori on 2015/08/26.
 */

import android.graphics.PointF;

import java.util.Random;

//ブロックの形と生成
public enum BlockType {
    T(0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f),
    X(-0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f),
    L(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 1.0f, -1.0f),
    J(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, -1.0f, -1.0f),
    B(0.0f, -1.5f, 0.0f, -0.5f, 0.0f, 0.5f, 0.0f, 1.5f),
    S(-0.5f, 0.0f, 0.5f, 0.0f, -0.5f, 1.0f, 0.5f, -1.0f),
    Z(-0.5f, 0.0f, 0.5f, 0.0f, -0.5f, -1.0f, 0.5f, 1.0f),
    EMPTY(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    private static int block_types = 7;
    private static Random random_number = new Random();
    private static int block_color = 0;

    //乱数
    public static BlockType random() {
        return valueOf(random_number.nextInt(block_types));
    }

    //7種類の色と形の生成
    public static BlockType valueOf(int a_value) {
        switch (a_value) {
            case 0:
                block_color = 0;
                setBlock_color(block_color);
                return T;
            case 1:
                block_color = 1;
                setBlock_color(block_color);
                return X;
            case 2:
                block_color = 2;
                setBlock_color(block_color);
                return L;
            case 3:
                block_color = 3;
                setBlock_color(block_color);
                return J;
            case 4:
                block_color = 4;
                setBlock_color(block_color);
                return B;
            case 5:
                block_color = 5;
                setBlock_color(block_color);
                return S;
            case 6:
                block_color = 6;
                setBlock_color(block_color);
                return Z;
        } // switch

        return EMPTY;
    }

    //座標
    private PointF[] m_points;

    BlockType(float a_x0, float a_y0,
              float a_x1, float a_y1,
              float a_x2, float a_y2,
              float a_x3, float a_y3) {

        m_points = new PointF[Block.NUM_OF_CELLS];
        m_points[0] = new PointF(a_x0, a_y0);
        m_points[1] = new PointF(a_x1, a_y1);
        m_points[2] = new PointF(a_x2, a_y2);
        m_points[3] = new PointF(a_x3, a_y3);
    }

    //色ゲッター
    public static int getBlock_color() {
        return block_color;
    }

    //色セッター
    public static void setBlock_color(int block_color) {
        BlockType.block_color = block_color;
    }

    //座標ゲッター
    public PointF getPoint(int a_i) {
        return m_points[a_i];
    }
}