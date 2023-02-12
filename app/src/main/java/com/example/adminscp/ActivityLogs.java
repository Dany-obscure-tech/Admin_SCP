package com.example.adminscp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityLogs extends AppCompatActivity {

    DatabaseReference databaseReference;

    List<String> adminNameList;
    List<String> descriptionList;

    RecyclerView parkingList_recyclerView;
    Adapter_logsListRecyclerView adapter_logsListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        setTitle("Admin Logs");
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        adminNameList = new ArrayList<>();
        descriptionList = new ArrayList<>();

        parkingList_recyclerView = findViewById(R.id.parkingList_recyclerView);
        parkingList_recyclerView.setLayoutManager(new GridLayoutManager(ActivityLogs.this, 1));

        databaseReference = FirebaseDatabase.getInstance().getReference("Logs");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    descriptionList.add(snapshot1.child("description").getValue().toString());
                    adminNameList.add(snapshot1.child("subadmin").getValue().toString());
                }
                adapter_logsListRecyclerView = new Adapter_logsListRecyclerView(ActivityLogs.this, adminNameList, descriptionList);
                parkingList_recyclerView.setAdapter(adapter_logsListRecyclerView);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}