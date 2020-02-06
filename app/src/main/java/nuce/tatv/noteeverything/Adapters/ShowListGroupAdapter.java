package nuce.tatv.noteeverything.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nuce.tatv.noteeverything.Models.Group;
import nuce.tatv.noteeverything.R;

public class ShowListGroupAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<Group> listGroup;
    ViewHolderGroup viewHolderGroup;

    public ShowListGroupAdapter(Context context, int layout, List<Group> listGroup) {
        this.context = context;
        this.layout = layout;
        this.listGroup = listGroup;
    }

    @Override
    public int getCount() {
        return listGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return listGroup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listGroup.get(position).getmThumbnail();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            viewHolderGroup = new ViewHolderGroup();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);
            viewHolderGroup.ivGroup = view.findViewById(R.id.ivGroup);
            viewHolderGroup.tvGroup = view.findViewById(R.id.tvGroup);
            view.setTag(viewHolderGroup);
        }else{
            viewHolderGroup = (ViewHolderGroup) view.getTag();
        }
        Group group = listGroup.get(position);

        viewHolderGroup.ivGroup.setImageResource(group.getmThumbnail());
        viewHolderGroup.tvGroup.setText(group.getmGroup());

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            viewHolderGroup = new ViewHolderGroup();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);
            viewHolderGroup.ivGroup = view.findViewById(R.id.ivGroup);
            viewHolderGroup.tvGroup = view.findViewById(R.id.tvGroup);
            view.setTag(viewHolderGroup);
        }else{
            viewHolderGroup = (ViewHolderGroup) view.getTag();
        }
        Group group = listGroup.get(position);

        viewHolderGroup.ivGroup.setImageResource(group.getmThumbnail());
        viewHolderGroup.tvGroup.setText(group.getmGroup());

        return view;
    }

    private class ViewHolderGroup {
        ImageView ivGroup;
        TextView tvGroup;
    }
}
