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

        // Прямоугольники рисуются полупрозрачными красным цветом (ARGB)
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


            // Расчет центра прямоугольника
            float px = (box.getCurrent().x + box.getOrigin().x) / 2;
            float py = (box.getCurrent().y + box.getOrigin().y) / 2;

            canvas.rotate(box.getAngle(), px, py);
            canvas.drawRect(left, right, top, bottom, mBoxPaint);
            canvas.rotate(-1 * box.getAngle(), px, py);
        }
    }

    // Угол между вертикальным сегментом от центральной точки и сегментом от центра
    public float calcAngle(PointF center, PointF target) {
        float angle = (float) Math.atan2(target.y - center.y, target.x - center.x );
        angle += Math.PI/2.0;
        // Перевод в градусы
        angle = (float) Math.toDegrees(angle);

        if (angle < 0 ) {
            angle += 360;
        }
        return angle;
    }

    // Используется при заполнении представления по разметке XML
    public BoxDrawingView(Context context) {
        super(context, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                // Новый прямоугольник
                mCurrentBox = new Box(current);
                mCurrentBox.setFirstId(event.getPointerId(0));
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if ((event.getPointerCount() >= 2) && (mCurrentBox != null)) {
                    // Второй палец
                    PointF current2 = new PointF(event.getX(1), event.getY(1));
                    mCurrentBox.setOrigin2(current2);
                    mCurrentBox.setSecondId(event.getPointerId(1));
                    break;
                }
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if ((mCurrentBox != null) && (mCurrentBox.getFirstId() == event.getPointerId(0))) {
                    mCurrentBox.setCurrent(current);
                    // Второй палец
                    if (event.getPointerCount() >= 2) {
                        if (mCurrentBox.getSecondId() == event.getPointerId(1)) {
                            PointF current2 = new PointF(event.getX(1), event.getY(1));
                            mCurrentBox.setCurrent2(current2);
                            // Расчет угла
                            mCurrentBox.setAngle(calcAngle(mCurrentBox.getOrigin2(),
                                    mCurrentBox.getCurrent2()));
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action = "ACTION_POINTER_UP";
                if (mCurrentBox != null) {
                    Log.i(TAG, "Angle = " + mCurrentBox.getAngle());
                }
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
