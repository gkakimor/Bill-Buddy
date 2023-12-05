package com.example.bill_buddy_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TargetBankActivity extends AppCompatActivity {
    ImageView imgCIBC, imgBMO , imgSCOTIA , imgTD , imgRBC , imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_bank);

        imgCIBC = findViewById(R.id.imgCIBC);
        imgBMO = findViewById(R.id.imgBMO);
        imgSCOTIA = findViewById(R.id.imgSCOTIA);
        imgTD = findViewById(R.id.imgTD);
        imgRBC = findViewById(R.id.imgRBC);


        imgCIBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.cibconline.cibc.com/ebm-resources/online-banking/client/index.html#/auth/signon";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        imgBMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www1.bmo.com/banking/digital/login";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        imgSCOTIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://auth.scotiaonline.scotiabank.com/online?oauth_key=jRUgio4-Mko&oauth_key_signature=eyJraWQiOiJrUFVqdlNhT25GWUVDakpjMmV1MXJvNGxnb2VFeXJJb2tCbU1oX3BiZXNVIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJyZWZlcmVyIjoiaHR0cHM6XC9cL2d0Yi5zY290aWFiYW5rLmNvbVwvIiwib2F1dGhfa2V5IjoialJVZ2lvNC1Na28iLCJjb25zZW50X3JlcXVpcmVkIjpmYWxzZSwicmVkaXJlY3RfdXJpIjoiaHR0cHM6XC9cL3d3dy5zY290aWFvbmxpbmUuc2NvdGlhYmFuay5jb21cL29ubGluZVwvbGFuZGluZ1wvb2F1dGhsYW5kaW5nLmJucyIsImV4cCI6MTcwMTA1MTI4NiwiaWF0IjoxNzAxMDUwMDg2LCJqdGkiOiJhZTJiYjkwZi05NDE1LTRlZWYtYTJhOS0zZWZhOTIzNzI3YmMiLCJjbGllbnRfaWQiOiI4ZWU5MGMzOS0xYzUyLTRmZjQtOGFlNi1hN2I1NGM1Mzk5MzMiLCJjbGllbnRfbWV0YWRhdGEiOnsiQ2hhbm5lbElEIjoiU09MIiwiQXBwbGljYXRpb25Db2RlIjoiSDcifSwiaXNzdWVyIjoiaHR0cHM6XC9cL3Bhc3Nwb3J0LnNjb3RpYWJhbmsuY29tIn0.ySklEe-HjximGbwIOqaqGF5tvY-pQ9jbzgo0cu8WfpMrhtN2eHovfhkYGGguiAalgGkLi5ZKJE5vEK4edVu6TGEmxeXQTpUkVegGff2MZsFsWSAS6UX5teD8SnB5zsk7R_nsGte5apZ6HRLLqQNpXMZmeh0H7D-jqQr9fZduNZCfmztgXfeV0iyvybWpNB5JoblAy07FBA2EWIZFqS5ZhnGCGqRMyQpyVf6h0HDdv5PexgQ6NyWZT-qyMSpscSHBb8R1Y6TmN2ymJMj16E1ORXQFPuCB9R6gFQ33SSyRsiKmBG76mkdryIBeA-SXBlKjCm-F4yj3mxgDyz6Ccmck9A&preferred_environment=";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        imgTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.td.com/ca/en/personal-banking/my-accounts";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        imgRBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://secure.royalbank.com/statics/login-service-ui/index#/full/signin?LANGUAGE=ENGLISH";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        imgBMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www1.bmo.com/banking/digital/login";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });


        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TargetBankActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }
}