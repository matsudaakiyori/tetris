package com.example.akiyori.down_grade;

/**
 * Created by akiyori on 2015/08/27.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by akiyori on 2015/08/26.
 */
//ゲームボードのクラス
public class Board {
    public static final int CELL_WIDTH = 18;
    public static final int CELL_PADDING = 2;
    private static Paint s_framePaint = null;
    private static Paint s_blockPaint = null;
    private static Paint paint = null;
    private static BlockType blockType;
    private static int count = 0;

    //フレームの色
    private static Paint getFramePaint() {
        if (s_framePaint == null) {
            s_framePaint = new Paint();
            s_framePaint.setAntiAlias(true);
            s_framePaint.setColor(Color.WHITE);
            s_framePaint.setStyle(Paint.Style.STROKE);
        } // if

        return s_framePaint;
    }

    //落ちた後のブロックの色
    private static Paint getBlockPaint() {
        if (s_blockPaint == null) {
            s_blockPaint = new Paint();
            s_blockPaint.setAntiAlias(true);
            s_blockPaint.setColor(Color.WHITE);
        }

        return s_blockPaint;
    }


    //落ちるまでのブロック色
    private static Paint getPaint() {
        if (blockType.getBlock_color() == 0) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
        } else if (blockType.getBlock_color() == 1) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);
        } // if
        else if (blockType.getBlock_color() == 2) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.GREEN);
        } // if
        else if (blockType.getBlock_color() == 3) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.YELLOW);
        } // if
        else if (blockType.getBlock_color() == 4) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.CYAN);
        } // if
        else if (blockType.getBlock_color() == 5) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.MAGENTA);
        } // if
        else if (blockType.getBlock_color() == 6) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.LTGRAY);
        }
        return paint;
    }

    private Point m_location;
    private Point m_size;
    private int m_cellCount;
    private ArrayList<Rect> m_rectangles;
    private BlockType[] m_cells;
    private Rect m_frame;

    //コンストラクタ
    public Board(Point a_size, Point a_location) {
        m_size = a_size;
        m_location = a_location;
        m_cellCount = a_size.x * a_size.y;

        initRects();
        m_cells = new BlockType[m_cellCount];
        clear();
    }

    /**
     * initializes m_rectangles.
     */
    //マス情報
    private void initRects() {
        m_rectangles = new ArrayList<Rect>();
        for (int y = 0; y < m_size.y; y++) {
            for (int x = 0; x < m_size.x; x++) {
                Point coord = toCoord(x, y);
                m_rectangles.add(new Rect(coord.x, coord.y,
                        coord.x + CELL_WIDTH, coord.y + CELL_WIDTH));
            } // for x
        } // for y

        Point coord = toCoord(m_size.x, -1);
        m_frame = new Rect(m_location.x, m_location.y,
                coord.x, coord.y);
    }

    //マスの位置
    private Point toCoord(int a_x, int a_y) {
        return new Point(a_x * (CELL_WIDTH + CELL_PADDING) + CELL_PADDING + m_location.x,
                (m_size.y - a_y - 1) * (CELL_WIDTH + CELL_PADDING) + CELL_PADDING + m_location.y);
    }

    //マスの有無
    private boolean isCellEmpty(int a_i) {
        return m_cells[a_i] == BlockType.EMPTY;
    }

    //マスの有無
    private boolean isCellEmpty(Point a_pos) {
        return getCell(a_pos) == BlockType.EMPTY;
    }

    //マスの有無
    private boolean isCellEmpty(int a_x, int a_y) {
        return m_cells[a_y * m_size.x + a_x] == BlockType.EMPTY;
    }

    //ゲッター
    private BlockType getCell(Point a_pos) {
        return m_cells[a_pos.y * m_size.x + a_pos.x];
    }

    //マスの境界の有無
    private boolean isCellInBoundary(Point a_pos) {
        return (a_pos.x >= 0
                && a_pos.x < m_size.x
                && a_pos.y >= 0
                && a_pos.y < m_size.y);
    }

    //セッター
    private void setCell(Point a_pos, BlockType a_value) {
        m_cells[a_pos.y * m_size.x + a_pos.x] = a_value;
    }

    //初期化
    public void clear() {
        for (int i = 0; i < m_cellCount; i++) {
            m_cells[i] = BlockType.EMPTY;
        } // for i
    }

    //ゲームボードの描画
    public void draw(Canvas a_canvas) {
        for (int i = 0; i < m_cellCount; i++) {
            if (!isCellEmpty(i)) {
                a_canvas.drawRect(m_rectangles.get(i), getBlockPaint());
            } // if
        } // for i

        a_canvas.drawRect(m_frame, getFramePaint());
    }

    //ブロック描画
    public void drawBlock(Canvas a_canvas, Block a_block) {
        for (Point cell : a_block.getCells()) {
            a_canvas.drawRect(m_rectangles.get(cell.y * m_size.x + cell.x), getPaint());
        }  // for cell
    }

    //ブロックを積む
    public boolean load(Block a_block) {
        for (Point cell : a_block.getCells()) {
            if (!isCellEmpty(cell)) {
                return false;
            } // if

            setCell(cell, a_block.getType());
        } // for cell
        return true;
    }

    //ブロックを積まない
    public boolean unload(Block a_block) {
        for (Point cell : a_block.getCells()) {
            setCell(cell, BlockType.EMPTY);
        } // for cell

        return true;
    }

    //ブロックの境界
    public boolean isBlockInBoundary(Block a_block) {
        for (Point cell : a_block.getCells()) {
            if (!isCellInBoundary(cell)) {
                return false;
            } // if
        } // for cell

        return true;
    }

    //ブロックの衝突
    public boolean isBlockCollide(Block a_block) {
        for (Point cell : a_block.getCells()) {
            if (!isCellEmpty(cell)) {
                return false;
            } // if
        } // for cell

        return true;
    }

    //行の判定
    public void checkRows() {
        for (int y = m_size.y - 1; y >= 0; y--) {
            if (isRowFilled(y)) {
                removeRow(y);
            } // if
        } // for y
    }

    //行の有無
    private boolean isRowFilled(int a_y) {
        for (int x = 0; x < m_size.x; x++) {
            if (isCellEmpty(x, a_y)) {
                return false;
            } // if
        } // for x

        return true;
    }

    //行の削除
    private void removeRow(int a_y) {
        for (int i = a_y * m_size.x; i < m_cellCount - m_size.x; i++) {
            m_cells[i] = m_cells[i + m_size.x];
        } // for i

        for (int i = m_cellCount - m_size.x; i < m_cellCount; i++) {
            m_cells[i] = BlockType.EMPTY;
        } // for i
        count = count + 1000;
    }

    //サイズゲッター
    public Point getSize() {
        return m_size;
    }

    //スコアゲッター
    public int getScore() {
        return count;
    }
}

