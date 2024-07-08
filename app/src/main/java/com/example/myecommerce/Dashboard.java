package com.example.myecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView dashboardGreetings;
    private ImageButton btnProfile, btnLogout;
    private Button btnLoanServices, btnCommunityEngagement, btnAgricProducts;

    // RecyclerView and Adapter for articles
    private RecyclerView recyclerViewArticles;
    private ArticleAdapter articleAdapter;
    private ArrayList<Article> articleList;
    private DatabaseReference databaseArticles;

   // private LoanViewModel loanViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboardGreetings = findViewById(R.id.dashboardGreetings);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnLoanServices = findViewById(R.id.btnLoanServices);
        btnCommunityEngagement = findViewById(R.id.btnCommunityEngagement);
        btnAgricProducts = findViewById(R.id.btnAgricProducts);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Setup RecyclerView for articles
        recyclerViewArticles = findViewById(R.id.recyclerViewArticles);
        recyclerViewArticles.setLayoutManager(new LinearLayoutManager(this));

        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleList);
        recyclerViewArticles.setAdapter(articleAdapter);

        databaseArticles = FirebaseDatabase.getInstance().getReference("articles");
        fetchArticles();

        // Initialize the ViewModel
        //loanViewModel = new ViewModelProvider(this).get(LoanViewModel.class);

        // Display user name on dashboard
        loadUserName();

        // Click listeners for buttons
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(Dashboard.this, ProfileSection.class);
                startActivity(profile);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnLoanServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loanServices = new Intent(Dashboard.this, LoanServices.class);
                startActivity(loanServices);
            }
        });

        btnCommunityEngagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent community = new Intent(Dashboard.this, CommunityContributions.class);
                startActivity(community);
            }
        });

        btnAgricProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agricProducts = new Intent(Dashboard.this, ProductList.class);
                startActivity(agricProducts);
            }
        });

       // handleNotificationIntent();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(Dashboard.this, Login.class);
        startActivity(login);
        finish();
    }

    private void loadUserName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    if (firstName != null) {
                        dashboardGreetings.setText("Hi " + firstName + ",");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Dashboard.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(Dashboard.this, Login.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        }
    }

    private void fetchArticles() {
        databaseArticles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articleList.clear();
                for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                    Article article = articleSnapshot.getValue(Article.class);
                    articleList.add(article);
                }
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "Failed to fetch articles.", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void handleNotificationIntent() {
//        // Handle notification clicks and provide user feedback
//        // This method can be expanded to handle different types of notifications
//        Toast.makeText(this, "Notification Clicked", Toast.LENGTH_SHORT).show();
//    }
}
