package com.example.comp2000referredcoursework;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText RegisterEmail, RegisterPassword, RegisterName, RegisterPhone;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    Button RegisterBtn;
    ProgressBar RegisterProgressBar;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        findViewById(R.id.registerBtn).setOnClickListener(this);

        RegisterEmail = findViewById(R.id.registerEmail);
        RegisterPassword = findViewById(R.id.registerPassword);
        RegisterBtn =  findViewById(R.id.registerBtn);
        RegisterProgressBar = findViewById(R.id.registerProgressBar);
        RegisterName = findViewById(R.id.registerName);
        RegisterPhone = findViewById(R.id.registerPhoneNumber);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() ==R.id.registerBtn){
            RegisterUser();
        }
    }

    private void RegisterUser() {
        String email = RegisterEmail.getText().toString();
        String password = RegisterPassword.getText().toString();
        String name = RegisterName.getText().toString();
        String phoneNumber = RegisterPhone.getText().toString();

        if (email.isEmpty()){
            RegisterEmail.setError("Email Required");
            RegisterEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            RegisterEmail.setError("Invalid Email");
            RegisterEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            RegisterPassword.setError("Password Required");
            RegisterPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            RegisterPassword.setError("6 Characters or more Required");
            RegisterPassword.requestFocus();
        }

//        RegisterProgressBar.setVisibility(View.VISIBLE);
//        RegisterBtn.setVisibility(View.INVISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
//                    RegisterProgressBar.setVisibility(View.INVISIBLE);
//                    RegisterBtn.setVisibility(View.INVISIBLE);

                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            RegisterProgressBar.setVisibility(View.INVISIBLE);
//                            RegisterBtn.setVisibility(View.VISIBLE);

                        }
                    });
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                    Map<String,Object> appUser = new HashMap<>();
                    appUser.put("name", name);
                    appUser.put("phoneNumber", phoneNumber);
                    appUser.put("email", email);
                    documentReference.set(appUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: user Profile created for " +userID);
                        }
                    });

                    Intent intent = new Intent(RegisterActivity.this, UserActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT);
                    RegisterProgressBar.setVisibility(View.VISIBLE);
                    RegisterBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}