package com.mbj.gestionproduit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class AddProductActivity extends AppCompatActivity {

    private EditText etProductCode, etLabel, etPrice, etQuantity;
    private Button btnSubmit, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        etProductCode = findViewById(R.id.add_etProductCode);
        etLabel = findViewById(R.id.add_etLabel);
        etPrice = findViewById(R.id.add_etPrice);
        etQuantity = findViewById(R.id.add_etQuantity);
        btnSubmit = findViewById(R.id.add_btnSubmit);
        btnReturn = findViewById(R.id.add_btnReturn);
        btnReturn.setOnClickListener(v -> {
            finish();
        });

        btnSubmit.setOnClickListener(v -> {

            String code = etProductCode.getText().toString().trim();
            String label = etLabel.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();

            boolean hasError = false;
            if (code.isEmpty()) {
                etProductCode.setError("Product Code Required");
                hasError = true;
            }
            if (label.isEmpty()) {
                etLabel.setError("Label Required");
                hasError = true;
            }
            if (priceStr.isEmpty()) {
                etPrice.setError("Price Required");
                hasError = true;
            }
            if (quantityStr.isEmpty()) {
                etQuantity.setError("Quantity Required");
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


            DBManager db = new DBManager(this);
            long id = db.add(code, label, price, quantity);
            db.close();

            if (id != -1) {
                Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erreur : code already used",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}