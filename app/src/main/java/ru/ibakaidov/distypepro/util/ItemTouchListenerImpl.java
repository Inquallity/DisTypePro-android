package ru.ibakaidov.distypepro.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author Maksim Radko
 */
public class ItemTouchListenerImpl implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;

    public ItemTouchListenerImpl(@NonNull Context ctx, @NonNull GestureDetector.OnGestureListener listener) {
        gestureDetector = new GestureDetector(ctx, listener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        //Not implemented
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //Not implemented
    }
}
