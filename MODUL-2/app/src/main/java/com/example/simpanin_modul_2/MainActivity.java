package com.example.simpanin_modul_2;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Menjaga tampilan tidak tertutup status bar / navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Menjalankan GridView
        GridView gridView = findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this);
        gridView.setAdapter(adapter);

        // Badge jumlah Bagian di header
        TextView tvCount = findViewById(R.id.tv_count);
        tvCount.setText(adapter.getCount() + " Bagian");

        // Efek "tekan" + toast saat item diketuk
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            view.animate()
                    .scaleX(0.94f).scaleY(0.94f)
                    .setDuration(80)
                    .withEndAction(() -> view.animate()
                            .scaleX(1f).scaleY(1f)
                            .setDuration(80)
                            .start())
                    .start();

            Toast.makeText(this, "Bagian " + (position + 1) + " dipilih",
                    Toast.LENGTH_SHORT).show();
        });
    }
}