package com.sizov.vitaly.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {

    private static final String TAG = "BoxDrawingView";
    private static final String KEY_DATA = "key_data";
    private static final String KEY_BOXEN = "key_boxen";


    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackGroundPaint;

    // Используется при создании представления в коде
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Треугольники рисуются полупрозрачными красным цветом (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        // Фон закрашевается серовато-белым цветом
        mBackGroundPaint = new Paint();
        mBackGroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Заполнение фона
        canvas.drawPaint(mBackGroundPaint);

        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, right, top, bottom, mBoxPaint);
        }
    }

    // Используется при заполнении представления по разметке XML
    public BoxDrawingView(Context context) {
        super(context, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                // Сброс текущего состояния
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }

        Log.i(TAG, action + " at x = " + current.x + ", y = " + current.y);

        return true;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable data = super.onSaveInstanceState();
        bundle.putParcelable(KEY_DATA, data);
        bundle.putSerializable(KEY_BOXEN, (ArrayList) mBoxen);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable data = bundle.getParcelable(KEY_DATA);
        mBoxen = (List<Box>) bundle.getSerializable(KEY_BOXEN);
        super.onRestoreInstanceState(data);
        invalidate();
    }
}
