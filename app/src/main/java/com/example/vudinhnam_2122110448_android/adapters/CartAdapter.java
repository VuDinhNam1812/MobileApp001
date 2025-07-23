package com.example.vudinhnam_2122110448_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vudinhnam_2122110448_android.R;
import com.example.vudinhnam_2122110448_android.cart.CartManager;
import com.example.vudinhnam_2122110448_android.models.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Product> cartItems;
    private final OnCartChangedListener onCartChangedListener;

    public interface OnCartChangedListener {
        void onCartChanged();
    }

    public CartAdapter(List<Product> cartItems, OnCartChangedListener listener) {
        this.cartItems = cartItems;
        this.onCartChangedListener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product product = cartItems.get(position);

        holder.checkBox.setChecked(true); // Luôn tick mặc định
        holder.tvName.setText(product.getTitle());
        holder.tvPrice.setText("$" + product.getPrice());
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        Glide.with(holder.itemView.getContext())
                .load(product.getImage()) // là URL từ API
                .placeholder(R.drawable.ic_placeholder) // ảnh chờ khi load
                .into(holder.imgProduct);

        // Cộng
        holder.btnIncrease.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            notifyItemChanged(position);
            onCartChangedListener.onCartChanged();
        });

        // Trừ
        holder.btnDecrease.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                notifyItemChanged(position);
                onCartChangedListener.onCartChanged();
            }
        });

        // Xoá
        holder.btnRemove.setOnClickListener(v -> {
            cartItems.remove(position);
            CartManager.getInstance().removeFromCart(product);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            onCartChangedListener.onCartChanged();
        });

        // Tick chọn thay đổi subtotal nếu cần
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            onCartChangedListener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnIncrease, btnDecrease, btnRemove;
        CheckBox checkBox;

        public CartViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
