package com.example.adminscp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityEditParking extends AppCompatActivity {
    String parkingName;
    String parkingSlots;
    String newAdminName;
    String adminName;
    Button edit_Button;
    Button remove_Button;
    Boolean valid = true;
    int slots;
    DatabaseReference databaseReference;

    EditText parkingName_editText;
    EditText numberOfSlots_editText;

    SmartMaterialSpinner<String> spParking;
    List<String> admin_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parking);

        parkingName = getIntent().getStringExtra("PARKING");
        adminName = getIntent().getStringExtra("SUB_ADMIN");
        parkingSlots = getIntent().getStringExtra("PARKING_SLOTS");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        edit_Button = (Button) findViewById(R.id.edit_Button);
        remove_Button = (Button) findViewById(R.id.remove_Button);

        parkingName_editText = (EditText) findViewById(R.id.parkingName_editText);
        parkingName_editText.setText(parkingName);

        numberOfSlots_editText = (EditText) findViewById(R.id.numberOfSlots_editText);
        numberOfSlots_editText.setText(parkingSlots);

        edit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parkingName_editText.getText().toString().isEmpty()){
                    parkingName_editText.setError("This field can not be empty");
                    valid = false;
                }
                if (numberOfSlots_editText.getText().toString().isEmpty()){
                    numberOfSlots_editText.setError("This field can not be empty");
                    valid = false;
                }


                if (valid){
                    databaseReference.child("Parkings").child(parkingName).removeValue();
                    databaseReference.child("Slots").child(parkingName).removeValue();
                    databaseReference.child("Enterance").child(parkingName).removeValue();
                    databaseReference.child("Registration").child(parkingName).removeValue();
                    databaseReference.child("Gates").child(parkingName).removeValue();

                    databaseReference.child("Parkings").child(String.valueOf(parkingName_editText.getText())).child("number_of_slots").setValue(numberOfSlots_editText.getText().toString());
                    databaseReference.child("Parkings").child(String.valueOf(parkingName_editText.getText())).child("sub_admin").setValue(newAdminName);
                    if (!newAdminName.equals("No Admin")){
                        databaseReference.child("Admin").child(String.valueOf(newAdminName).replace(".",",")).child("parking").setValue(parkingName_editText.getText().toString());
                    }

                    if (!adminName.equals("No Admin")){
                        databaseReference.child("Admin").child(String.valueOf(adminName).replace(".",",")).child("parking").setValue("No Parking");
                    }


                    slots = Integer.parseInt(numberOfSlots_editText.getText().toString());

                    for (int i = 0;i<slots;i++){
                        databaseReference.child("Slots").child(String.valueOf(parkingName_editText.getText())).child(String.valueOf(i+1)).setValue(0);
                    }


                    databaseReference.child("Enterance").child(String.valueOf(parkingName_editText.getText())).setValue(0);

                    databaseReference.child("Registration").child(String.valueOf(parkingName_editText.getText())).setValue(0);

                    databaseReference.child("Gates").child(String.valueOf(parkingName_editText.getText())).child("gate").setValue(0);
                    databaseReference.child("Gates").child(String.valueOf(parkingName_editText.getText())).child("registration").setValue(0);

                    finish();

                }else {
                    valid = true;
                }
            }
        });
        remove_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Parkings").child(parkingName).removeValue();
                databaseReference.child("Slots").child(parkingName).removeValue();
                databaseReference.child("Enterance").child(parkingName).removeValue();
                databaseReference.child("Registration").child(parkingName).removeValue();
                databaseReference.child("Gates").child(parkingName).removeValue();
                databaseReference.child("Admin").child(String.valueOf(adminName).replace(".",",")).child("parking").setValue("No Parking");
                finish();
            }
        });

        setTitle("Edit Parking");
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        initSpinner();



    }

    public void initSpinner() {
        spParking = findViewById(R.id.spinner1);
        admin_list = new ArrayList<>();

        if (!adminName.equals("No Admin")){
            admin_list.add(adminName.replace(",","."));
        }

        admin_list.add("No Admin");
        spParking.setSelection(0);
        databaseReference.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    if (snapshot1.child("parking").getValue().equals("No Parking")){
                        admin_list.add(snapshot1.getKey().toString().replace(",","."));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        spParking.setItem(admin_list);

        spParking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                newAdminName = admin_list.get(position);
                edit_Button.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}