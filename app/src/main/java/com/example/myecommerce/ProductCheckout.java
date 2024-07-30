package com.example.myecommerce;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProductCheckout extends AppCompatActivity {

    private ImageView productImage;
    private TextView txtProductName, productPrice, emailAddress;
    private Spinner location;
    private EditText phoneNumber;
    private Button btnSubmit;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_checkout);

        productImage = findViewById(R.id.productImage);
        txtProductName = findViewById(R.id.txtProductName);
        productPrice = findViewById(R.id.productPrice);
        emailAddress = findViewById(R.id.emailaddress);
        location = findViewById(R.id.location);
        phoneNumber = findViewById(R.id.phoneNumber);
        btnSubmit = findViewById(R.id.btnSubmit);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Populate the spinner with town names
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kenyan_towns, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter);

        //show user emails address
        String userEmail = mAuth.getCurrentUser().getEmail();
        emailAddress.setText(userEmail);

        // Dummy data for product details
        String productNameStr = getIntent().getStringExtra("productName");
        double productPriceDouble = getIntent().getDoubleExtra("productPrice", 0.0);
        String productImageUrl = getIntent().getStringExtra("productImage");


        txtProductName.setText(productNameStr);
        productPrice.setText(String.valueOf(productPriceDouble));

        Glide.with(this)
                .load(productImageUrl)
                .into(productImage);





        btnSubmit.setOnClickListener(view -> submitOrder());
    }



    private void submitOrder() {
        String userEmail = emailAddress.getText().toString();
        String phone = phoneNumber.getText().toString();
        String selectedLocation = location.getSelectedItem().toString();
        String productName = txtProductName.getText().toString();
        double price = Double.parseDouble(productPrice.getText().toString());

        // Prepare data to save
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("email", userEmail);
        orderDetails.put("phone", phone);
        orderDetails.put("location", selectedLocation);
        orderDetails.put("productName", productName);
        orderDetails.put("price", price);

        // Save to Firestore
        db.collection("orders")
                .add(orderDetails)
                .addOnSuccessListener(documentReference -> generateReceipt(documentReference))
                .addOnFailureListener(e -> Toast.makeText(ProductCheckout.this, "Order submission failed.", Toast.LENGTH_SHORT).show());
    }

    private void generateReceipt(DocumentReference documentReference) {
        // Fetch the saved order and generate a receipt
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            StringBuilder receipt = new StringBuilder();
            receipt.append("Receipt\n");
            receipt.append("--------\n");
            receipt.append("Email: ").append(documentSnapshot.getString("email")).append("\n");
            receipt.append("Phone: ").append(documentSnapshot.getString("phone")).append("\n");
            receipt.append("Location: ").append(documentSnapshot.getString("location")).append("\n");
            receipt.append("Product: ").append(documentSnapshot.getString("productName")).append("\n");
            receipt.append("Price: Kshs.").append(documentSnapshot.getDouble("price")).append("\n");

            // Display the receipt
            Toast.makeText(ProductCheckout.this, receipt.toString(), Toast.LENGTH_LONG).show();
        });
    }
}
