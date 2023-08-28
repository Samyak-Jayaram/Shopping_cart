package com.example.shopping_cart;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private RadioButton customerRadioButton, adminRadioButton;
    private Button registerButton,backButton;

    private DataBaseHelper dbHelper;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        customerRadioButton = findViewById(R.id.reg_cust_id);
        adminRadioButton = findViewById(R.id.reg_ad_id);
        registerButton = findViewById(R.id.register_button);
        backButton =findViewById(R.id.register_back);
        dbHelper = new DataBaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String role = customerRadioButton.isChecked() ? "customer" : "admin";

                // Save user info to SQLite database
                boolean insertResult = dbHelper.insertUser(username, password, role);

                if (insertResult ==true) {
                    // Data inserted successfully
                    Toast.makeText(Register.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to login page
                    Intent intent = new Intent(Register.this, Login.class);
                    intent.putExtra("username", username); // Pass username if needed
                    startActivity(intent);
                } else {
                    // Data insertion failed
                    Toast.makeText(Register.this, "Data insertion failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to login page
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
