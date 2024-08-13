package com.example.comp2000referredcoursework.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.comp2000referredcoursework.LoginActivity;
import com.example.comp2000referredcoursework.R;
import com.example.comp2000referredcoursework.databinding.FragmentUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserFragment extends Fragment {

    TextView UserFullName, UserEmail, UserPhoneNumber;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        UserFullName = view.findViewById(R.id.userName);
        UserEmail = view.findViewById(R.id.fragmentUserEmail);
        UserPhoneNumber = view.findViewById(R.id.fragmentUserPhoneNumber);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        super.onViewCreated(view, savedInstanceState);

        userId = firebaseAuth.getCurrentUser().getUid();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                UserPhoneNumber.setText(value.getString("phoneNumber"));
                UserEmail.setText(value.getString("email"));
                UserFullName.setText(value.getString("name"));

            }
        });
    }

    public void userLogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}