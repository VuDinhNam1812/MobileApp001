package com.example.vudinhnam_2122110448_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername, txtPassword;
    private Button btnLogin;

    private static final String LOGIN_URL = "https://fakestoreapi.com/users"; // ✅ Thay URL API của bạn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(username, password);
        });
    }

    private void loginUser(String username, String password) {
        String url = "https://fakestoreapi.com/users";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        boolean found = false;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject user = jsonArray.getJSONObject(i);
                            String apiUsername = user.getString("username");
                            String apiPassword = user.getString("password");
                            if (username.equals(apiUsername) && password.equals(apiPassword)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, HomeActivity.class));
                        } else {
                            Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(stringRequest);
    }

}
