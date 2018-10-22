package com.example.malaligowda.billreminder;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback{

    private RecyclerItemTouchHelperListener listener;
    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener !=null)
            listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());

    }
    @Override
    public void clearView(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder){
        View forground = ((displayAdapter.ViewHolder)viewHolder).viewforground;
        getDefaultUIUtil().clearView(forground);
    }
    @Override

    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                 View forground = ((displayAdapter.ViewHolder)viewHolder).viewforground;
                getDefaultUIUtil().onDraw(c,recyclerView,forground,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View forground = ((displayAdapter.ViewHolder) viewHolder).viewforground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, forground, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null) {
            View forground = ((displayAdapter.ViewHolder) viewHolder).viewforground;
            getDefaultUIUtil().onSelected(forground);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
    
}
