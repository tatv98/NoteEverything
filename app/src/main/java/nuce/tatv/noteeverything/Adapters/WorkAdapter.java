package nuce.tatv.noteeverything.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import nuce.tatv.noteeverything.Activities.EditWorkActivity;
import nuce.tatv.noteeverything.Activities.MainActivity;
import nuce.tatv.noteeverything.Fragments.WorkFragment;
import nuce.tatv.noteeverything.Models.Note;
import nuce.tatv.noteeverything.Models.Work;
import nuce.tatv.noteeverything.R;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.MyViewHolder>{
    private Context context;
    private int layout;
    private List<Work> listWork;

    public WorkAdapter(Context context, int layout , List<Work> listWork) {
        this.context = context;
        this.layout = layout;
        this.listWork = listWork;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vItem = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new MyViewHolder(vItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Work work = listWork.get(position);
        holder.tvTitle.setText(work.getWorkTitle());
        holder.tvPlace.setText(work.getWorkPlace());
        holder.tvDeadline.setText(work.getWorkDeadline());
        holder.tvStatus.setText(work.getWorkStatus());
        if (work.getWorkStatus().equals("Đã hoàn thành")){
            holder.tvStatus.setTextColor(Color.parseColor("#FFFF5722"));
        }

        holder.vgWorkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWork = new Intent(context, EditWorkActivity.class);
                Bundle bundleWork = new Bundle();
                bundleWork.putInt("work_id", work.getWorkId());
                bundleWork.putString("work_title", work.getWorkTitle());
                bundleWork.putString("work_place", work.getWorkPlace());
                bundleWork.putString("work_deadline", work.getWorkDeadline());
                bundleWork.putString("work_status", work.getWorkStatus());
                intentWork.putExtras(bundleWork);
                ((MainActivity)context).startActivityForResult(intentWork, WorkFragment.REQUEST_CODE_EDIT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listWork.size();
    }

    public void removeWork(int position){
        listWork.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreWork(Work work, int position){
        listWork.add(position, work);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvPlace, tvStatus, tvDeadline;
        public CardView vgWorkItem;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            vgWorkItem = itemView.findViewById(R.id.vgWorkItem);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
        }
    }
}
