package net.diskroom.weather;

import java.io.Serializable;

public final class Point implements Serializable {
    private float mX;
    private float mY;

    public Point() {
    }

    public Point(float x, float y) {
        mX = x;
        mY = y;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public void setX(float x) {
        mX = x;
    }

    public void setY(float y) {
        mY = y;
    }
}