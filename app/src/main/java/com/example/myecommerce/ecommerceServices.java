package com.example.myecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ecommerceServices extends AppCompatActivity {

    private Button btnAgricProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerce_services);

        btnAgricProducts = findViewById(R.id.btnAgricProducts);

        btnAgricProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agricProducts = new Intent(ecommerceServices.this, ProductList.class);
                startActivity(agricProducts);
            }
        });
    }
}
