package com.example.adminscp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityUsersList extends AppCompatActivity {
    Adapter_userListRecyclerView adapter_userListRecyclerView;
    RecyclerView parkingList_recyclerView;
    DatabaseReference databaseReference;
    DatabaseReference walletRef;
    DatabaseReference SubscriptionRef;

    List<String> username_List;
    List<String> name_List;
    List<String> parking_List;
    List<String> phno_List;
    List<String> token_List;
    List<String> wallet_List;
    List<String> duration_List;
    List<String> duedate_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        setTitle("Users");
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        username_List = new ArrayList<>();
        name_List = new ArrayList<>();
        parking_List = new ArrayList<>();
        phno_List = new ArrayList<>();
        token_List = new ArrayList<>();
        wallet_List = new ArrayList<>();
        duration_List = new ArrayList<>();
        duedate_List = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        parkingList_recyclerView = findViewById(R.id.parkingList_recyclerView);
        parkingList_recyclerView.setLayoutManager(new GridLayoutManager(ActivityUsersList.this, 1));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.child("Users").getChildren()){
                    username_List.add(snapshot1.getKey());
                    name_List.add(snapshot1.child("name").getValue().toString());
                    parking_List.add(snapshot1.child("parking").getValue().toString());
                    phno_List.add(snapshot1.child("ph_no").getValue().toString());
                    token_List.add(snapshot1.child("token").getValue().toString());

                    duration_List.add(snapshot.child("Subscription").child(snapshot1.getKey()).child("Duration").getValue().toString());
                    duedate_List.add(snapshot.child("Subscription").child(snapshot1.getKey()).child("due_date").getValue().toString());

                    wallet_List.add(snapshot.child("Wallet").child(snapshot1.getKey()).getValue().toString());

                }
                adapter_userListRecyclerView = new Adapter_userListRecyclerView(ActivityUsersList.this,username_List, name_List, parking_List, phno_List, token_List, wallet_List, duration_List, duedate_List);
                parkingList_recyclerView.setAdapter(adapter_userListRecyclerView);
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