package com.example.vudinhnam_2122110448_android;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vudinhnam_2122110448_android.adapters.CartAdapter;
import com.example.vudinhnam_2122110448_android.cart.CartManager;
import com.example.vudinhnam_2122110448_android.models.Product;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView tvSubtotal;
    private ImageButton btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        btnBackHome = findViewById(R.id.btnBackHome); // Tìm ID
        btnBackHome.setOnClickListener(v -> finish());  // Quay về Home


        List<Product> cartItems = CartManager.getInstance().getCartItems();

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems,this::calculateSubtotal);
        recyclerViewCart.setAdapter(cartAdapter);

        calculateSubtotal();
    }

    private void calculateSubtotal() {
        double total = 0.0;
        for (Product p : CartManager.getInstance().getCartItems()) {
            total += p.getPrice() * p.getQuantity();
        }
        double shippingFee = 30.0;
        double subtotal = total + shippingFee;
        tvSubtotal.setText("Subtotal: $" + subtotal);
    }
}
