package com.mbj.gestionproduit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    private EditText etProductCode, etLabel, etPrice, etQuantity;
    private Button btnModify, btnDelete, btnReturn;

    private FirebaseFirestore db;

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);

        db = FirebaseFirestore.getInstance();

        etProductCode = findViewById(R.id.edit_etProductCode);
        etLabel = findViewById(R.id.edit_etLabel);
        etPrice = findViewById(R.id.edit_etPrice);
        etQuantity = findViewById(R.id.edit_etQuantity);

        btnModify = findViewById(R.id.edit_btnModify);
        btnDelete = findViewById(R.id.edit_btnDelete);
        btnReturn = findViewById(R.id.edit_btnReturn);

        // Get product from Intent (same as you did)
        Intent intent = getIntent();
        code = intent.getStringExtra("product_code");
        String label = intent.getStringExtra("label");
        double price = intent.getDoubleExtra("price", 0);
        int quantity = intent.getIntExtra("quantity", 0);

        if (code == null || code.trim().isEmpty()) {
            Toast.makeText(this, "Missing product code", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etProductCode.setText(code);
        etLabel.setText(label != null ? label : "");
        etPrice.setText(String.valueOf(price));
        etQuantity.setText(String.valueOf(quantity));

        etProductCode.setEnabled(false);

        btnModify.setOnClickListener(v -> updateProduct());
        btnDelete.setOnClickListener(v -> deleteProduct());
        btnReturn.setOnClickListener(v -> finish());
    }

    private void updateProduct() {
        String newLabel = etLabel.getText().toString().trim();
        if (newLabel.isEmpty()) {
            etLabel.setError("Label required");
            return;
        }

        double newPrice;
        try {
            newPrice = Double.parseDouble(etPrice.getText().toString().trim());
            if (newPrice <= 0) {
                etPrice.setError("Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            etPrice.setError("Invalid price format");
            return;
        }

        int newQuantity;
        try {
            newQuantity = Integer.parseInt(etQuantity.getText().toString().trim());
            if (newQuantity < 0) {
                etQuantity.setError("Quantity cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Invalid quantity format");
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("label", newLabel);
        updates.put("price", newPrice);
        updates.put("quantity", newQuantity);

        btnModify.setEnabled(false);

        db.collection("products").document(code).update(updates).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            btnModify.setEnabled(true);
            Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteProduct() {
        btnDelete.setEnabled(false);

        db.collection("products").document(code).delete().addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            btnDelete.setEnabled(true);
            Toast.makeText(this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
