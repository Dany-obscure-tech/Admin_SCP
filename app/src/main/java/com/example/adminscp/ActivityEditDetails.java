package com.example.adminscp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityEditDetails extends AppCompatActivity {
    String parkingName;
    Boolean valid = true;
    Button edit_Button;
    Button remove_Button;
    DatabaseReference databaseReference;

    EditText parkingAdminName_editText;
    EditText parkingAdminEmail_editText;
    EditText parkingAdminPin_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        edit_Button = (Button) findViewById(R.id.edit_Button);
        remove_Button = (Button) findViewById(R.id.remove_Button);

        parkingAdminName_editText = (EditText) findViewById(R.id.parkingAdminName_editText);
        parkingAdminEmail_editText = (EditText) findViewById(R.id.parkingAdminEmail_editText);
        parkingAdminPin_editText = (EditText) findViewById(R.id.parkingAdminPin_editText);

        parkingName = getIntent().getStringExtra("PARKING");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (!getIntent().getStringExtra("SUB_ADMIN").equals("No Admin")){
            parkingAdminEmail_editText.setText(getIntent().getStringExtra("SUB_ADMIN").replace(",","."));
            databaseReference.child("Admin/"+getIntent().getStringExtra("SUB_ADMIN")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    parkingAdminName_editText.setText(snapshot.child("name").getValue().toString());
                    parkingAdminPin_editText.setText(snapshot.child("pin").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            remove_Button.setVisibility(View.GONE);
        }


        edit_Button.setOnClickListener(new View.OnClickListener() {
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

                if (!isValidEmail(parkingAdminEmail_editText.getText().toString())){
                    parkingAdminEmail_editText.setError("Invaid Email");
                    valid = false;
                }

                if (valid){
                    databaseReference.child("Admin").child(getIntent().getStringExtra("SUB_ADMIN")).removeValue();

                    databaseReference.child("Parkings").child(String.valueOf(parkingName)).child("sub_admin").setValue(parkingAdminEmail_editText.getText().toString());

                    databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_editText.getText()).replace(".",",")).child("name").setValue(String.valueOf(parkingAdminName_editText.getText()));
                    databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_editText.getText()).replace(".",",")).child("pin").setValue(String.valueOf(parkingAdminPin_editText.getText()));
                    databaseReference.child("Admin").child(String.valueOf(parkingAdminEmail_editText.getText()).replace(".",",")).child("parking").setValue(String.valueOf(parkingName));

                    finish();

                }else {
                    valid = true;
                }
            }
        });

        remove_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Admin").child(getIntent().getStringExtra("SUB_ADMIN")).removeValue();
                databaseReference.child("Parkings").child(String.valueOf(parkingName)).child("sub_admin").setValue("No Admin");
                finish();
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