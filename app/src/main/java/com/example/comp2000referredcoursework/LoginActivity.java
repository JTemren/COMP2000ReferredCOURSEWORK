package com.example.comp2000referredcoursework;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    EditText LoginEmail, LoginPassword;
    TextView RegisterNowTxt;
    FirebaseAuth mAuth;
    Button LoginBtn;
    ProgressBar LoginProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.loginRegisterNowBtn).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);

        RegisterNowTxt = findViewById(R.id.loginRegisterNowBtn);
        LoginEmail = findViewById(R.id.loginEmail);
        LoginPassword = findViewById(R.id.loginPassword);
        LoginBtn = findViewById(R.id.loginBtn);
        LoginProgressBar = findViewById(R.id.loginProgressBar);



    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.loginBtn){
//            Toast.makeText(this, "Pressed Login", Toast.LENGTH_SHORT).show();
            userLogin();
        }
        if (view.getId() ==R.id.loginRegisterNowBtn){
//            Toast.makeText(this, "Pressed Register", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,RegisterActivity.class));
        }
    }

    private void userLogin() {

        String email = LoginEmail.getText().toString();
        String password = LoginPassword.getText().toString();

        if (email.isEmpty()){
            LoginEmail.setError("Email Required");
            LoginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            LoginEmail.setError("Invalid Email");
            LoginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            LoginPassword.setError("Password Required");
            LoginPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            LoginPassword.setError("6 Characters or more Required");
            LoginPassword.requestFocus();
        }

        LoginProgressBar.setVisibility(View.VISIBLE);
        LoginBtn.setVisibility(View.INVISIBLE);
        RegisterNowTxt.setVisibility(View.INVISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    LoginProgressBar.setVisibility(View.INVISIBLE);
                    LoginBtn.setVisibility(View.INVISIBLE);
                    RegisterNowTxt.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT);
                    LoginProgressBar.setVisibility(View.INVISIBLE);
                    LoginBtn.setVisibility(View.VISIBLE);
                    RegisterNowTxt.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}