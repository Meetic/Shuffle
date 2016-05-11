package com.meetic.shuffle;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;

import com.meetic.dragueur.Direction;

public class ShuffleViewAnimatorOnSecondCard extends ShuffleViewAnimator {

    @Override
    public boolean animateExit(@NonNull CardDraggableView draggableView, Direction direction, int duration) {
        resetCard(shuffle.getDraggableView(1), duration);
        return super.animateExit(draggableView, direction, duration);
    }

    public void resetCard(final CardDraggableView draggableView, int duration) {
        final int delay = duration;
        ViewCompat.animate(draggableView.getOverlayView())
            .withLayer()
            .alpha(0)
            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    draggableView.setOverlayLeftAlpha(0f);
                    draggableView.setOverlayRightAlpha(0f);
                }
            })
            .setStartDelay(delay);
        ViewCompat.animate(draggableView.getContent())
            .withLayer()
            .alpha(1)
            .setStartDelay(delay);

    }

    @Override
    public void update(CardDraggableView draggableViewFront, float percentX, float percentY) {
        CardDraggableView draggableViewBottom = shuffle.getDraggableView(1);

        if (percentX <= 0) {
            draggableViewBottom.setOverlayLeftAlpha(1f);
            draggableViewBottom.setOverlayRightAlpha(0f);
        } else {
            draggableViewBottom.setOverlayLeftAlpha(0f);
            draggableViewBottom.setOverlayRightAlpha(1f);
        }

        float percent = Math.abs(percentX);
        percent = (float) Math.pow(percent, 10);
        percent = Math.min(0.09f, percent);

        if(percent == 0f){
            ViewCompat.setAlpha(draggableViewBottom.getOverlayView(), 0f);
            ViewCompat.animate(draggableViewBottom.getContent()).withLayer().setDuration(100).alpha(1f);
        } else {
            ViewCompat.animate(draggableViewBottom.getContent()).cancel();
            ViewCompat.setAlpha(draggableViewBottom.getContent(), percent);
            ViewCompat.setAlpha(draggableViewBottom.getOverlayView(), 1f);
        }
    }
}
