package com.example.myecommerce;

public class LoanDisplay {
    private String loanId;
    private String email;
    private double amount;
    private String dueDate;
    private String guarantorName;
    private String guarantorPhoneNumber;
    private String status;
    private double totalLoanBalance;

    // Default constructor is required for Firestore
    public LoanDisplay() {
    }

    public LoanDisplay(String loanId, String email, double amount, String dueDate, String guarantorName, String guarantorPhoneNumber, String status, double totalLoanBalance) {
        this.loanId = loanId;
        this.email = email;
        this.amount = amount;
        this.dueDate = dueDate;
        this.guarantorName = guarantorName;
        this.guarantorPhoneNumber = guarantorPhoneNumber;
        this.status = status;
        this.totalLoanBalance = totalLoanBalance;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getGuarantorPhoneNumber() {
        return guarantorPhoneNumber;
    }

    public void setGuarantorPhoneNumber(String guarantorPhoneNumber) {
        this.guarantorPhoneNumber = guarantorPhoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalLoanBalance() {
        return totalLoanBalance;
    }

    public void setTotalLoanBalance(double totalLoanBalance) {
        this.totalLoanBalance = totalLoanBalance;
    }
}
