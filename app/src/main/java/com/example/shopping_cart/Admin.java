package com.example.shopping_cart;

import static com.example.shopping_cart.DataBaseHelper.COLUMN_PRODUCT_COST;
import static com.example.shopping_cart.DataBaseHelper.COLUMN_PRODUCT_NAME;
import static com.example.shopping_cart.DataBaseHelper.TABLE_PRODUCTS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    private EditText productNameEditText, productCostEditText;
    private Button addProductButton,backButton;
    private ListView productListView;

    private ArrayList<String> productList;
    private ArrayAdapter<String> productAdapter;

    private DataBaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        productNameEditText = findViewById(R.id.product_name);
        productCostEditText = findViewById(R.id.product_cost);
        addProductButton = findViewById(R.id.add_product);
        productListView = findViewById(R.id.products);

        productList = new ArrayList<>();
        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(productAdapter);
        backButton =findViewById(R.id.admin_back);

        dbHelper = new DataBaseHelper(this);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameEditText.getText().toString();
                String productCost = productCostEditText.getText().toString();

                if (!productName.isEmpty() && !productCost.isEmpty()) {
                    boolean insertResult = dbHelper.insertProduct(productName, productCost);

                    if (insertResult) {
                        productList.add(productName + " - $" + productCost);
                        productAdapter.notifyDataSetChanged();
                        Toast.makeText(Admin.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Admin.this, "Product addition failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Admin.this, "Please enter product name and cost", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadProductList();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to login page
                Intent intent = new Intent(Admin.this, Login.class);
                startActivity(intent);

            }
        });
    }

    private void loadProductList() {
        productList.clear(); // Clear existing list

        // Retrieve products from the database and add them to productList
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_COST};
        Cursor cursor = db.query(TABLE_PRODUCTS, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
            int costIndex = cursor.getColumnIndex(COLUMN_PRODUCT_COST);

            do {
                String productName = cursor.getString(nameIndex);
                String productCost = cursor.getString(costIndex);

                productList.add(productName + " - $" + productCost);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        productAdapter.notifyDataSetChanged(); // Update the ListView
    }



    // ... (other methods)
}

