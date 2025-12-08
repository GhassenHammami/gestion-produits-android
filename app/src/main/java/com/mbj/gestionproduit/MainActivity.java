package com.mbj.gestionproduit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button btnAddProduct;
    private ListView listViewProducts;

    private FirebaseFirestore db;
    private ListenerRegistration registration;

    private ArrayList<HashMap<String, Object>> data;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        btnAddProduct = findViewById(R.id.btnAddProduct);
        listViewProducts = findViewById(R.id.listViewProducts);

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        data = new ArrayList<>();

        // Keys in each map -> which TextViews in item layout
        String[] from = new String[]{"label", "priceText", "quantityText"};
        int[] to = new int[]{R.id.item_name, R.id.item_price, R.id.item_quantity};

        adapter = new SimpleAdapter(this, data, R.layout.item_product, from, to);
        listViewProducts.setAdapter(adapter);

        listViewProducts.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, Object> item = data.get(position);

            String code = (String) item.get("code");
            String label = (String) item.get("label");
            double price = (double) item.get("price");      // stored as Double in map
            int quantity = (int) item.get("quantity");      // stored as Integer in map

            Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
            intent.putExtra("product_code", code);
            intent.putExtra("label", label);
            intent.putExtra("price", price);
            intent.putExtra("quantity", quantity);
            startActivity(intent);
        });

        attachRealtimeListener();
    }

    private void attachRealtimeListener() {
        registration = db.collection("products").addSnapshotListener((snap, e) -> {
            if (e != null) {
                Toast.makeText(this, "Load failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (snap == null) return;

            data.clear();

            for (DocumentSnapshot doc : snap.getDocuments()) {
                String code = doc.getId();

                String label = doc.getString("label");

                Double priceD = doc.getDouble("price");
                Long quantityL = doc.getLong("quantity");

                double price = (priceD != null) ? priceD : 0.0;
                int quantity = (quantityL != null) ? quantityL.intValue() : 0;

                HashMap<String, Object> map = new HashMap<>();
                map.put("code", code);
                map.put("label", label != null ? label : "");
                map.put("price", price);
                map.put("quantity", quantity);

                // these are just for display formatting in the list item
                map.put("priceText", String.valueOf(price));
                map.put("quantityText", String.valueOf(quantity));

                data.add(map);
            }

            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registration != null) registration.remove();
    }
}
