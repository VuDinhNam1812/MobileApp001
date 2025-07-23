package com.example.vudinhnam_2122110448_android;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vudinhnam_2122110448_android.models.Product;
import com.example.vudinhnam_2122110448_android.adapters.ProductAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    private EditText edtSearch;
    private Button btnCategoryAll, btnCategoryChairs, btnCategoryTables, btnCategorySofas;
    private String currentCategory = "All";

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "https://fakestoreapi.com/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupRecyclerView();
        loadProductsFromApi();
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

        productAdapter = new ProductAdapter(this, filteredProducts);
        productAdapter.setOnProductClickListener(this);
        
        recyclerViewProducts.setAdapter(productAdapter);
    }

    private void loadProductsFromApi() {
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://fakestoreapi.com/products";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    allProducts.clear();
                    filteredProducts.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            Product product = new Product();
                            product.setId(obj.getInt("id"));
                            product.setTitle(obj.getString("title"));
                            product.setDescription(obj.getString("description"));
                            product.setPrice(obj.getDouble("price"));
                            product.setCategory(obj.getString("category"));
                            product.setImage(obj.getString("image")); // Lưu link ảnh

                            allProducts.add(product);
                        }

                        // Mặc định hiển thị tất cả
                        filteredProducts.addAll(allProducts);
                        productAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Lỗi khi gọi API: " + error.toString(), Toast.LENGTH_LONG).show();
                });

        mRequestQueue.add(jsonArrayRequest);
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
                    product.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(searchText.toLowerCase());

            if (matchesCategory && matchesSearch) {
                filteredProducts.add(product);
            }
        }

        productAdapter.notifyDataSetChanged();
    }

    //Ham Goi Data API
    private void getData() {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
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
                "Added to favorites: " + product.getTitle() :
                "Removed from favorites: " + product.getTitle();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // TODO: Save to database or SharedPreferences
    }

}