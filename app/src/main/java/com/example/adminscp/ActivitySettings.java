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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivitySettings extends AppCompatActivity {

    Boolean valid = true;
    Button edit_Button;
    DatabaseReference databaseReference;
    TextView email_editText;
    EditText pin_editText;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

        setTitle("Edit Admin");
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        edit_Button = (Button) findViewById(R.id.edit_Button);
        email_editText = (TextView) findViewById(R.id.email_editText);
        pin_editText = (EditText) findViewById(R.id.pin_editText);

        email_editText.setText(AdminDetails_class.getInstance().getEmail());
        pin_editText.setText(AdminDetails_class.getInstance().getPin());

        databaseReference = FirebaseDatabase.getInstance().getReference();


        edit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email_editText.getText().toString().isEmpty()) {
                    email_editText.setError("This field can not be empty");
                    valid = false;
                }
                if (pin_editText.getText().toString().isEmpty()) {
                    pin_editText.setError("This field can not be empty");
                    valid = false;
                }

                if (!isValidEmail(email_editText.getText().toString())) {
                    email_editText.setError("Invaid Email");
                    valid = false;
                }

                if (valid) {

                    mAuth.signInWithEmailAndPassword(AdminDetails_class.getInstance().getEmail(), AdminDetails_class.getInstance().getPin()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                user = mAuth.getCurrentUser();

                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            databaseReference.child("Super Admin").removeValue();

                                            mAuth.createUserWithEmailAndPassword(email_editText.getText().toString(),pin_editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()){
                                                        user = mAuth.getCurrentUser(); //You Firebase user
                                                        // user registered, start profile activity
                                                        databaseReference.child("Super Admin").child(email_editText.getText().toString().replace(".",",")).child("pin").setValue(pin_editText.getText().toString());
                                                        AdminDetails_class.getInstance().setEmail(email_editText.getText().toString());
                                                        AdminDetails_class.getInstance().setPin(pin_editText.getText().toString());
                                                        finish();
                                                    }
                                                    else{
                                                        Toast.makeText(ActivitySettings.this,"Could not create account. Please try again",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });


                            } else {
                                Toast.makeText(ActivitySettings.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    valid = true;
                }
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
