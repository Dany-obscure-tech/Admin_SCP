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
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityEditDetails extends AppCompatActivity {
    String parkingName;
    String newParkingName;
    Boolean valid = true;
    Button edit_Button;
    Button remove_Button;
    DatabaseReference databaseReference;

    EditText parkingAdminName_editText;
    TextView parkingAdminEmail_textView;
    EditText parkingAdminPin_editText;

    FirebaseAuth mAuth;
    FirebaseUser user;
    private SmartMaterialSpinner<String> spParking;
    private List<String> parking_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        mAuth = FirebaseAuth.getInstance();

        setTitle("Edit Admin");
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        edit_Button = (Button) findViewById(R.id.edit_Button);
        remove_Button = (Button) findViewById(R.id.remove_Button);

        parkingAdminName_editText = (EditText) findViewById(R.id.parkingAdminName_editText);
        parkingAdminEmail_textView = (TextView) findViewById(R.id.parkingAdminEmail_textView);
        parkingAdminPin_editText = (EditText) findViewById(R.id.parkingAdminPin_editText);

        parkingName = getIntent().getStringExtra("PARKING");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (!getIntent().getStringExtra("SUB_ADMIN").equals("No Admin")){
            parkingAdminEmail_textView.setText(getIntent().getStringExtra("SUB_ADMIN").replace(",","."));
            databaseReference.child("Admin/"+getIntent().getStringExtra("SUB_ADMIN")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    parkingAdminName_editText.setText(snapshot.child("name").getValue().toString());
                    parkingAdminPin_editText.setText(snapshot.child("pin").getValue().toString());

                    mAuth.signInWithEmailAndPassword(getIntent().getStringExtra("SUB_ADMIN").replace(",","."), snapshot.child("pin").getValue().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                user = mAuth.getCurrentUser();
                                edit_Button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if (parkingAdminName_editText.getText().toString().isEmpty()){
                                            parkingAdminName_editText.setError("This field can not be empty");
                                            valid = false;
                                        }
                                        if (parkingAdminEmail_textView.getText().toString().isEmpty()){
                                            parkingAdminEmail_textView.setError("This field can not be empty");
                                            valid = false;
                                        }
                                        if (parkingAdminPin_editText.getText().toString().isEmpty()){
                                            parkingAdminPin_editText.setError("This field can not be empty");
                                            valid = false;
                                        }

                                        if (!isValidEmail(parkingAdminEmail_textView.getText().toString())){
                                            parkingAdminEmail_textView.setError("Invaid Email");
                                            valid = false;
                                        }

                                        if (valid){
                                            databaseReference.child("Admin").child(getIntent().getStringExtra("SUB_ADMIN")).removeValue();
                                            databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_textView.getText()).replace(".",",")).child("name").setValue(String.valueOf(parkingAdminName_editText.getText()));
                                            databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_textView.getText()).replace(".",",")).child("pin").setValue(String.valueOf(parkingAdminPin_editText.getText()));
                                            databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_textView.getText()).replace(".",",")).child("parking").setValue(String.valueOf(newParkingName));

                                            if (!parkingName.equals("No Parking")){
                                                databaseReference.child("Parkings").child(String.valueOf(parkingName)).child("sub_admin").setValue("No Admin");
                                            }
                                            if (!newParkingName.equals("No Parking")){
                                                databaseReference.child("Parkings").child(String.valueOf(newParkingName)).child("sub_admin").setValue(parkingAdminEmail_textView.getText().toString());
                                            }

                                            user.updatePassword(parkingAdminPin_editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        finish();
                                                    } else {
                                                        Toast.makeText(ActivityEditDetails.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                        }else {
                                            valid = true;
                                        }
                                    }
                                });

                                remove_Button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        databaseReference.child("Admin").child(getIntent().getStringExtra("SUB_ADMIN")).removeValue();
                                        if (!parkingName.equals("No Parking")){
                                            databaseReference.child("Parkings").child(String.valueOf(parkingName)).child("sub_admin").setValue("No Admin");
                                        }

                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    finish();
                                                }else {
                                                    Toast.makeText(ActivityEditDetails.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                });

                                initSpinner();
                            } else {
                                Toast.makeText(ActivityEditDetails.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            remove_Button.setVisibility(View.GONE);
        }






    }

    public void initSpinner() {
        spParking = findViewById(R.id.spinner1);
        parking_list = new ArrayList<>();

        if (!parkingName.equals("No Parking")){
            parking_list.add(parkingName);
        }

        parking_list.add("No Parking");
        spParking.setSelection(0);
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
                newParkingName = parking_list.get(position);

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