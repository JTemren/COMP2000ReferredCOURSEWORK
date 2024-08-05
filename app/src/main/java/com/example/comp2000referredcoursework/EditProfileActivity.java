package com.example.comp2000referredcoursework;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {

    EditText EditUserName, EditUserPhoneNumber, EditUserEmail;
    ProgressBar EditUserProgressBar;
    Button EditConfirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        EditUserName = findViewById(R.id.editUserName);
        EditUserPhoneNumber = findViewById(R.id.editUserPhoneNumber);
        EditUserEmail = findViewById(R.id.editUserEmail);

        Intent data = getIntent();
        String name = data.getStringExtra("name");
        String email = data.getStringExtra("email");
        String phoneNumber = data.getStringExtra("phoneNumber");

        Log.d("TAG", "onCreate: " + name + " " + email + " " + phoneNumber);

        EditUserEmail.setText(email, EditText.BufferType.EDITABLE);
        EditUserName.setText(name);
        EditUserPhoneNumber.setText(phoneNumber);



        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void EditConfirm(View view) {
    }
}