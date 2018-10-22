package io.michalzuk.horton.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.michalzuk.horton.R;
import io.michalzuk.horton.activities.LoginActivity;
import io.michalzuk.horton.models.ApiCredentials;

public class SettingsFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText domain;
    private EditText username;
    private EditText apiKey;
    private Button signOut;
    private Button saveCredentials;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        domain = view.findViewById(R.id.credentials_domain);
        username = view.findViewById(R.id.credentials_username);
        apiKey = view.findViewById(R.id.credentials_api_key);
        signOut = view.findViewById(R.id.sign_out_button);
        saveCredentials = view.findViewById(R.id.save_credentials);

        String domainValue = domain.getText().toString();
        String usernameValue = username.getText().toString();
        String apiKeyValue = apiKey.getText().toString();

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user =  firebaseAuth.getCurrentUser();

        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        String userId = user.getUid();
        DatabaseReference mRef =  database.getReference().child("user_credentials").child(userId);
        mRef.child("domain").setValue(domainValue);
        mRef.child("username").setValue(usernameValue);
        mRef.child("apiKey").setValue(apiKeyValue);


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity
                        .class);
                startActivity(intent);
            }
        });

        saveCredentials.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (firebaseUser!=null) {
                    FirebaseDatabase database =  FirebaseDatabase.getInstance();
                    String userId = user.getUid();
                    ApiCredentials apiCredentials = new ApiCredentials("aaa", "bbb", "ccc");
                    DatabaseReference mRef =  database.getReference("https://horton-87339.firebaseio.com/").child("user_credentials").child(userId);
                    mRef.setValue(apiCredentials);
                }
            }
        });


        return view;
    }
}
