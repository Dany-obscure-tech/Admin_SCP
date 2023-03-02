package com.example.adminscp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_adminListRecyclerView extends RecyclerView.Adapter<Adapter_adminListRecyclerView.ViewHolder> {
    Context context;
    List<String> adminEmail_List;
    List<String> adminName_List;
    List<String> adminParking_List;

    public Adapter_adminListRecyclerView(Context context, List<String> adminEmail_List, List<String> adminName_List, List<String> adminParking_List) {
        this.context = context;
        this.adminEmail_List = adminEmail_List;
        this.adminName_List = adminName_List;
        this.adminParking_List = adminParking_List;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_admin_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.adminEmail_textView.setText(adminEmail_List.get(position));
        holder.adminName_textView.setText(adminName_List.get(position));
        holder.adminParking_textView.setText(adminParking_List.get(position));
        holder.edit_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ActivityEditDetails.class);
                intent.putExtra("SUB_ADMIN", adminEmail_List.get(position).replace(".",","));
                intent.putExtra("PARKING", adminParking_List.get(position));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return adminEmail_List.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView adminEmail_textView;
        public TextView adminName_textView;
        public TextView adminParking_textView;
        public TextView edit_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.adminEmail_textView = (TextView) itemView.findViewById(R.id.adminEmail_textView);
            this.adminName_textView = (TextView) itemView.findViewById(R.id.adminName_textView);
            this.adminParking_textView = (TextView) itemView.findViewById(R.id.adminParking_textView);
            this.edit_textView = (TextView) itemView.findViewById(R.id.edit_textView);
        }
    }
}