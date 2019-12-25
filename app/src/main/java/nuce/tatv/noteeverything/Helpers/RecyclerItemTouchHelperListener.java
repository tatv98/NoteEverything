package nuce.tatv.noteeverything.Helpers;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerItemTouchHelperListener {
    void onMove(RecyclerView.ViewHolder viewHolder, int oldPosition, int newPosition);
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
