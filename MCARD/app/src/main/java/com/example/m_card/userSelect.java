package com.example.m_card;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class userSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select);
    }

    public void next(View view){

        Intent intent  = new Intent(userSelect.this,LogInPage.class);
        startActivity(intent);
    }
public void pharma(View view){

        Intent intent = new Intent(userSelect.this,pharmacist.class);
        startActivity(intent);

}
}

