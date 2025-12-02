package com.mbj.gestionproduit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

public class EditProductActivity extends AppCompatActivity {

    private EditText etProductCode, etLabel, etPrice, etQuantity;
    private Button btnModify, btnDelete, btnReturn;
    private DBManager db;

    private long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);

        db = new DBManager(this);

        etProductCode = findViewById(R.id.edit_etProductCode);
        etLabel = findViewById(R.id.edit_etLabel);
        etPrice = findViewById(R.id.edit_etPrice);
        etQuantity = findViewById(R.id.edit_etQuantity);

        btnModify = findViewById(R.id.edit_btnModify);
        btnDelete = findViewById(R.id.edit_btnDelete);
        btnReturn = findViewById(R.id.edit_btnReturn);

        Intent intent = getIntent();
        productId = intent.getLongExtra("product_id", -1);
        String code = intent.getStringExtra("product_code");
        String label = intent.getStringExtra("label");
        double price = intent.getDoubleExtra("price", 0);
        int quantity = intent.getIntExtra("quantity", 0);

        etProductCode.setText(code);
        etLabel.setText(label);
        etPrice.setText(String.valueOf(price));
        etQuantity.setText(String.valueOf(quantity));

        etProductCode.setEnabled(false);

        btnModify.setOnClickListener(v -> {
            String newLabel = etLabel.getText().toString().trim();
            double newPrice;
            int newQuantity;

            try {
                newPrice = Double.parseDouble(etPrice.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                newQuantity = Integer.parseInt(etQuantity.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            int rows = db.update(productId, code, newLabel, newPrice, newQuantity);

            if (rows > 0) {
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }

            finish();
        });

        btnDelete.setOnClickListener(v -> {
            db.delete(productId);
            Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnReturn.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
