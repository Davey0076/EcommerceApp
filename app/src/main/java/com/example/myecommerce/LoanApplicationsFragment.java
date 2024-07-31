// LoanApplicationsFragment.java
package com.example.myecommerce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoanApplicationsFragment extends Fragment {

    private RecyclerView rvLoanApplications;
    private LoanApplicationsAdapter adapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_applications, container, false);

        rvLoanApplications = view.findViewById(R.id.rvLoanApplications);
        rvLoanApplications.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();

        fetchLoanApplications();

        return view;
    }

    private void fetchLoanApplications() {
        db.collection("loans")
                .whereEqualTo("status", "Pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<LoanDisplay> loanList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LoanDisplay loan = document.toObject(LoanDisplay.class);
                            loanList.add(loan);
                        }
                        adapter = new LoanApplicationsAdapter(loanList, getContext());
                        rvLoanApplications.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch loan applications.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
