package com.example.m_card.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.m_card.CustomAdapter;
import com.example.m_card.DataModel;
import com.example.m_card.PrescriptionShowActivity;
import com.example.m_card.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    int logos[] = {R.drawable.add,R.drawable.box1,R.drawable.box3,R.drawable.box2};
    ArrayList<String> arrayList;
    String times[] = {"2","4","5","6","7"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textView = root.findViewById(R.id.usernameValue);
        TextView ageName = root.findViewById(R.id.agevalue);
        final ProgressBar progressBar = root.findViewById(R.id.progressBar2);

        Animation animation = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);

        textView.setText(DataModel.nameOfUser.toUpperCase());
        ageName.setText(DataModel.id);

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        arrayList = new ArrayList<>();

        final GridView gridView = root.findViewById(R.id.GridView);
        gridView.startAnimation(animation);
        final CircularImageView circularImageView = (CircularImageView)root.findViewById(R.id.circularImageView);
// Set Border
        circularImageView.setBorderColor(Color.WHITE);

        circularImageView.setBorderWidth(3);
// Add Shadow with default param
// or with custom param
        circularImageView.setShadowRadius(0);
        circularImageView.setShadowColor(Color.BLACK);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(DataModel.id).child("Dates");
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("proFile").child(DataModel.id);

        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                circularImageView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);


            }
        });



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                progressDialog.dismiss();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String name = childSnapshot.getValue(String.class);

                    arrayList.add(name.replace("_","/"));

                    i = i + 1;
                }


                CustomAdapter customAdapter = new CustomAdapter(getContext(),logos,arrayList,times);
                gridView.setAdapter(customAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModel.DateSelected = arrayList.get(position).replace("/","_");
                        Intent intent = new Intent(getContext(), PrescriptionShowActivity.class);
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return root;
    }
}