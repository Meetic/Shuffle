package com.meetic.shuffle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;

import com.meetic.dragueur.Direction;
import com.meetic.dragueur.DraggableView;
import com.meetic.dragueur.ExitViewAnimator;

public class ShuffleViewAnimator extends ExitViewAnimator<CardDraggableView> {

    @Nullable
    protected Shuffle shuffle;

    public ShuffleViewAnimator() {
    }

    public void setShuffle(@NonNull Shuffle shuffle) {
        this.shuffle = shuffle;
    }

    @Override
    public boolean animateToOrigin(@NonNull CardDraggableView draggableView, int duration) {
        boolean animating = super.animateToOrigin(draggableView, duration);
        if (animating) {
            ViewCompat.animate(draggableView.getOverlayView()).alpha(0f).setDuration(duration);
        }
        return animating;
    }

    public boolean animateViewStackFrom(@NonNull Listener listener, Direction direction) {
        if (shuffle != null) {
            if (shuffle.getShuffleSettings().isVertical()) {
                switch (direction) {
                    case TOP:
                        return animateViewStackScaleUp(listener);
                    case BOTTOM:
                        return animateViewStackScaleUp(listener);
                }
            } else {
                switch (direction) {
                    case LEFT:
                        return animateViewStackScaleUp(listener);
                    case RIGHT:
                        return animateViewStackFromRight(listener);
                }
            }
        }
        return false;
    }

    public boolean animateViewStackScaleUp(@NonNull final Listener listener) {
        if (shuffle != null) {

            final DraggableView lastCard = shuffle.getLastDraggableView();

            { //reset
                ViewCompat.setRotation(lastCard, 0);
                ViewCompat.setTranslationX(lastCard, 0);
                lastCard.reset();
            }

            int numberOfCards = shuffle.getShuffleSettings().getNumberOfDisplayedCards();
            int position = numberOfCards - 1;
            float scale = shuffle.getShuffleSettings().getScaleForPosition(position);
            float translationY = shuffle.getShuffleSettings().getTranslationYForPosition(position);

            ViewCompat.setScaleX(lastCard, 0.5f);
            ViewCompat.setScaleY(lastCard, 0.5f);
            ViewCompat.setTranslationY(lastCard, 0);
            ViewCompat.animate(lastCard)
                .scaleX(scale)
                .scaleY(scale)
                .translationY(translationY)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        listener.animationEnd();
                    }
                })
                .setStartDelay(100)
                .setDuration(shuffle.getShuffleSettings().getAnimationReturnCardDuration());

            return true;
        } else {
            return false;
        }
    }

    public boolean animateViewStackFromRight(@NonNull final Listener listener) {
        if (shuffle != null) {
            final DraggableView lastCard = shuffle.getLastDraggableView();

            { //reset
                final int numberOfCards = shuffle.getShuffleSettings().getNumberOfDisplayedCards();
                final int position = numberOfCards - 1;

                float scale = shuffle.getShuffleSettings().getScaleForPosition(position);
                ViewCompat.setScaleX(lastCard, scale);
                ViewCompat.setScaleY(lastCard, scale);
                lastCard.reset();

                float translationY = shuffle.getShuffleSettings().getTranslationYForPosition(position);
                ViewCompat.setTranslationY(lastCard, translationY);

                ViewCompat.setRotation(lastCard, 0);
                listener.animationStarted();
            }

            float translationX = lastCard.parentWidth;
            ViewCompat.setTranslationX(lastCard, translationX);
            ViewCompat.animate(lastCard)
                .translationX(0)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        listener.animationEnd();
                    }
                })
                .setDuration(shuffle.getShuffleSettings().getAnimationReturnCardDuration());

            return true;
        } else {
            return false;
        }

    }

    public void updateViewsPositions(float percentX, float percentY) {
        if (shuffle != null) {
            float percent = shuffle.getShuffleSettings().isVertical() ? percentY : percentX;
            int numberOfCards = shuffle.getShuffleSettings().getNumberOfDisplayedCards();
            for (int i = 1; i < numberOfCards; ++i) {
                DraggableView view = shuffle.getDraggableView(i);

                float myScale = shuffle.getShuffleSettings().getScaleForPosition(i);
                float nextScale = shuffle.getShuffleSettings().getScaleForPosition(i - 1);
                float percentAbs = myScale + (nextScale - myScale) * Math.abs(percent);
                ViewCompat.setScaleX(view, percentAbs);
                ViewCompat.setScaleY(view, percentAbs);

                float myTranslationY = shuffle.getShuffleSettings().getTranslationYForPosition(i);
                float nextTranslationY = shuffle.getShuffleSettings().getTranslationYForPosition(i - 1);
                float translationY = myTranslationY - Math.abs(percent) * (myTranslationY - nextTranslationY);
                ViewCompat.setTranslationY(view, translationY);

                float myTranslationX = shuffle.getShuffleSettings().getTranslationXForPosition(i);
                float nextTranslationX = shuffle.getShuffleSettings().getTranslationXForPosition(i - 1);
                float translationX = myTranslationX - Math.abs(percent) * (myTranslationX - nextTranslationX);
                ViewCompat.setTranslationX(view, translationX);
            }
        }
    }

    @Override
    public void update(CardDraggableView draggableView, float percentX, float percentY) {
        super.update(draggableView, percentX, percentY);

        if (percentX < 0) {
            if (draggableView.getOldPercentX() >= 0) { //percent changed sign
                draggableView.getOverlayView().setBackgroundColor(draggableView.getColorLeft());
                draggableView.setOverlayLeftAlpha(1f);
                draggableView.setOverlayRightAlpha(0f);
            }
        } else {
            if (draggableView.getOldPercentX() <= 0) {
                //percent changed sign
                draggableView.getOverlayView().setBackgroundColor(draggableView.getColorRight());
                draggableView.setOverlayLeftAlpha(0f);
                draggableView.setOverlayRightAlpha(1f);
            }
        }

        ViewCompat.setAlpha(draggableView.getOverlayView(), Math.abs(percentX));
    }
}
