package nuce.tatv.noteeverything.Helpers;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerItemTouchHelperForNote extends ItemTouchHelper.Callback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelperForNote(RecyclerItemTouchHelperListener listener) {
        this.listener = listener;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlag, swipeFlag);
    }
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();

    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        listener.onMove(viewHolder, viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null){
            listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

}
