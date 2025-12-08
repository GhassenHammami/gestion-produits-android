package com.mbj.gestionproduit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddProductActivity extends AppCompatActivity {

    private EditText etProductCode, etLabel, etPrice, etQuantity;
    private Button btnSubmit, btnReturn;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        db = FirebaseFirestore.getInstance();

        etProductCode = findViewById(R.id.add_etProductCode);
        etLabel = findViewById(R.id.add_etLabel);
        etPrice = findViewById(R.id.add_etPrice);
        etQuantity = findViewById(R.id.add_etQuantity);

        btnSubmit = findViewById(R.id.add_btnSubmit);
        btnReturn = findViewById(R.id.add_btnReturn);

        btnReturn.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String code = etProductCode.getText().toString().trim();
        String label = etLabel.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        boolean hasError = false;

        if (code.isEmpty()) {
            etProductCode.setError("Product code required");
            hasError = true;
        }
        if (label.isEmpty()) {
            etLabel.setError("Label required");
            hasError = true;
        }
        if (priceStr.isEmpty()) {
            etPrice.setError("Price required");
            hasError = true;
        }
        if (quantityStr.isEmpty()) {
            etQuantity.setError("Quantity required");
            hasError = true;
        }
        if (hasError) return;

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                etPrice.setError("Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            etPrice.setError("Invalid price format");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                etQuantity.setError("Quantity cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Invalid quantity format");
            return;
        }

        Product product = new Product(code, label, price, quantity);

        btnSubmit.setEnabled(false);

        db.collection("products").document().set(product).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                    finish();
                }

        ).addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}
