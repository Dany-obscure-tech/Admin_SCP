package com.example.adminscp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button parking_Button;
    Button admin_Button;
    Button settings_Button;
    Button users_Button;
    Button addParking_Button;
    Button addAdmin_Button;
    Button logs_Button;
    DatabaseReference databaseReference;
    RecyclerView parkingList_recyclerView;
    RecyclerView adminList_recyclerView;
    Adapter_parkingListRecyclerView adapter_parkingListRecyclerView;
    Adapter_adminListRecyclerView adapter_adminListRecyclerView;

    List<String> parkingName_List;
    List<String> parkingSlots_List;
    List<String> parkingSubAdmin_List;

    List<String> adminEmail_List;
    List<String> adminName_List;
    List<String> adminParking_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Super Admin Panel");

        FirebaseAuth.getInstance().signOut();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        parkingName_List = new ArrayList<>();
        parkingSlots_List = new ArrayList<>();
        parkingSubAdmin_List = new ArrayList<>();

        adminEmail_List = new ArrayList<>();
        adminName_List = new ArrayList<>();
        adminParking_List = new ArrayList<>();

        parking_Button = (Button) findViewById(R.id.parking_Button);
        admin_Button = (Button) findViewById(R.id.admin_Button);
        users_Button = (Button) findViewById(R.id.users_Button);
        settings_Button = (Button) findViewById(R.id.settings_Button);

        settings_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivitySettings.class);
                startActivity(intent);
            }
        });

        users_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityUsersList.class);
                startActivity(intent);

            }
        });

        parking_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parkingList_recyclerView.setVisibility(View.VISIBLE);
                adminList_recyclerView.setVisibility(View.GONE);
                parking_Button.setEnabled(false);
                admin_Button.setEnabled(true);
            }
        });

        admin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parkingList_recyclerView.setVisibility(View.GONE);
                adminList_recyclerView.setVisibility(View.VISIBLE);
                admin_Button.setEnabled(false);
                parking_Button.setEnabled(true);
            }
        });


        addAdmin_Button = (Button) findViewById(R.id.addAdmin_Button);
        addParking_Button = (Button) findViewById(R.id.addParking_Button);

        logs_Button = (Button) findViewById(R.id.logs_Button);

        logs_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityLogs.class);
                startActivity(intent);
            }
        });

        addAdmin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Activity_add_admin.class);
                startActivity(intent);
            }
        });

        parkingList_recyclerView = findViewById(R.id.parkingList_recyclerView);
        parkingList_recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        databaseReference.child("Parkings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        parkingName_List.add(snapshot1.getKey());
                        parkingSlots_List.add(snapshot1.child("number_of_slots").getValue().toString());
                        parkingSubAdmin_List.add(snapshot1.child("sub_admin").getValue().toString());
                    }
                    adapter_parkingListRecyclerView = new Adapter_parkingListRecyclerView(MainActivity.this,parkingName_List,parkingSlots_List,parkingSubAdmin_List);
                    parkingList_recyclerView.setAdapter(adapter_parkingListRecyclerView);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adminList_recyclerView = findViewById(R.id.adminList_recyclerView);
        adminList_recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        databaseReference.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    adminEmail_List.add(snapshot1.getKey().replace(",","."));
                    adminName_List.add(snapshot1.child("name").getValue().toString());
                    adminParking_List.add(snapshot1.child("parking").getValue().toString());
                }
                adapter_adminListRecyclerView = new Adapter_adminListRecyclerView(MainActivity.this,adminEmail_List,adminName_List,adminParking_List);
                adminList_recyclerView.setAdapter(adapter_adminListRecyclerView);

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