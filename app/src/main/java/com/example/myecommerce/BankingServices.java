package com.example.myecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BankingServices extends AppCompatActivity {

    private Button btnLoanServices, btnCommunityEngagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banking_services);

        btnLoanServices = findViewById(R.id.btnLoanServices);
        btnCommunityEngagement = findViewById(R.id.btnCommunityEngagement);

        btnLoanServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loanServices = new Intent(BankingServices.this, LoanServices.class);
                startActivity(loanServices);
            }
        });

        btnCommunityEngagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent community = new Intent(BankingServices.this, CommunityContributions.class);
                startActivity(community);
            }
        });
    }
}
