package com.example.adminscp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText email_editText;
    EditText pin_editText;
    Button login_Button;
    Button forgotPass_Button;
    DatabaseReference superAdminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Super Admin");

        mAuth = FirebaseAuth.getInstance();

        superAdminRef = FirebaseDatabase.getInstance().getReference("Super Admin");

        email_editText = findViewById(R.id.email_editText);
        pin_editText = findViewById(R.id.pin_editText);

        login_Button = findViewById(R.id.login_Button);

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean valid = true;
                if (email_editText.getText().toString().isEmpty()){
                    email_editText.setError("Enter email");
                    valid = false;
                }else if (!isValidEmail(email_editText.getText().toString())){
                    email_editText.setError("Invaid Email");
                    valid = false;
                }

                if (pin_editText.getText().toString().isEmpty()){
                    pin_editText.setError("Enter pin");
                    valid = false;
                }


                if (valid){

                    superAdminRef.child(email_editText.getText().toString().toLowerCase(Locale.ROOT).replace(".",",")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){
                                mAuth.signInWithEmailAndPassword(email_editText.getText().toString(),pin_editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            superAdminRef.child(email_editText.getText().toString().toLowerCase(Locale.ROOT).replace(".",",")).child("pin").setValue(pin_editText.getText().toString());
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(intent);

                                        }else {
                                            Toast.makeText(LoginActivity.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }else {
                                Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        forgotPass_Button = findViewById(R.id.forgotPass_Button);
        forgotPass_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean valid = true;
                if (email_editText.getText().toString().isEmpty()){
                    email_editText.setError("Enter email");
                    valid = false;
                }else if (!isValidEmail(email_editText.getText().toString())){
                    email_editText.setError("Invaid Email");
                    valid = false;
                }
                if (valid){
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email_editText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        email_editText.setText("");
        pin_editText.setText("");
    }
}