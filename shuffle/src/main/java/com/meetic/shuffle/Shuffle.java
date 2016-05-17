package com.meetic.shuffle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.meetic.dragueur.Direction;
import com.meetic.dragueur.DraggableView;
import com.meetic.dragueur.ViewAnimator;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Shuffle extends FrameLayout {

    ShuffleSettings shuffleSettings;

    @Nullable
    Adapter shuffleAdapter;
    int adapterPosition = 0;

    SparseArray<List<ViewHolder>> pool = new SparseArray<>();

    LinkedList<CardDraggableView> draggableViews = new LinkedList<>();
    CardDraggableView currentDraggableView;
    ShuffleViewAnimator viewAnimator;

    Set<Listener> listeners = new HashSet<>();

    public Shuffle(Context context) {
        this(context, null);
    }

    public Shuffle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Shuffle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public ShuffleSettings getShuffleSettings() {
        return shuffleSettings;
    }

    @Nullable
    public Adapter getShuffleAdapter() {
        return shuffleAdapter;
    }

    public void setShuffleAdapter(@Nullable Adapter shuffleAdapter) {
        this.shuffleAdapter = shuffleAdapter;
        if (shuffleAdapter != null) {
            shuffleAdapter.shuffleWeakReference = new WeakReference<>(this);
        }
        notifyDataSetChanged();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public ShuffleViewAnimator getViewAnimator() {
        return viewAnimator;
    }

    public void setViewAnimator(ShuffleViewAnimator viewAnimator) {
        viewAnimator.setShuffle(this);
        this.viewAnimator = viewAnimator;
        updateDraggableViews();
    }

    public void swipeRight(int duration) {
        if (viewAnimator != null && currentDraggableView != null) {
            viewAnimator.animateExit(currentDraggableView, Direction.RIGHT, duration);
        }
    }

    public void swipeLeft(int duration) {
        if (viewAnimator != null && currentDraggableView != null) {
            viewAnimator.animateExit(currentDraggableView, Direction.LEFT, duration);
        }
    }

    public int getCurrentAdapterPosition() {
        return adapterPosition;
    }

    public CardDraggableView getDraggableView(int position) {
        if (position < draggableViews.size()) {
            return draggableViews.get(position);
        }
        return null;
    }

    public CardDraggableView getLastDraggableView() {
        return draggableViews.getLast();
    }

    public void enable(boolean enable) {
        if (enable) {
            DraggableView firstDraggableView = draggableViews.getFirst();
            if (firstDraggableView != null) {
                firstDraggableView.setDraggable(true);
            }
            ViewCompat.setAlpha(this, 1f);
        } else {
            DraggableView firstDraggableView = draggableViews.getFirst();
            if (firstDraggableView != null) {
                firstDraggableView.setDraggable(false);
            }
            ViewCompat.setAlpha(this, 0.5f);
        }
    }

    public void restartShuffling() {
        viewAnimator.animateRestartShuffling(new ShuffleViewAnimator.RestartListener() {
            @Override
            public void animationStarted() {

            }

            @Override
            public void animationMiddle() {
                adapterPosition = 0;
                updateAdapter();
            }

            @Override
            public void animationEnd() {

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listeners.clear();
        pool.clear();
        draggableViews.clear();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onViewInflated();
    }

    protected void onViewInflated() {
        int numberOfCards = shuffleSettings.getNumberOfDisplayedCards();
        for (int i = numberOfCards - 1; i >= 0; i--) {
            CardDraggableView draggableView = generateDraggableView();
            draggableView.setDraggable(false);
            updateDraggableView(draggableView);
            draggableViews.addFirst(draggableView);
            addView(draggableView);
        }
        notifyDataSetChanged();
    }

    void notifyDataSetChanged() {
        updateAdapter();
        setFirstCardDraggable();
    }

    @VisibleForTesting
    ViewGroup getViewParent() {
        return (ViewGroup) getParent();
    }

    @VisibleForTesting
    CardDraggableView generateDraggableView() {
        CardDraggableView cardDraggableView = (CardDraggableView) LayoutInflater.from(getContext()).inflate(R.layout.draggable_view, this, false);
        return cardDraggableView;
    }

    List<ViewHolder> getViewHolderListForType(int viewType) {
        List<ViewHolder> list = pool.get(viewType);
        if (list == null) {
            list = new ArrayList<>();
            pool.put(viewType, list);
        }
        return list;
    }

    @VisibleForTesting
    void updateDraggableViews() {
        for (CardDraggableView draggableView : draggableViews) {
            updateDraggableView(draggableView);
        }
    }

    void updateDraggableView(CardDraggableView draggableView) {
        draggableView.setVertical(shuffleSettings.isVertical());
        draggableView.setRotationEnabled(shuffleSettings.isRotationEnabled());
        draggableView.setRotationValue(shuffleSettings.getRotation());
        draggableView.setInlineMove(shuffleSettings.isInlineMove());
        draggableView.setMinVelocity(shuffleSettings.getMinVelocity());
        draggableView.setOverlayColors(shuffleSettings.getColorLeft(), shuffleSettings.getColorRight());
        draggableView.setOverlayLayouts(shuffleSettings.getLayoutLeftResId(), shuffleSettings.getLayoutRightResId());
        draggableView.setViewAnimator(viewAnimator);
    }

    void dispatchAdapterPositionToListeners() {
        for (Listener listener : listeners) {
            listener.onViewChanged(adapterPosition);
        }
    }

    void dispatchScrollStartedToListeners() {
        for (Listener listener : listeners) {
            listener.onScrollStarted();
        }
    }

    void dispatchExitedToListeners(DraggableView draggableView, Direction direction) {
        for (Listener listener : listeners) {
            listener.onViewExited(draggableView, direction);
        }
    }

    void dispatchScrollFinishedToListeners() {
        for (Listener listener : listeners) {
            listener.onScrollFinished();
        }
    }

    void dispatchViewScrolledToListeners(DraggableView draggableView, float percentX, float percentY) {
        for (Listener listener : listeners) {
            listener.onViewScrolled(draggableView, percentX, percentY);
        }
    }

    @VisibleForTesting
    void updateAdapter() {
        checkScrollStarted();
        if (shuffleAdapter != null) {
            int itemCount = shuffleAdapter.getItemCount();
            int numberOfCards = shuffleSettings.getNumberOfDisplayedCards();
            for (int i = 0; i < numberOfCards; i++) {
                int position = adapterPosition + i;
                if(position < draggableViews.size()) {
                    CardDraggableView draggableView = draggableViews.get(i);
                    ViewGroup draggableViewContent = draggableView.getContent();
                    if (position < itemCount) {
                        draggableView.setVisibility(VISIBLE);
                        int type = shuffleAdapter.getItemViewType(position);

                        List<ViewHolder> viewHolderList = getViewHolderListForType(type);
                        ViewHolder viewHolder = getNextUnusedViewHolder(viewHolderList);

                        if (viewHolder == null) {
                            viewHolder = shuffleAdapter.onCreateViewHolder(draggableView, type);
                            viewHolderList.add(viewHolder);
                        }

                        draggableViewContent.removeAllViews();
                        draggableViewContent.addView(viewHolder.itemView);

                        viewHolder.position = position;

                        shuffleAdapter.onBindViewHolder(viewHolder, position);
                    } else {
                        draggableView.setVisibility(GONE);
                    }
                }
            }
        }
    }

    void setFirstCardDraggable() {
        currentDraggableView = draggableViews.getFirst();
        currentDraggableView.setDraggable(true);
        currentDraggableView.reset();
        currentDraggableView.setDragListener(new DraggableView.DraggableViewListener() {
            @Override
            public void onDrag(DraggableView draggableView, float percentX, float percentY) {
                viewAnimator.updateViewsPositions(percentX, percentY);
                dispatchViewScrolledToListeners(draggableView, percentX, percentY);
            }

            @Override
            public void onDraggedStarted(DraggableView draggableView, Direction direction) {

            }

            @Override
            public void onDraggedEnded(final DraggableView draggableView, final Direction direction) {
                draggableView.setDraggable(false);
                moveFirstCardToStack();
                viewAnimator.animateViewStackFrom(new ViewAnimator.Listener() {
                    @Override
                    public void animationStarted() {
                    }

                    @Override
                    public void animationEnd() {
                        dispatchExitedToListeners(draggableView, direction);
                        setFirstCardDraggable();
                    }
                }, direction);
            }

            @Override
            public void onDragCancelled(DraggableView draggableView) {

            }

        });
        viewAnimator.updateViewsPositions(0, 0);
    }

    void checkScrollStarted() {
        int itemCount = shuffleAdapter != null ? shuffleAdapter.getItemCount() : 0;
        if (itemCount != 0 && adapterPosition == 0) {
            dispatchScrollStartedToListeners();
        }
    }

    void checkScrollFinished() {
        int itemCount = shuffleAdapter != null ? shuffleAdapter.getItemCount() : 0;
        if (itemCount != 0 && adapterPosition >= itemCount) {
            dispatchScrollFinishedToListeners();
        }
    }

    void moveFirstCardToStack() {
        //replace views
        CardDraggableView firstCard = draggableViews.pop();
        draggableViews.addLast(firstCard);

        //reorganize views Z order
        removeAllViews();
        int numberOfCards = shuffleSettings.getNumberOfDisplayedCards();
        for (int i = numberOfCards - 1; i >= 0; i--) {
            addView(draggableViews.get(i));
        }

        adapterPosition++;
        updateAdapter();
        dispatchAdapterPositionToListeners();

        checkScrollFinished();
    }

    @Nullable
    static ViewHolder getNextUnusedViewHolder(List<ViewHolder> list) {
        for (ViewHolder viewHolder : list) {
            if (viewHolder.position == -1) {
                return viewHolder;
            }
        }
        return null;
    }

    private void initialize(Context context, AttributeSet attrs) {
        this.shuffleSettings = new ShuffleSettings();
        this.shuffleSettings.handleAttributes(context, attrs);
        setViewAnimator(new ShuffleViewAnimator());
    }

    public interface Listener {
        void onViewChanged(int position);

        void onScrollStarted();

        void onScrollFinished();

        void onViewExited(DraggableView draggableView, Direction direction);

        void onViewScrolled(DraggableView draggableView, float percentX, float percentY);
    }

    public static abstract class Adapter<V extends ViewHolder> {

        public static final int TYPE_DEFAULT = 0;
        WeakReference<Shuffle> shuffleWeakReference;

        public int getItemViewType(int position) {
            return TYPE_DEFAULT;
        }

        public abstract V onCreateViewHolder(ViewGroup viewGroup, int type);

        public abstract void onBindViewHolder(V viewHolder, int position);

        public abstract int getItemCount();

        public final void notifyDataSetChanged() {
            if (shuffleWeakReference != null) {
                Shuffle shuffle = shuffleWeakReference.get();
                if (shuffle != null) {
                    shuffle.notifyDataSetChanged();
                }
            }
        }
    }

    public static class ViewHolder {
        public View itemView;
        int position;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

}