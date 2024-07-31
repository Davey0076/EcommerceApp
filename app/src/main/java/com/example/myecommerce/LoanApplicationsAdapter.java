// LoanApplicationsAdapter.java
package com.example.myecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LoanApplicationsAdapter extends RecyclerView.Adapter<LoanApplicationsAdapter.LoanViewHolder> {

    private List<LoanDisplay> loanList;
    private Context context;
    private FirebaseFirestore db;

    public LoanApplicationsAdapter(List<LoanDisplay> loanList, Context context) {
        this.loanList = loanList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loans_application, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        LoanDisplay loan = loanList.get(position);

        holder.tvEmail.setText("Email: " + loan.getEmail());
        holder.tvAmount.setText("Amount: KSHS. " + loan.getAmount());
        holder.tvDueDate.setText("Due Date: " + loan.getDueDate());
        holder.tvGuarantorName.setText("Guarantor: " + loan.getGuarantorName());
        holder.tvGuarantorPhone.setText("Guarantor Phone: " + loan.getGuarantorPhoneNumber());

        holder.btnApprove.setOnClickListener(v -> updateLoanStatus(loan, "Approved"));
        holder.btnReject.setOnClickListener(v -> updateLoanStatus(loan, "Rejected"));
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    private void updateLoanStatus(LoanDisplay loan, String status) {
        db.collection("loans").document(loan.getLoanId())
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    loanList.remove(loan);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Loan " + status, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update loan status.", Toast.LENGTH_SHORT).show());
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmail, tvAmount, tvDueDate, tvGuarantorName, tvGuarantorPhone;
        Button btnApprove, btnReject;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvGuarantorName = itemView.findViewById(R.id.tvGuarantorName);
            tvGuarantorPhone = itemView.findViewById(R.id.tvGuarantorPhone);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
