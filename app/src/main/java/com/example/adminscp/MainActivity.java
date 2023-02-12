package com.example.adminscp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button addParking_Button;
    Button logs_Button;
    DatabaseReference databaseReference;
    RecyclerView parkingList_recyclerView;
    Adapter_parkingListRecyclerView adapter_parkingListRecyclerView;

    List<String> parkingName_List;
    List<String> parkingSlots_List;
    List<String> parkingSubAdmin_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Admin Panel");

        databaseReference = FirebaseDatabase.getInstance().getReference("Parkings");
        parkingName_List = new ArrayList<>();
        parkingSlots_List = new ArrayList<>();
        parkingSubAdmin_List = new ArrayList<>();
        addParking_Button = (Button) findViewById(R.id.addParking_Button);
        logs_Button = (Button) findViewById(R.id.logs_Button);

        logs_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityLogs.class);
                startActivity(intent);
            }
        });

        parkingList_recyclerView = findViewById(R.id.parkingList_recyclerView);
        parkingList_recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    parkingName_List.add(snapshot1.getKey());
                    parkingSlots_List.add(snapshot1.child("number_of_slots").getValue().toString());
                    parkingSubAdmin_List.add(snapshot1.child("sub_admin").getValue().toString());
                }
                adapter_parkingListRecyclerView = new Adapter_parkingListRecyclerView(MainActivity.this,parkingName_List,parkingSlots_List,parkingSubAdmin_List);
                parkingList_recyclerView.setAdapter(adapter_parkingListRecyclerView);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addParking_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityNewParking.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}