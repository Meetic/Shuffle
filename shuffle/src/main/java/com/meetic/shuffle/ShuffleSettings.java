package com.meetic.shuffle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class ShuffleSettings {

    public static final float MIN_VELOCITY = 700;
    public final static int DIFFERENCE_TRANSLATION_Y_DP = 12;
    public final static int DIFFERENCE_TRANSLATION_X_DP = 0;
    public final static int ORIENTATION_HORIZONTAL = 0;
    public final static int ORIENTATION_VERTICAL = 1;
    public final static int STACK_FROM_TOP = 0;
    public final static int STACK_FROM_BOTTOM = 1;
    int numberOfDisplayedCards = 3;

    float differenceScale = 0.02f;
    float differenceTranslationY;
    float differenceTranslationX;

    int colorLeft = Color.TRANSPARENT;
    int layoutLeftResId = 0;
    int colorRight = Color.TRANSPARENT;
    int layoutRightResId = 0;

    int animationReturnCardDuration = 300;

    boolean vertical = false;
    boolean inlineMove = false;

    boolean rotationEnabled = true;
    float rotation = 45f;
    float minVelocity;

    private boolean stackFromTop;

    public static float dpToPx(Context context, float dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float pxToDp(Context context, float px) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public int getAnimationReturnCardDuration() {
        return animationReturnCardDuration;
    }

    public void setAnimationReturnCardDuration(int animationReturnCardDuration) {
        this.animationReturnCardDuration = animationReturnCardDuration;
    }

    public int getNumberOfDisplayedCards() {
        return numberOfDisplayedCards;
    }

    public void setNumberOfDisplayedCards(int numberOfDisplayedCards) {
        this.numberOfDisplayedCards = numberOfDisplayedCards;
    }

    public boolean isRotationEnabled() {
        return rotationEnabled;
    }

    public void setRotationEnabled(boolean rotationEnabled) {
        this.rotationEnabled = rotationEnabled;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public boolean isInlineMove() {
        return inlineMove;
    }

    public void setInlineMove(boolean inlineMove) {
        this.inlineMove = inlineMove;
    }

    public float getDifferenceTranslationY() {
        return differenceTranslationY;
    }

    public void setDifferenceTranslationY(float differenceTranslationY) {
        this.differenceTranslationY = differenceTranslationY;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public float getDifferenceScale() {
        return differenceScale;
    }

    public void setDifferenceScale(float differenceScale) {
        this.differenceScale = differenceScale;
    }

    public float getMinVelocity() {
        return minVelocity;
    }

    public void setMinVelocity(float minVelocity) {
        this.minVelocity = minVelocity;
    }

    public int getColorLeft() {
        return colorLeft;
    }

    public void setColorLeft(int colorLeft) {
        this.colorLeft = colorLeft;
    }

    public int getColorRight() {
        return colorRight;
    }

    public void setColorRight(int colorRight) {
        this.colorRight = colorRight;
    }

    public int getLayoutRightResId() {
        return layoutRightResId;
    }

    public int getLayoutLeftResId() {
        return layoutLeftResId;
    }

    public float getScaleForPosition(int position) {
        return 1f - (position * differenceScale);
    }

    public float getTranslationYForPosition(int position) {
        return position * differenceTranslationY;
    }

    public float getTranslationXForPosition(int position) {
        return position * differenceTranslationX;
    }

    public boolean isStackFromTop() {
        return stackFromTop;
    }

    protected void handleAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.Shuffle);
            {
                int orientation = styledAttrs.getInteger(R.styleable.Shuffle_shuffle_orientation, ORIENTATION_HORIZONTAL);
                this.vertical = orientation == ORIENTATION_VERTICAL;
            }
            {
                this.rotationEnabled = styledAttrs.getBoolean(R.styleable.Shuffle_shuffle_rotationEnabled, rotationEnabled);
                this.rotation = styledAttrs.getFloat(R.styleable.Shuffle_shuffle_rotation, rotation);
            }
            {
                this.numberOfDisplayedCards = styledAttrs.getInt(R.styleable.Shuffle_shuffle_numberOfDisplayedCards, numberOfDisplayedCards);
            }
            {
                this.inlineMove = styledAttrs.getBoolean(R.styleable.Shuffle_shuffle_inlineMove, inlineMove);
            }
            {
                this.differenceScale = styledAttrs.getFloat(R.styleable.Shuffle_shuffle_differenceScale, differenceScale);
                this.differenceTranslationY = styledAttrs.getDimensionPixelOffset(R.styleable.Shuffle_shuffle_differenceTranslationY, (int) dpToPx(context, DIFFERENCE_TRANSLATION_Y_DP));
                this.differenceTranslationX = styledAttrs.getDimensionPixelOffset(R.styleable.Shuffle_shuffle_differenceTranslationX, (int) dpToPx(context, DIFFERENCE_TRANSLATION_X_DP));
            }
            {
                this.minVelocity = styledAttrs.getFloat(R.styleable.Shuffle_shuffle_velocityMin, MIN_VELOCITY);
            }
            {
                this.colorLeft = styledAttrs.getColor(R.styleable.Shuffle_shuffle_colorLeft, colorLeft);
                this.layoutLeftResId = styledAttrs.getResourceId(R.styleable.Shuffle_shuffle_layoutLeft, layoutLeftResId);
                this.colorRight = styledAttrs.getColor(R.styleable.Shuffle_shuffle_colorRight, colorRight);
                this.layoutRightResId = styledAttrs.getResourceId(R.styleable.Shuffle_shuffle_layoutRight, layoutRightResId);
            }
            {
                int stackFrom = styledAttrs.getInteger(R.styleable.Shuffle_shuffle_stackFrom, STACK_FROM_BOTTOM);
                this.stackFromTop = stackFrom == STACK_FROM_TOP;
            }
            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
