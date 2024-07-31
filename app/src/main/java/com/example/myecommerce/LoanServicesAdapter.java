package com.example.myecommerce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;

public class LoanServicesAdapter extends RecyclerView.Adapter<LoanServicesAdapter.LoanViewHolder> {

    private List<LoanDisplay> loanList;
    private Context context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public LoanServicesAdapter(List<LoanDisplay> loanList, Context context) {
        this.loanList = loanList;
        this.context = context;
        this.firestore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loanservices, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        LoanDisplay loan = loanList.get(position);
        holder.tvLoanAmount.setText("Loan Amount: KSHS. " + loan.getTotalLoanBalance());
        holder.tvDueDate.setText("Due Date: " + loan.getDueDate());

        holder.btnRepayLoan.setOnClickListener(v -> {
            Intent intent = new Intent(context, RepayActivity.class);
            intent.putExtra("loanId", loan.getLoanId());
            context.startActivity(intent);
        });

        // Fetch and update loan balance in real-time
        String email = auth.getCurrentUser().getEmail();
        firestore.collection("loans")
                .whereEqualTo("email", email)
                .whereEqualTo("loanId", loan.getLoanId())
                .addSnapshotListener((QuerySnapshot snapshots, FirebaseFirestoreException e) -> {
                    if (e != null) {
                        return;
                    }
                    for (QueryDocumentSnapshot doc : snapshots) {
                        if (doc.exists()) {
                            double updatedBalance = doc.getDouble("totalLoanBalance");
                            holder.tvLoanAmount.setText("Loan Amount: KSHS. " + updatedBalance);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoanAmount, tvDueDate;
        Button btnRepayLoan;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoanAmount = itemView.findViewById(R.id.tvLoanAmount);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            btnRepayLoan = itemView.findViewById(R.id.btnRepayLoan);
        }
    }
}
