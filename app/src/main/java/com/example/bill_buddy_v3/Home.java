package com.example.bill_buddy_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    private final Class HOME = Home.class;
    private final Home CURRENT = Home.this;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Bundle extras = getIntent().getExtras();
        user_id = extras.getInt("user_id");
    }

    public void registerBill (View view) {
        Intent intent = new Intent(CURRENT, RegisterBill.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
    public void upcoming (View view) {
        Intent intent = new Intent(CURRENT, UpcomingBills.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void history (View view) {
        Intent intent = new Intent(CURRENT, PaymentHistory.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void updateBill (View view) {
        Intent intent = new Intent(CURRENT, UpdateBill.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

    public void bankList (View view) {
        Intent intent = new Intent(CURRENT, TargetBankActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

}