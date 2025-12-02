package com.mbj.gestionproduit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button btnAddProduct;
    private ListView listViewProducts;
    private SimpleCursorAdapter adapter;
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        listViewProducts = findViewById(R.id.listViewProducts);


        db = new DBManager(this);
        Cursor cursor = db.getAllProducts();

        String[] fromColumns = new String[]{
                DatabaseHelper.COL_LABEL,
                DatabaseHelper.COL_PRICE,
                DatabaseHelper.COL_QTY
        };

        int[] toViews = new int[]{
                R.id.item_name,
                R.id.item_price,
                R.id.item_quantity
        };

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.item_product,
                cursor,
                fromColumns,
                toViews,
                0
        );

        listViewProducts.setAdapter(adapter);

        listViewProducts.setOnItemClickListener((parent, view, position, id) -> {
            cursor.moveToPosition(position);

            String code = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CODE));
            String label = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LABEL));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QTY));

            Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
            intent.putExtra("product_code", code);
            intent.putExtra("label", label);
            intent.putExtra("price", price);
            intent.putExtra("quantity", quantity);
            startActivity(intent);
        });

    }

    private void refreshList() {
        Cursor newCursor = db.getAllProducts();
        adapter.changeCursor(newCursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.getCursor().close();
        db.close();
    }
}