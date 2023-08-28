package com.example.shopping_cart;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

// LoginActivity.java
public class Login extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton,backButton;
    private RadioButton customerRadioButton, adminRadioButton;
    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private DataBaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        customerRadioButton =findViewById(R.id.log_cust_id);
        adminRadioButton =findViewById(R.id.log_ad_id);
        loginButton = findViewById(R.id.login);
        backButton =findViewById(R.id.login_back);
        dbHelper = new DataBaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String role = customerRadioButton.isChecked() ? "customer" : "admin";


                if (validateUser(username, password,role))
                {
                    // User is authenticated
                    if (isUserAdmin(username))
                    {
                        // Redirect to admin interface
                        Toast.makeText(Login.this, "Login sucessfull.", Toast.LENGTH_LONG).show();
                        Intent adminIntent = new Intent(Login.this, Admin.class);
                        startActivity(adminIntent);
                    }
                    else
                    {
                        // Redirect to customer interface
                        Toast.makeText(Login.this, "Login sucessfull.", Toast.LENGTH_LONG).show();
                        Intent customerIntent = new Intent(Login.this, Customer.class);
                        startActivity(customerIntent);
                    }
                }
                else
                {
                    // Invalid credentials
                    loginAttempts++;
                    if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                        Toast.makeText(Login.this, "Maximum login attempts exceeded. Please register again.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, Register.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Invalid username or password. Remaining attempts: " + (MAX_LOGIN_ATTEMPTS - loginAttempts), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to login page
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private boolean validateUser(String username, String password,String role) {
        return dbHelper.checkUserCredentials(username, password,role);
    }

    private boolean isUserAdmin(String username) {
        return dbHelper.isUserAdmin(username);
    }


}
