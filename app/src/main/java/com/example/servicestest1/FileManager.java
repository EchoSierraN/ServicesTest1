package com.example.servicestest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class FileManager extends AppCompatActivity {
    RecyclerView rvExplorer;
    TextView tvNoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        rvExplorer = findViewById(R.id.rv_explorer);
        tvNoItems = findViewById(R.id.tv_no_items);
    }
}