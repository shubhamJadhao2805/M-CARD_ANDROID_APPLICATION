package com.example.m_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrescriptionShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_show);
        final ArrayList<String> arrayList = new ArrayList<>();
        final TextView textView = findViewById(R.id.diseaseName);



        final ListView listView = findViewById(R.id.prescriptionList);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(DataModel.id).child(DataModel.DateSelected);
//        reference.push().setValue("Name");
//        reference.push().setValue("prescription one");
//        reference.push().setValue("prescription two");
//        reference.push().setValue("prescription three");
//        reference.push().setValue("prescription foure").addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(PrescriptionShowActivity.this,"Done",Toast.LENGTH_SHORT).show();
//            }
//        });

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String name = childSnapshot.getValue(String.class);
                    arrayList.add(name);
                }

                int i;
                for(i=1;i<arrayList.size();i++){
                    String[] arrayList1 = arrayList.get(i).split("_");
                    String name = arrayList1[0];
                    String time = arrayList1[1];
                    String quantity = arrayList1[2];

                    arrayList.set(i,"Name = " + name + "\n" + "Time = " + time + "\n" + "Quantity = " + quantity);

                }

                if(!arrayList.isEmpty()) {
                    textView.setText(arrayList.get(0));
                    arrayList.remove(0);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PrescriptionShowActivity.this, android.R.layout.simple_list_item_1, arrayList);
                    listView.setAdapter(arrayAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }
}
