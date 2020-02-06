package nuce.tatv.noteeverything.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nuce.tatv.noteeverything.Activities.AddExpenseActivity;
import nuce.tatv.noteeverything.Activities.AddWorkActivity;
import nuce.tatv.noteeverything.Activities.MainActivity;
import nuce.tatv.noteeverything.Adapters.NoteAdapter;
import nuce.tatv.noteeverything.Adapters.WorkAdapter;
import nuce.tatv.noteeverything.Helpers.RecyclerItemTouchHelperForWork;
import nuce.tatv.noteeverything.Helpers.RecyclerItemTouchHelperListener;
import nuce.tatv.noteeverything.Models.Note;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.Models.Work;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import nuce.tatv.noteeverything.Utils.DateFormat;
import retrofit2.Call;
import retrofit2.Callback;

public class WorkFragment extends Fragment implements RecyclerItemTouchHelperListener, SwipeRefreshLayout.OnRefreshListener {
    public static final int REQUEST_CODE_ADD = 1122;
    public static final int REQUEST_CODE_EDIT = 1133;
    private View vWork;
    private ArrayList<Work> listWork;
    private RecyclerView vgListWork;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private CoordinatorLayout rootLayout;
    private WorkAdapter workAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vWork = inflater.inflate(R.layout.work_fragment, container, false);
        init();
        return vWork;
    }

    private void init() {
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Work");

        listWork = new ArrayList<>();
        rootLayout = vWork.findViewById(R.id.rootLayout);
        vgListWork = vWork.findViewById(R.id.vgListWork);
        mySwipeRefreshLayout = vWork.findViewById(R.id.mySwipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(this);

        getAllWork();
        ItemTouchHelper.Callback itemTouchHelper = new RecyclerItemTouchHelperForWork(this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(vgListWork);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itemExpense = menu.add(1, R.id.itemAddWork, 1, "Thêm công việc");
        itemExpense.setIcon(R.drawable.ic_work);
        itemExpense.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemAddWork:
                startActivityForResult(new Intent(getActivity(), AddWorkActivity.class), REQUEST_CODE_ADD);
                break;
        }
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT){
            if (resultCode == Activity.RESULT_OK) {
                getAllWork();
            }
        }
    }

    public void  getAllWork(){
        listWork.clear();
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Response> call = getDataService.getWork(MainActivity.USER_NAME);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                for (Work work: response.body().getData().getWork()){
                    listWork.add(work);
                    Log.d("1111", work.toString());
                }
                workAdapter = new WorkAdapter(getActivity(), R.layout.work_custom_row_item, listWork);
                vgListWork.setAdapter(workAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                vgListWork.setLayoutManager(layoutManager);
                vgListWork.setItemAnimator(new DefaultItemAnimator());
                workAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        getAllWork();
        mySwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMove(RecyclerView.ViewHolder viewHolder, int oldPosition, int newPosition) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof WorkAdapter.MyViewHolder){
            String work_title = listWork.get(viewHolder.getAdapterPosition()).getWorkTitle();
            final Work work_delete = listWork.get(viewHolder.getAdapterPosition());
            final int work_position = viewHolder.getAdapterPosition();
            workAdapter.removeWork(work_position);
            //---remove work
            final GetDataService svDelete = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            final Call<Response> callDeleteWork = svDelete.deleteWork(work_delete.getWorkId());
            callDeleteWork.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                }
            });

            Snackbar snackbar = Snackbar.make(rootLayout, work_title + " removed from works!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workAdapter.restoreWork(work_delete, work_position);
                    final GetDataService svRestore = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<Response> callRestoreWork = svRestore.postWork(work_delete);
                    callRestoreWork.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                        }
                    });
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
