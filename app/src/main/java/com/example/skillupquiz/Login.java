package com.example.skillupquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import static com.example.skillupquiz.Register.isEmailValid;

public class Login extends AppCompatActivity {
    private ImageView imageView;
    private EditText emailET;
    private EditText passwordET;
    private Button signInBTN;
    private TextView registerTV;
    private TextView forgotPasswordTV;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView = findViewById(R.id.image_view);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        signInBTN = findViewById(R.id.signInBTN);
        registerTV = findViewById(R.id.regTV);
        forgotPasswordTV = findViewById(R.id.recoverPassTV);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (isEmailValid(email) && password.length()>=6) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(Login.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(Login.this, "Sign In Successful",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            Toast.makeText(Login.this, "User Already Exists",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(Login.this, "An error occured",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(Login.this, "Email or password is incorrect!!!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
