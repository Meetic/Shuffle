package com.meetic.shuffle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.meetic.dragueur.Direction;
import com.meetic.dragueur.DraggableView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ShuffleTest {

    public @Rule MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ViewGroup parent;
    @Mock ShuffleViewAnimator viewAnimator;
    @Mock ShuffleSettings shuffleSettings;
    @Spy Context context = RuntimeEnvironment.application;

    Shuffle shuffle;

    @Before
    public void setUp() throws Exception {
        shuffle = spy(new Shuffle(context));

        shuffle.viewAnimator = viewAnimator;
        shuffle.shuffleSettings = shuffleSettings;

        doReturn(parent).when(shuffle).getViewParent();
        doReturn(mock(CardDraggableView.class)).when(shuffle).generateDraggableView();
        doNothing().when(shuffle).addView(any(View.class));
        doNothing().when(shuffle).removeAllViews();
    }

    @Test
    public void testSwipeRight() throws Exception {
        //Given
        shuffle.currentDraggableView = mock(CardDraggableView.class);
        //When
        shuffle.swipeRight(1000);
        //Then
        verify(viewAnimator).animateExit(any(CardDraggableView.class), eq(Direction.RIGHT), eq(1000));
    }

    @Test
    public void testSwipeLeft() throws Exception {
        //Given
        shuffle.currentDraggableView = mock(CardDraggableView.class);
        //When
        shuffle.swipeLeft(1000);
        //Then
        verify(viewAnimator).animateExit(any(CardDraggableView.class), eq(Direction.LEFT), eq(1000));
    }

    @Test
    public void testOnViewInflated() throws Exception {
        //Given
        given(shuffleSettings.getNumberOfDisplayedCards()).willReturn(3);
        //When
        shuffle.onViewInflated();
        //Then
        assertThat(shuffle.draggableViews).hasSize(3);
        verify(shuffle, times(3)).updateDraggableView(any(CardDraggableView.class));
        verify(shuffle, times(3)).addView(any(View.class));
        verify(shuffle, times(1)).updateAdapter();
        verify(shuffle, times(1)).setFirstCardDraggable();
    }

    @Test
    public void testGetViewHolderListForType() throws Exception {
    }

    @Test
    public void testGetNextUnusedViewHolder() throws Exception {
        //Given
        List<Shuffle.ViewHolder> viewHolderList = Arrays.asList(
            mockShuffleViewHolder(1),
            mockShuffleViewHolder(-1),
            mockShuffleViewHolder(2)
        );
        //When
        Shuffle.ViewHolder returnedHolder = Shuffle.getNextUnusedViewHolder(viewHolderList);
        //Then
        assertThat(returnedHolder.position).isEqualTo(-1);
    }

    @Test
    public void testGetNextUnusedViewHolder_isNull() throws Exception {
        //Given
        List<Shuffle.ViewHolder> viewHolderList = Arrays.asList(
            mockShuffleViewHolder(1),
            mockShuffleViewHolder(2),
            mockShuffleViewHolder(3)
        );
        //When
        Shuffle.ViewHolder returnedHolder = Shuffle.getNextUnusedViewHolder(viewHolderList);
        //Then
        assertThat(returnedHolder).isNull();
    }

    @Test
    @Ignore
    public void testUpdateDraggableViews() throws Exception {

    }

    @Test
    public void testUpdateAdapter_firstTime() throws Exception {
        //Given
        given(shuffleSettings.getNumberOfDisplayedCards()).willReturn(3);
        shuffle.adapterPosition = 0;

        CardDraggableView draggableView1 = mockDraggableView();
        CardDraggableView draggableView2 = mockDraggableView();
        CardDraggableView draggableView3 = mockDraggableView();
        shuffle.draggableViews.addAll(Arrays.asList(draggableView1, draggableView2, draggableView3));

        Shuffle.Adapter shuffleAdapter = mock(Shuffle.Adapter.class);
        Shuffle.ViewHolder mockHolder = mock(Shuffle.ViewHolder.class);
        {
            given(shuffleAdapter.getItemCount()).willReturn(10);
            given(shuffleAdapter.getItemViewType(anyInt())).willReturn(1);
            mockHolder.itemView = mock(View.class);
            given(shuffleAdapter.onCreateViewHolder(any(ViewGroup.class), anyInt())).willReturn(mockHolder);
        }
        //When
        shuffle.setShuffleAdapter(shuffleAdapter);
        //Then
        verify(shuffleAdapter).onCreateViewHolder(eq(draggableView1), anyInt());
        verify(shuffleAdapter).onCreateViewHolder(eq(draggableView2), anyInt());
        verify(shuffleAdapter).onCreateViewHolder(eq(draggableView3), anyInt());
        verify(draggableView1.getContent()).removeAllViews();
        verify(draggableView1.getContent()).addView(eq(mockHolder.itemView));
        assertThat(mockHolder.position).isEqualTo(2);
        verify(shuffleAdapter).onBindViewHolder(eq(mockHolder), eq(0));
        verify(shuffleAdapter).onBindViewHolder(eq(mockHolder), eq(1));

    }

    @Test
    public void testSetFirstCardDraggable() throws Exception {
        //Given
        CardDraggableView draggableView1 = mockDraggableView();
        CardDraggableView draggableView2 = mockDraggableView();
        CardDraggableView draggableView3 = mockDraggableView();
        shuffle.draggableViews.addAll(Arrays.asList(draggableView1, draggableView2, draggableView3));

        //When
        shuffle.setFirstCardDraggable();

        //Then
        verify(draggableView1).setDraggable(eq(true));
        verify(draggableView1).reset();
        verify(draggableView1).setDragListener(any(DraggableView.DraggableViewListener.class));
        verify(viewAnimator).updateViewsPositions(eq(0.0f), eq(0.0f));
    }

    @Test
    public void testMoveFirstCardToStack() throws Exception {
        //Give
        final List<View> viewsList = new ArrayList<>();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                viewsList.add((View) invocation.getArguments()[0]);
                return null;
            }
        }).when(shuffle).addView(any(View.class));

        shuffle.adapterPosition = 4;

        given(shuffleSettings.getNumberOfDisplayedCards()).willReturn(3);

        CardDraggableView draggableView1 = mockDraggableView();
        CardDraggableView draggableView2 = mockDraggableView();
        CardDraggableView draggableView3 = mockDraggableView();
        shuffle.draggableViews.addAll(Arrays.asList(draggableView1, draggableView2, draggableView3));

        //When
        shuffle.moveFirstCardToStack();
        //Then
        assertThat(shuffle.draggableViews.get(0)).isEqualTo(draggableView2);
        assertThat(shuffle.draggableViews.get(1)).isEqualTo(draggableView3);
        assertThat(shuffle.draggableViews.get(2)).isEqualTo(draggableView1);

        verify(shuffle).removeAllViews();
        assertThat(viewsList.get(0)).isEqualTo(draggableView1);
        assertThat(viewsList.get(1)).isEqualTo(draggableView3);
        assertThat(viewsList.get(2)).isEqualTo(draggableView2);

        assertThat(shuffle.adapterPosition).isEqualTo(5);
    }

    CardDraggableView mockDraggableView(){
        CardDraggableView draggableView = mock(CardDraggableView.class);
        given(draggableView.getContent()).willReturn(mock(ViewGroup.class));
        return draggableView;
    }

    Shuffle.ViewHolder mockShuffleViewHolder(int position){
        Shuffle.ViewHolder holder = new Shuffle.ViewHolder(mock(View.class));
        holder.position = position;
        return holder;
    }
}