package com.example.m_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class prescriptionpharmacist extends AppCompatActivity {
    int logos[] = {R.drawable.add,R.drawable.box1,R.drawable.box3,R.drawable.box2};
    String dates[] = {"","","",""};
    String times[] = {"2","4","5","6","7"};
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptionpharmacist);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting prescription details...");
        progressDialog.show();
        arrayList = new ArrayList<>();

        TextView textView = findViewById(R.id.usernameValue2);
        textView.setText(DataModel.id);

        progressDialog.setCanceledOnTouchOutside(false);

        final GridView gridView = findViewById(R.id.prescriptionGrideView);

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Users");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(DataModel.id)){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(DataModel.id).child("Dates");



                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int i = 0;

                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                String name = childSnapshot.getValue(String.class);
                                arrayList.add(name);
                                i = i + 1;
                            }

                            progressDialog.dismiss();

                            CustomAdapter customAdapter = new CustomAdapter(prescriptionpharmacist.this,logos,arrayList,times);
                            gridView.setAdapter(customAdapter);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    DataModel.DateSelected = arrayList.get(position).replace("/","_");
                                    Intent intent = new Intent(prescriptionpharmacist.this, PrescriptionShowActivity.class);
                                    startActivity(intent);

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();

                        }
                    });

                }else{
                    Toast.makeText(prescriptionpharmacist.this,"Wrong QR code",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });


    }
}
