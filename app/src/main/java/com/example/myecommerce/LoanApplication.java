// LoanApplicationActivity.java
package com.example.myecommerce;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoanApplication extends AppCompatActivity {

    private EditText etLoanAmount, etKRAPin, etIDNumber, etGuarantorName, etGuarantorPhoneNumber;
    private TextView tvDueDate;
    private CheckBox cbAcceptTerms;
    private Button btnPickDueDate, btnSubmitLoan;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String selectedDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_applications);

        // Initialize views
        etLoanAmount = findViewById(R.id.etLoanAmount);
        etKRAPin = findViewById(R.id.etKRAPin);
        etIDNumber = findViewById(R.id.etIDNumber);
        etGuarantorName = findViewById(R.id.etGuarantorName);
        etGuarantorPhoneNumber = findViewById(R.id.etGuarantorPhoneNumber);
        tvDueDate = findViewById(R.id.tvDueDate);
        cbAcceptTerms = findViewById(R.id.cbAcceptTerms);
        btnPickDueDate = findViewById(R.id.btnPickDueDate);
        btnSubmitLoan = findViewById(R.id.btnSubmitLoan);

        // Initialize Firebase Firestore and Auth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Set up date picker
        btnPickDueDate.setOnClickListener(v -> showDatePickerDialog());

        // Set up submit button
        btnSubmitLoan.setOnClickListener(v -> submitLoanApplication());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDueDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvDueDate.setText("Due Date: " + selectedDueDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void submitLoanApplication() {
        String loanAmountStr = etLoanAmount.getText().toString().trim();
        String kraPin = etKRAPin.getText().toString().trim();
        String idNumber = etIDNumber.getText().toString().trim();
        String guarantorName = etGuarantorName.getText().toString().trim();
        String guarantorPhoneNumber = etGuarantorPhoneNumber.getText().toString().trim();

        if (loanAmountStr.isEmpty() || kraPin.isEmpty() || idNumber.isEmpty() ||
                guarantorName.isEmpty() || guarantorPhoneNumber.isEmpty() || selectedDueDate == null ||
                !cbAcceptTerms.isChecked()) {
            Toast.makeText(this, "Please fill all fields and accept terms", Toast.LENGTH_SHORT).show();
            return;
        }

        double loanAmount = Double.parseDouble(loanAmountStr);
        double interestRate = 0.09;
        double totalLoanBalance = loanAmount + (loanAmount * interestRate);

        // Get current user email
        String userEmail = auth.getCurrentUser().getEmail();
        String loanId = db.collection("loans").document().getId();  // Generate unique ID

        Map<String, Object> loanData = new HashMap<>();
        loanData.put("loanId", loanId);
        loanData.put("email", userEmail);
        loanData.put("amount", loanAmount);
        loanData.put("kraPin", kraPin);
        loanData.put("idNumber", idNumber);
        loanData.put("guarantorName", guarantorName);
        loanData.put("guarantorPhoneNumber", guarantorPhoneNumber);
        loanData.put("dueDate", selectedDueDate);
        loanData.put("totalLoanBalance", totalLoanBalance);
        loanData.put("status", "Pending");
        loanData.put("applicationDate", System.currentTimeMillis());

        // Save to Firestore
        db.collection("loans").document(loanId).set(loanData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Loan application submitted. Wait for admin approval in an hour.", Toast.LENGTH_SHORT).show();
                    finish();  // Close activity
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to submit loan application.", Toast.LENGTH_SHORT).show());
    }
}
