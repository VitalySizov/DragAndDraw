package com.sizov.vitaly.draganddraw;

import android.graphics.PointF;

public class Box {

    private PointF mOrigin;
    private PointF mCurrent;
    private int firstId; // ID для первого пальца

    private PointF mOrigin2;
    private PointF mCurrent2;
    private float angle;
    private int secondId; // ID для второго пальца

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public PointF getOrigin2() {
        return mOrigin2;
    }

    public void setOrigin2(PointF origin2) {
        mOrigin2 = origin2;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    public int getFirstId() {
        return firstId;
    }

    public void setFirstId(int firstId) {
        this.firstId = firstId;
    }

    public PointF getCurrent2() {
        return mCurrent2;
    }

    public void setCurrent2(PointF current2) {
        mCurrent2 = current2;
    }

    public int getSecondId() {
        return secondId;
    }

    public void setSecondId(int secondId) {
        this.secondId = secondId;
    }
}
