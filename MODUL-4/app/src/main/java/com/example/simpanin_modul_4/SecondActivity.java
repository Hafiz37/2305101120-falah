package com.example.simpanin_modul_4;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SecondActivity extends AppCompatActivity {

    public static final String EXTRA_SUMBER = "extra_sumber";
    public static final String EXTRA_GAMBAR = "extra_gambar";

    private TextInputLayout tilJudul;
    private TextInputLayout tilDeskripsi;
    private TextInputEditText etJudul;
    private TextInputEditText etDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        View root = findViewById(R.id.main);
        View header = findViewById(R.id.header);
        View scroll = findViewById(R.id.scroll);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        applyHeaderGradient(header);
        applyInsets(root, header, scroll);

        tilJudul = findViewById(R.id.tilJudul);
        tilDeskripsi = findViewById(R.id.tilDeskripsi);
        etJudul = findViewById(R.id.etJudul);
        etDeskripsi = findViewById(R.id.etDeskripsi);

        // Data dari intent
        String sumber = getIntent().getStringExtra(EXTRA_SUMBER);
        int gambar = getIntent().getIntExtra(EXTRA_GAMBAR, 0);

        TextView tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setText(getString(R.string.second_message, sumber != null ? sumber : "-"));

        if (gambar != 0) {
            findViewById(R.id.cardPreview).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.imgPreview)).setImageResource(gambar);
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> simpan());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void simpan() {
        String judul = etJudul.getText() == null ? "" : etJudul.getText().toString().trim();
        String deskripsi = etDeskripsi.getText() == null ? "" : etDeskripsi.getText().toString().trim();

        tilJudul.setError(null);
        tilDeskripsi.setError(null);

        if (judul.isEmpty()) {
            tilJudul.setError(getString(R.string.error_judul_kosong));
            return;
        }
        if (judul.length() > 100) {
            tilJudul.setError(getString(R.string.error_judul_panjang));
            return;
        }
        if (deskripsi.length() > 2000) {
            tilDeskripsi.setError(getString(R.string.error_deskripsi_panjang));
            return;
        }

        Toast.makeText(this, getString(R.string.toast_saved, judul), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void applyHeaderGradient(View header) {
        float r = 28 * getResources().getDisplayMetrics().density;
        GradientDrawable bg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        ContextCompat.getColor(this, R.color.simpanin_gradient_start),
                        ContextCompat.getColor(this, R.color.simpanin_gradient_end)
                });
        bg.setCornerRadii(new float[]{0, 0, 0, 0, r, r, r, r});
        header.setBackground(bg);
    }

    private void applyInsets(View root, View header, View scroll) {
        final int headerBottom = header.getPaddingBottom();
        final int sLeft = scroll.getPaddingLeft();
        final int sTop = scroll.getPaddingTop();
        final int sRight = scroll.getPaddingRight();
        final int sBottom = scroll.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            header.setPadding(bars.left, bars.top, bars.right, headerBottom);
            scroll.setPadding(sLeft + bars.left, sTop, sRight + bars.right,
                    sBottom + Math.max(bars.bottom, ime.bottom));
            return insets;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}