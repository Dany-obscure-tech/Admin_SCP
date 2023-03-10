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

public class ActivityNewParking extends AppCompatActivity {

    Button createParking_Button;
    Boolean valid = true;
    int slots;
    DatabaseReference databaseReference;

    EditText parkingName_editText;
    EditText numberOfSlots_editText;

    SmartMaterialSpinner<String> spParking;
    List<String> admin_list;
    String adminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_parking);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        createParking_Button = (Button) findViewById(R.id.createParking_Button);

        parkingName_editText = (EditText) findViewById(R.id.parkingName_editText);
        numberOfSlots_editText = (EditText) findViewById(R.id.numberOfSlots_editText);

        createParking_Button.setOnClickListener(new View.OnClickListener() {
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
                    databaseReference.child("Parkings").child(String.valueOf(parkingName_editText.getText())).child("number_of_slots").setValue(numberOfSlots_editText.getText().toString());
                    databaseReference.child("Parkings").child(String.valueOf(parkingName_editText.getText())).child("sub_admin").setValue(adminName);
                    if (!adminName.equals("No Admin")){
                        databaseReference.child("Admin").child(String.valueOf(adminName).replace(".",",")).child("parking").setValue(parkingName_editText.getText().toString());
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



        setTitle("Add Parking");
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        initSpinner();



    }

    public void initSpinner() {
        spParking = findViewById(R.id.spinner1);
        admin_list = new ArrayList<>();

        admin_list.add("No Admin");
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
                adminName = admin_list.get(position);
                createParking_Button.setEnabled(true);
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