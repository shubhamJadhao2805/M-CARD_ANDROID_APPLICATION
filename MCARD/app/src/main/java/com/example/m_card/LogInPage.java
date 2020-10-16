package com.example.m_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LogInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
    }



    public void Signup(View view){

        Intent intent = new Intent(LogInPage.this,signup.class);
        startActivity(intent);


    }


    public void login(View view){

        final EditText id = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        final String idString = id.getText().toString();
        final String passString = password.getText().toString();
        if(TextUtils.isEmpty(idString) || TextUtils.isEmpty(passString)) {
        Toast.makeText(LogInPage.this,"Please enter fields",Toast.LENGTH_SHORT).show();

        }else{
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Authenticating...");

            progressDialog.show();

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Auth");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(idString)) {
                        DatabaseReference reference1 = reference.child(idString);
                        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                                String password = map.get("password");
                                DataModel.nameOfUser = map.get("name");
                                if (passString.equals(password)) {
                                    DataModel.id = idString;
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LogInPage.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LogInPage.this,"Password incorrect!",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(LogInPage.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(LogInPage.this, "User does not exits!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LogInPage.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });

        }

    }





}
