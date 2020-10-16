package com.example.m_card;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class signup extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


String finalIdString;
EditText name;
EditText mobileNo;
EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


    }


    public void Register(View view) {

        name = findViewById(R.id.txtName2);
        mobileNo = findViewById(R.id.txtMob2);
        password = findViewById(R.id.txtPassword);
        EditText repassword = findViewById(R.id.repassword);
        EditText dob = findViewById(R.id.dob);
        EditText bloodGroup = findViewById(R.id.bloodgroup);


        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(mobileNo.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(repassword.getText().toString()) || TextUtils.isEmpty(bloodGroup.getText().toString())) {
            Toast.makeText(signup.this, "Please Enter all fields", Toast.LENGTH_SHORT).show();
        } else {

            final String nameString = name.getText().toString();
            final String mobilString = mobileNo.getText().toString();
            final String passwordString = password.getText().toString();
            String repasswordString = repassword.getText().toString();
            final String bloodGroup2 = bloodGroup.getText().toString();
            final String dobString = dob.getText().toString();


            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Auth");

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            Random random = new Random();
            final int randomId = random.nextInt(9999-1000 + 1) + 1000;
            final String idString = String.valueOf(randomId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(idString)) {
                        Toast.makeText(signup.this, "User already exits !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", nameString);
                        map.put("mobil", mobilString);
                        map.put("password", passwordString);
                        map.put("id", idString);
                        map.put("group", bloodGroup2);
                        map.put("dob",dobString);
                        DatabaseReference newRefereance = databaseReference.child(idString);
                        newRefereance.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(signup.this, "Account created", Toast.LENGTH_SHORT).show();
                                    finalIdString = idString;

                                    if (ActivityCompat.checkSelfPermission(signup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
                                    } else {
//                                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                        startActivityForResult(intent, CAMERA_REQUEST);
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(
                                                Intent.createChooser(
                                                        intent,
                                                        "Select Image from here..."),
                                                CAMERA_REQUEST);

                                    }

                                    if(ActivityCompat.checkSelfPermission(signup.this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 4);
                                    }else{
                                        Toast.makeText(signup.this,"permission Granted",Toast.LENGTH_SHORT).show();
                                    }



//
//                               Runnable runnable = new Runnable() {
//                                   @Override
//                                   public void run() {
//                                       Intent intent = new Intent(signup.this,LogInPage.class);
//                                       startActivity(intent);
//                                       finish();
//                                   }
//                               };
//
//                               Handler handler = new Handler();
//                               handler.postDelayed(runnable,1000);

                                }


                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(signup.this, "Error!", Toast.LENGTH_SHORT).show();

                }
            });


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(signup.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                Toast.makeText(signup.this, "camera permission denied", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 35, baos);
                byte[] imageData = baos.toByteArray();

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Uploading image...");
                progressDialog.show();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("proFile").child(finalIdString);
                storageReference.putBytes(imageData).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            DataModel.nameOfUser = name.getText().toString();
                            DataModel.id = finalIdString;
                            Toast.makeText(signup.this, "Profile image Uploaded!", Toast.LENGTH_SHORT).show();

                            Intent messageIntent = new Intent(getApplicationContext(),HomeActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,messageIntent,0);
                            SmsManager sms=SmsManager.getDefault();
                            sms.sendTextMessage(mobileNo.getText().toString(), null, "Thanks for registering with M-CARD your id is  " + finalIdString + "and password is" + password.getText(), pendingIntent,null);
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(signup.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }


//
//
//
        } else {
            Toast.makeText(signup.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
