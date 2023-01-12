package com.example.adminscp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_parkingListRecyclerView extends RecyclerView.Adapter<Adapter_parkingListRecyclerView.ViewHolder> {
    Context context;
    List<String> parkingName_List;
    List<String> parkingSlots_List;
    List<String> parkingSubAdmin_List;

    public Adapter_parkingListRecyclerView(Context context, List<String> parkingName_List, List<String> parkingSlots_List, List<String> parkingSubAdmin_List) {
        this.context = context;
        this.parkingName_List = parkingName_List;
        this.parkingSlots_List = parkingSlots_List;
        this.parkingSubAdmin_List = parkingSubAdmin_List;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_parking_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.parkingName_textView.setText(parkingName_List.get(position));
        holder.parkingSlotNumber_textView.setText(parkingSlots_List.get(position));
        holder.parkingSubAdminName_textView.setText(parkingSubAdmin_List.get(position));
    }


    @Override
    public int getItemCount() {
        return parkingName_List.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView parkingName_textView;
        public TextView parkingSlotNumber_textView;
        public TextView parkingSubAdminName_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.parkingName_textView = (TextView) itemView.findViewById(R.id.parkingName_textView);
            this.parkingSlotNumber_textView = (TextView) itemView.findViewById(R.id.parkingSlotNumber_textView);
            this.parkingSubAdminName_textView = (TextView) itemView.findViewById(R.id.parkingSubAdminName_textView);
        }
    }
}