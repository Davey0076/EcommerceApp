package com.example.myecommerce;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RepayActivity extends AppCompatActivity {

    private EditText etAmountToRepay, etMpesaTransactionCode;
    private Button btnFinishRepayment;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userEmail, loanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repay);

        etAmountToRepay = findViewById(R.id.etAmountToRepay);
        etMpesaTransactionCode = findViewById(R.id.etMpesaTransactionCode);
        btnFinishRepayment = findViewById(R.id.btnFinishRepayment);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the logged-in user's email
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }

        // Get the loanId from the intent
        if (getIntent().hasExtra("loanId")) {
            loanId = getIntent().getStringExtra("loanId");
        }

        btnFinishRepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountToRepayStr = etAmountToRepay.getText().toString().trim();
                String transactionCode = etMpesaTransactionCode.getText().toString().trim();

                if (TextUtils.isEmpty(amountToRepayStr) || TextUtils.isEmpty(transactionCode)) {
                    Toast.makeText(RepayActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    double amountToRepay = Double.parseDouble(amountToRepayStr);
                    processRepayment(amountToRepay, transactionCode);
                }
            }
        });
    }

    private void processRepayment(double amountToRepay, String transactionCode) {
        db.collection("loans").document(loanId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            double totalLoanBalance = documentSnapshot.getDouble("totalLoanBalance");
                            double newLoanBalance = totalLoanBalance - amountToRepay;

                            if (newLoanBalance < 0) {
                                Toast.makeText(RepayActivity.this, "Repayment amount exceeds loan balance", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            db.collection("loans").document(loanId).update("totalLoanBalance", newLoanBalance)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RepayActivity.this, "Repayment successful", Toast.LENGTH_SHORT).show();
                                            clearFields();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RepayActivity.this, "Failed to update loan balance", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RepayActivity.this, "Failed to fetch loan details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFields() {
        etAmountToRepay.setText("");
        etMpesaTransactionCode.setText("");
    }
}
