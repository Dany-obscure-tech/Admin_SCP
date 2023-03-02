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
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_add_admin extends AppCompatActivity {

    Boolean valid = true;
    Button add_Button;
    EditText parkingAdminName_editText;
    EditText parkingAdminEmail_editText;
    EditText parkingAdminPin_editText;
    String parkingName;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    private SmartMaterialSpinner<String> spParking;
//    private SmartMaterialSpinner<String> spEmptyItem;
    private List<String> parking_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        mAuth = FirebaseAuth.getInstance();

        setTitle("Add Admin");
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        add_Button = (Button) findViewById(R.id.add_Button);

        parkingAdminName_editText = (EditText) findViewById(R.id.parkingAdminName_editText);
        parkingAdminEmail_editText = (EditText) findViewById(R.id.parkingAdminEmail_editText);
        parkingAdminPin_editText = (EditText) findViewById(R.id.parkingAdminPin_editText);

        initSpinner();
        add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (parkingAdminName_editText.getText().toString().isEmpty()){
                    parkingAdminName_editText.setError("This field can not be empty");
                    valid = false;
                }
                if (parkingAdminEmail_editText.getText().toString().isEmpty()){
                    parkingAdminEmail_editText.setError("This field can not be empty");
                    valid = false;
                }
                if (parkingAdminPin_editText.getText().toString().isEmpty()){
                    parkingAdminPin_editText.setError("This field can not be empty");
                    valid = false;
                }
                if (parkingAdminPin_editText.getText().toString().length()<6){
                    parkingAdminPin_editText.setError("At least 6 digits");
                    valid = false;
                }

                if (!isValidEmail(parkingAdminEmail_editText.getText().toString())){
                    parkingAdminEmail_editText.setError("Invaid Email");
                    valid = false;
                }

                if (valid){
                    databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_editText.getText().toString().toLowerCase()).replace(".",",")).child("name").setValue(String.valueOf(parkingAdminName_editText.getText().toString().toLowerCase()));
                    databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_editText.getText().toString().toLowerCase()).replace(".",",")).child("pin").setValue(String.valueOf(parkingAdminPin_editText.getText()));
                    databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_editText.getText().toString().toLowerCase()).replace(".",",")).child("parking").setValue(parkingName);

                    if (!parkingName.equals("No Parking")){
                        databaseReference.child("Parkings").child(parkingName).child("sub_admin").setValue(parkingAdminEmail_editText.getText().toString());
                    }


                    mAuth.createUserWithEmailAndPassword(parkingAdminEmail_editText.getText().toString(),parkingAdminPin_editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                finish();
                            }else {
                                Toast.makeText(Activity_add_admin.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }else {
                    valid = true;
                }
            }
        });
    }

    public void initSpinner() {
        spParking = findViewById(R.id.spinner1);
        parking_list = new ArrayList<>();

        parking_list.add("No Parking");
        databaseReference.child("Parkings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    if (snapshot1.child("sub_admin").getValue().equals("No Admin")){
                        parking_list.add(snapshot1.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        spParking.setItem(parking_list);

        spParking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                parkingName = parking_list.get(position);
                add_Button.setEnabled(true);
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