package com.example.skillupquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private ImageView imageView;
    private EditText emailET;
    private Button submitBTN;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        imageView = findViewById(R.id.image_view);
        emailET = findViewById(R.id.emailET);
        submitBTN = findViewById(R.id.submitBTN);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = emailET.getText().toString();
                firebaseAuth
                        .sendPasswordResetEmail(email)
                        .addOnCompleteListener(
                                ForgotPassword.this,
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(
                                                    ForgotPassword.this,
                                                    "Password recovery mail has successfully been sent to the email address provided",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(
                                                    ForgotPassword.this,
                                                    "Something went wrong",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                });
            }
        });
    }
}
