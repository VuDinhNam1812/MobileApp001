package com.example.vudinhnam_2122110448_android;

import com.example.vudinhnam_2122110448_android.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vudinhnam_2122110448_android.models.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDescription;
    private RatingBar ratingBar;
    private ImageButton btnFavorite, btnBack;
    private Button btnAddToCart;

    private Product product;  // bạn truyền qua Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();
        getProductFromIntent();
        bindProductToUI();
        setListeners();
    }


    private void initViews() {
        imgProduct = findViewById(R.id.imgProduct);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        ratingBar = findViewById(R.id.ratingBar);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);
    }

    private void getProductFromIntent() {
        product = (Product) getIntent().getSerializableExtra("product");
        if (product == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void bindProductToUI() {
        if (product == null) return;

        imgProduct.setImageResource(product.getImageResId());
        tvName.setText(product.getName());
        tvPrice.setText(product.getFormattedPrice());
        tvDescription.setText(product.getDescription());
        ratingBar.setRating(4.5f); // tạm thời để cố định
        updateFavoriteIcon(product.isFavorite());
    }

    private void setListeners() {
        btnAddToCart.setOnClickListener(v ->
                Toast.makeText(this, "Added to cart: " + product.getName(), Toast.LENGTH_SHORT).show()
        );

        btnFavorite.setOnClickListener(v -> {
            product.setFavorite(!product.isFavorite());
            updateFavoriteIcon(product.isFavorite());
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void updateFavoriteIcon(boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }
}
