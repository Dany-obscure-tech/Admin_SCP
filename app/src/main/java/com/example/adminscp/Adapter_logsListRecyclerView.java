package com.example.adminscp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_logsListRecyclerView extends RecyclerView.Adapter<Adapter_logsListRecyclerView.ViewHolder> {
    Context context;
    List<String> adminNameList;
    List<String> descriptionList;

    public Adapter_logsListRecyclerView(Context context, List<String> adminNameList, List<String> descriptionList) {
        this.context = context;
        this.adminNameList = adminNameList;
        this.descriptionList = descriptionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_logs_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.adminName_textView.setText(adminNameList.get(position));
        holder.description_textView.setText(descriptionList.get(position));

    }


    @Override
    public int getItemCount() {
        return adminNameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView adminName_textView;
        public TextView description_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.adminName_textView = (TextView) itemView.findViewById(R.id.adminName_textView);
            this.description_textView = (TextView) itemView.findViewById(R.id.description_textView);
        }
    }
}