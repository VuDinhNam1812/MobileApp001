package com.example.vudinhnam_2122110448_android.cart;

import com.example.vudinhnam_2122110448_android.models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Product> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product) {
        if (!cartItems.contains(product)) {
            cartItems.add(product);
        }
    }

    public void removeFromCart(Product product) {
        cartItems.remove(product);
    }

    public List<Product> getCartItems() {
        return cartItems;
    }
}
