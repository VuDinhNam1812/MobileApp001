package com.example.vudinhnam_2122110448_android;

import com.example.vudinhnam_2122110448_android.models.Product;
import com.example.vudinhnam_2122110448_android.adapters.ProductAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> allProducts;
    private List<Product> filteredProducts;

    private EditText edtSearch;
    private Button btnCategoryAll, btnCategoryChairs, btnCategoryTables, btnCategorySofas;
    private String currentCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupRecyclerView();
        createSampleData();
        setupCategoryButtons();
        setupSearchFunction();
    }

    private void initViews() {
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        edtSearch = findViewById(R.id.edtSearch);
        btnCategoryAll = findViewById(R.id.btnCategoryAll);
        btnCategoryChairs = findViewById(R.id.btnCategoryChairs);
        btnCategoryTables = findViewById(R.id.btnCategoryTables);
        btnCategorySofas = findViewById(R.id.btnCategorySofas);
    }

    private void setupRecyclerView() {
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        filteredProducts = new ArrayList<>();
        productAdapter = new ProductAdapter(this, filteredProducts);
        productAdapter.setOnProductClickListener(this);
        recyclerViewProducts.setAdapter(productAdapter);
    }

    private void createSampleData() {
        allProducts = new ArrayList<>();
        // ❌ KHÔNG tạo lại filteredProducts ở đây
        // filteredProducts = new ArrayList<>(); ← bỏ dòng này

        // Chairs
        allProducts.add(new Product(1, "Ghế dựa làm việc", "Ergonomic office chair", 299.99, R.drawable.chair1, "Chairs"));
        allProducts.add(new Product(2, "Ghế lười", "Elegant dining chair", 599.99, R.drawable.chair2, "Chairs"));
        allProducts.add(new Product(3, "Ghế nâu sang trọng", "Gaming chair RGB", 449.99, R.drawable.chair3, "Chairs"));

        // Tables
        allProducts.add(new Product(4, "Bàn kính", "Coffee table with wooden legs", 199.99, R.drawable.table1, "Tables"));
        allProducts.add(new Product(5, "Bàn ăn mở rộng", "Dining table 6-8 people", 899.99, R.drawable.table2, "Tables"));
        allProducts.add(new Product(6, "Bàn học", "Desk with drawers", 349.99, R.drawable.table3, "Tables"));

        // Sofas
        allProducts.add(new Product(7, "Sofa trắng", "Fabric sofa gray", 1299.99, R.drawable.chair4, "Sofas"));
        allProducts.add(new Product(8, "Sofa góc", "L-shaped sectional sofa", 1899.99, R.drawable.chair5, "Sofas"));

        // Hiển thị tất cả ban đầu
        filteredProducts.addAll(allProducts);
        productAdapter.notifyDataSetChanged();
    }


    private void setupCategoryButtons() {
        btnCategoryAll.setOnClickListener(v -> filterByCategory("All"));
        btnCategoryChairs.setOnClickListener(v -> filterByCategory("Chairs"));
        btnCategoryTables.setOnClickListener(v -> filterByCategory("Tables"));
        btnCategorySofas.setOnClickListener(v -> filterByCategory("Sofas"));
    }

    private void setupSearchFunction() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterByCategory(String category) {
        currentCategory = category;
        updateCategoryButtonStyles(category);
        filterProducts(edtSearch.getText().toString());
    }

    private void updateCategoryButtonStyles(String selectedCategory) {
        // Reset all buttons
        resetButtonStyle(btnCategoryAll);
        resetButtonStyle(btnCategoryChairs);
        resetButtonStyle(btnCategoryTables);
        resetButtonStyle(btnCategorySofas);

        // Highlight selected button
        Button selectedButton = null;
        switch (selectedCategory) {
            case "All":
                selectedButton = btnCategoryAll;
                break;
            case "Chairs":
                selectedButton = btnCategoryChairs;
                break;
            case "Tables":
                selectedButton = btnCategoryTables;
                break;
            case "Sofas":
                selectedButton = btnCategorySofas;
                break;
        }

        if (selectedButton != null) {
            selectedButton.setBackgroundTintList(getColorStateList(android.R.color.holo_orange_dark));
            selectedButton.setTextColor(getColor(android.R.color.white));
        }
    }

    private void resetButtonStyle(Button button) {
        button.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        button.setTextColor(getColor(android.R.color.black));
    }

    private void filterProducts(String searchText) {
        filteredProducts.clear();

        for (Product product : allProducts) {
            boolean matchesCategory = currentCategory.equals("All") ||
                    product.getCategory().equals(currentCategory);
            boolean matchesSearch = searchText.isEmpty() ||
                    product.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(searchText.toLowerCase());

            if (matchesCategory && matchesSearch) {
                filteredProducts.add(product);
            }
        }

        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", product);  // Truyền nguyên object
        startActivity(intent);
    }


    @Override
    public void onFavoriteClick(Product product) {
        // Handle favorite click
        String message = product.isFavorite() ?
                "Added to favorites: " + product.getName() :
                "Removed from favorites: " + product.getName();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // TODO: Save to database or SharedPreferences
    }
}