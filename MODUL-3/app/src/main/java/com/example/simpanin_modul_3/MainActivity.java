package com.example.simpanin_modul_3;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.main);
        View header = findViewById(R.id.headerContainer);
        GridView gridView = findViewById(R.id.gridView);
        TextView tvCount = findViewById(R.id.tvCount);

        // Atur jarak agar konten tidak tertutup status bar / navigation bar
        final int headerTop = header.getPaddingTop();
        final int gridBottom = gridView.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, 0, bars.right, 0);
            header.setPadding(header.getPaddingLeft(), headerTop + bars.top,
                    header.getPaddingRight(), header.getPaddingBottom());
            gridView.setPadding(gridView.getPaddingLeft(), gridView.getPaddingTop(),
                    gridView.getPaddingRight(), gridBottom + bars.bottom);
            return windowInsets;
        });

        // Jalankan GridView
        ImageAdapter adapter = new ImageAdapter(this);
        gridView.setAdapter(adapter);
        tvCount.setText(getString(R.string.label_count, adapter.getCount()));

        adapter.setOnImageClickListener(this::showPreview);
    }

    // Preview gambar layar penuh
    private void showPreview(int position, int imageRes) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        FrameLayout container = new FrameLayout(this);
        container.setBackgroundColor(Color.parseColor("#F00B0C10"));

        ImageView image = new ImageView(this);
        image.setImageResource(imageRes);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setContentDescription(getString(R.string.desc_image, position + 1));
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageParams.setMargins(dp(16), dp(48), dp(16), dp(96));
        container.addView(image, imageParams);

        TextView caption = new TextView(this);
        caption.setText(getString(R.string.preview_caption, position + 1));
        caption.setTextColor(Color.WHITE);
        caption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        FrameLayout.LayoutParams captionParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        captionParams.bottomMargin = dp(48);
        container.addView(caption, captionParams);

        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(container);

        // Animasi muncul
        container.setAlpha(0f);
        image.setScaleX(0.85f);
        image.setScaleY(0.85f);
        dialog.show();
        container.animate().alpha(1f).setDuration(200).start();
        image.animate().scaleX(1f).scaleY(1f).setDuration(250).start();
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }
}