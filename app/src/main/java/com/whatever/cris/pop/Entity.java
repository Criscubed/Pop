package com.whatever.cris.pop;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Entity {
    protected static Random mDice = new Random();
    protected float mX = 0;
    protected float mY = 0;
    protected float mWidth = 0;
    protected float mHeight = 0;
    protected float mVelocityX = 0;
    protected float mVelocityY = 0;

    public Entity() {}
    public Entity(float x, float y){
        mX = x;
        mY = y;
    }

    public void respawn(){    }

    public void input(Game game){    }
    public void update(){
        mX += mVelocityX;
        mY += mVelocityY;
    }
    public void render(final Canvas canvas, final Paint paint){}

    public boolean isColliding(Entity that){
        return Entity.intersectsAABB(this, that);
    }
    public void onCollision(Entity e){
    }
    public static boolean intersectsAABB(Entity a, Entity b){
        return !(a.right() < b.left()
                || b.right() < a.left()
                || a.bottom() < b.top()
                || b.bottom() < a.top());
    }

    public void worldWrap(final float width, final float height){
        mX = Utils.wrap(mX, -mWidth, width+mWidth);
    }

    public float left(){
        return mX;
    }
    public float right(){
        return mX+mWidth;
    }
    public float top(){
        return mY;
    }
    public float bottom(){
        return mY + mHeight;
    }

    public float getX() {
        return mX;
    }
    public void setX(float mX) {
        this.mX = mX;
    }
    public float getY() {
        return mY;
    }
    public void setY(float mY) {
        this.mY = mY;
    }
    public float getWidth() {
        return mWidth;
    }
    public void setWidth(float mWidth) {
        this.mWidth = mWidth;
    }
    public float getHeight() {
        return mHeight;
    }
    public void setHeight(float mHeight) {
        this.mHeight = mHeight;
    }
    public float getVelocityX() {
        return mVelocityX;
    }
    public void setVelocityX(float mVelocityX) {
        this.mVelocityX = mVelocityX;
    }
    public float getVelocityY() {
        return mVelocityY;
    }
    public void setVelocityY(float mVelocityY) {
        this.mVelocityY = mVelocityY;
    }
}
