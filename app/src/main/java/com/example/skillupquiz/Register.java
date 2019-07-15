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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class Register extends AppCompatActivity {
    private ImageView imageView;
    private EditText nameET;
    private EditText emailET;
    private EditText passwordET;
    private Button signUpBTN;
    private TextView signInTV;
    private TextView forgotPasswordTV;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    CollectionReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageView = findViewById(R.id.image_view);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        signUpBTN = findViewById(R.id.signUpBTN);
        signInTV = findViewById(R.id.regTV);
        forgotPasswordTV = findViewById(R.id.recoverPassTV);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("users");


        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameET.getText().toString();
                final String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (isEmailValid(email) && password.length()>=6) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()) {
                                        HashMap<String, String> user = new HashMap();
                                        user.put("name", name);
                                        user.put("email", email);
                                        String userId = task.getResult().getUser().getUid();
                                        reference
                                                .document(userId)
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Register.this,
                                                                "Successfully registered " +
                                                                        "\n You can login now",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new
                                                                Intent(Register.this,
                                                                Login.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(
                                                                Register.this,
                                                                "Registration Failed \n " +
                                                                        "Something went wrong \n " +
                                                                        "Try again.",
                                                                Toast.LENGTH_SHORT
                                                        ).show();
                                                    }
                                                });

                                        Toast.makeText(Register.this, "Succesfully Registered",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            Toast.makeText(Register.this, "User Already Exists",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        Toast.makeText(Register.this, "An error occured",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                else {
                    Toast.makeText(Register.this, "Email or password is incorrect!!!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public static boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}
