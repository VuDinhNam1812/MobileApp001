package com.example.vudinhnam_2122110448_android;

import com.bumptech.glide.Glide;
import com.example.vudinhnam_2122110448_android.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vudinhnam_2122110448_android.cart.CartManager;
import com.example.vudinhnam_2122110448_android.models.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDescription;
    private RatingBar ratingBar;
    private ImageButton btnFavorite, btnBack, btnCart;
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
        btnCart = findViewById(R.id.btnCart);
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

        Glide.with(this)
                .load(product.getImage())
                .placeholder(R.drawable.ic_placeholder) // nên có ảnh tạm khi đang load
                .into(imgProduct);
        tvName.setText(product.getTitle());
        tvPrice.setText(product.getFormattedPrice());
        tvDescription.setText(product.getDescription());
        ratingBar.setRating(4.5f); // tạm thời để cố định
        updateFavoriteIcon(product.isFavorite());
    }

    private void setListeners() {
        btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(this, "Added to cart: " + product.getTitle(), Toast.LENGTH_SHORT).show();
        });

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
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
