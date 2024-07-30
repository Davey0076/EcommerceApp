package com.example.myecommerce;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class  LoanViewModel extends ViewModel {
    private final MutableLiveData<Double> loanBalance = new MutableLiveData<>();

    public void setLoanBalance(double balance) {
        loanBalance.setValue(balance);
    }

    public LiveData<Double> getLoanBalance() {
        return loanBalance;
    }
}
