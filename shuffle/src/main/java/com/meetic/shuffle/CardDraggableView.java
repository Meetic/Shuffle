package com.meetic.shuffle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.meetic.dragueur.DraggableView;

public class CardDraggableView extends DraggableView {

    int colorLeft;
    int colorRight;

    @Nullable View overlayLeft;
    @Nullable View overlayRight;
    ViewGroup overlay;

    int layoutLeftResId;
    int layoutRightResId;

    ViewGroup content;

    private ShuffleSettings settings;
    private Shuffle host;
    private Shuffle.OnScrollChangeListener verticalScrollListener;

    public CardDraggableView(Context context) {
        super(context);
    }

    public CardDraggableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardDraggableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewGroup getOverlayView() {
        return overlay;
    }

    public ViewGroup getContent() {
        return content;
    }

    public void setOverlayColors(int colorLeft, int colorRight) {
        this.colorLeft = colorLeft;
        this.colorRight = colorRight;
    }

    @Override
    public void reset() {
        ViewCompat.setAlpha(content, 1f);
        ViewCompat.setAlpha(overlay, 0f);
    }

    public void setOverlayLayouts(int layoutLeftResId, int layoutRightResId) {
        if (layoutLeftResId != 0) {
            this.layoutLeftResId = layoutLeftResId;
            if (overlayLeft != null) {
                overlay.removeView(overlayLeft);
            }
            overlayLeft = LayoutInflater.from(getContext()).inflate(this.layoutLeftResId, overlay, false);
            if (overlayLeft != null) {
                overlay.addView(overlayLeft);
                setOverlayLeftAlpha(0f);
            }
        }

        if (layoutRightResId != 0) {
            this.layoutRightResId = layoutRightResId;
            if (overlayRight != null) {
                overlay.removeView(overlayRight);
            }
            overlayRight = LayoutInflater.from(getContext()).inflate(this.layoutRightResId, overlay, false);
            if (overlayRight != null) {
                overlay.addView(overlayRight);
                setOverlayRightAlpha(0f);
            }
        }
    }

    public int getColorLeft() {
        return colorLeft;
    }

    public int getColorRight() {
        return colorRight;
    }

    @Nullable
    public View getOverlayLeft() {
        return overlayLeft;
    }

    public void setOverlayLeftAlpha(float alpha) {
        if (overlayLeft != null) {
            ViewCompat.setAlpha(overlayLeft, alpha);
        }
    }

    public void setOverlayRightAlpha(float alpha) {
        if (overlayRight != null) {
            ViewCompat.setAlpha(overlayRight, alpha);
        }
    }

    @Nullable
    public View getOverlayRight() {
        return overlayRight;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.shuffle_card, this);
        content = (ViewGroup) findViewById(R.id.content);
        overlay = (ViewGroup) findViewById(R.id.overlay);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        if (settings.isHorizontal())
            return handleTouch(event);
        return super.onTouchEvent(event);
    }

    /*
    handleTouch-related only vars
     */
    private float x1, x2, y1, y2, dx, dy;
    private int sensibility = 100;
    private String direction = "none";

    /**
     * Handle touch when scroll is locked to only horizontal (to allow vertical scroll on child views)
     * @param event The motion event
     * @return trus is the event is handled
     */
    private boolean handleTouch(MotionEvent event) {
        if (super.isDraggable() && !super.isAnimating()) {
            final int action = MotionEventCompat.getActionMasked(event);

            verticalScrollListener.onScrollChange(this, event.getX(), event.getY(), x1, y1);

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    switch (direction) {
                        case "right":
                            host.swipeRight(500);
                            break;
                        case "left":
                            host.swipeLeft(500);
                            break;
                        case "none":
                            if (getViewAnimator() != null)
                                getViewAnimator().animateToOrigin(this, 500);
                            break;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    x2 = event.getX();
                    y2 = event.getY();
                    dx = x2-x1;
                    dy = y2-y1;

                    float newMotionX = event.getRawX();
                    float newMotionY = event.getRawY();

                    float diffMotionX = newMotionX - motionXOrigin;
                    float diffMotionY = newMotionY - motionYOrigin;

                    if (!super.isInlineMove()) {
                        ViewCompat.setTranslationY(this, getOriginalViewY() + diffMotionY);
                    }
                    ViewCompat.setTranslationX(this, getOriginalViewX() + diffMotionX);

                    this.update();

                    if(Math.abs(dx) > (Math.abs(dy) + sensibility/3)) {
                        if (diffMotionX > 0) {
                            if (diffMotionX > sensibility)
                                direction = "right";
                            else
                                direction = "none";
                        } else if (diffMotionX < 0) {
                            if (diffMotionX < -sensibility)
                                direction = "left";
                            else
                                direction = "none";
                        } else {
                            direction = "none";
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    switch (direction) {
                        case "right":
                            host.swipeRight(500);
                            break;
                        case "left":
                            host.swipeLeft(500);
                            break;
                        case "none":
                            if (getViewAnimator() != null)
                                getViewAnimator().animateToOrigin(this, 500);
                            break;
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    public void setSettings(ShuffleSettings shuffleSettings) {
        this.settings = shuffleSettings;
    }

    public void setHost(Shuffle shuffle) {
        this.host = shuffle;
    }

    public void setVerticalScrollListener(Shuffle.OnScrollChangeListener verticalScrollListener) {
        this.verticalScrollListener = verticalScrollListener;
    }
}
