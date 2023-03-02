package com.example.adminscp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_userListRecyclerView extends RecyclerView.Adapter<Adapter_userListRecyclerView.ViewHolder> {
    Context context;

    List<String> username_List;
    List<String> name_List;
    List<String> parking_List;
    List<String> phno_List;
    List<String> token_List;
    List<String> wallet_List;
    List<String> duration_List;
    List<String> duedate_List;


    public Adapter_userListRecyclerView(Context context, List<String> username_List, List<String> name_List, List<String> parking_List, List<String> phno_List, List<String> token_List, List<String> wallet_List, List<String> duration_List, List<String> duedate_List) {
        this.context = context;
        this.username_List = username_List;
        this.name_List = name_List;
        this.parking_List = parking_List;
        this.phno_List = phno_List;
        this.token_List = token_List;
        this.wallet_List = wallet_List;
        this.duration_List = duration_List;
        this.duedate_List = duedate_List;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_user_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.username_textView.setText(username_List.get(position));
        holder.name_textView.setText(name_List.get(position));
        holder.parking_textView.setText(parking_List.get(position));
        holder.phno_textView.setText(phno_List.get(position));
        holder.token_textView.setText(token_List.get(position));
        holder.wallet_textView.setText(wallet_List.get(position));
        holder.duration_textView.setText(duration_List.get(position));
        holder.duedate_textView.setText(duedate_List.get(position));

    }


    @Override
    public int getItemCount() {
        return username_List.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username_textView;
        public TextView name_textView;
        public TextView parking_textView;
        public TextView phno_textView;
        public TextView token_textView;
        public TextView wallet_textView;
        public TextView duration_textView;
        public TextView duedate_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.username_textView = (TextView) itemView.findViewById(R.id.username_textView);
            this.name_textView = (TextView) itemView.findViewById(R.id.name_textView);
            this.parking_textView = (TextView) itemView.findViewById(R.id.parking_textView);
            this.phno_textView = (TextView) itemView.findViewById(R.id.phno_textView);
            this.token_textView = (TextView) itemView.findViewById(R.id.token_textView);
            this.wallet_textView = (TextView) itemView.findViewById(R.id.wallet_textView);
            this.duration_textView = (TextView) itemView.findViewById(R.id.duration_textView);
            this.duedate_textView = (TextView) itemView.findViewById(R.id.duedate_textView);

        }
    }
}