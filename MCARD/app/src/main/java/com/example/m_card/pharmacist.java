package com.example.m_card;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.m_card.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

public class pharmacist extends AppCompatActivity {

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist);
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(true);
        qrScan.setBeepEnabled(true);
    }


    public void scanQrCode(View view) {


//        final EditText id = findViewById(R.id.email);
//        final EditText pass = findViewById(R.id.password);
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait...");
//
//        if (!TextUtils.isEmpty(id.getText()) || !TextUtils.isEmpty(pass.getText())) {
//            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Auth");
//            reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.hasChild(id.getText().toString())){
//
//                        DatabaseReference reference1 = reference.child(id.getText().toString());
//                        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                HashMap<String,String> map = (HashMap<String, String>) dataSnapshot.getValue();
//                                String password = map.get("password");
//                                if (pass.getText().toString().equals(password)) {
//
//
//
//
//
//                                }else{
//                                    Toast.makeText(pharmacist.this,"Password incorrect!",Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//        }else{
//            Toast.makeText(pharmacist.this,"Please enter Fieldds",Toast.LENGTH_SHORT).show();
//        }


        qrScan.initiateScan();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {


                Toast.makeText(pharmacist.this,result.getContents(),Toast.LENGTH_SHORT).show();
                DataModel.id = result.getContents().toString();
                Intent intent = new Intent(pharmacist.this,prescriptionpharmacist.class);
                startActivity(intent);

            }
            super.onActivityResult(requestCode, resultCode, data);
        }    }

}
