package com.example.simpanin_modul_4;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ImageAdapter adapter;
    private TextView tvSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate dipanggil");

        View root = findViewById(R.id.main);
        View header = findViewById(R.id.header);
        GridView gridView = findViewById(R.id.gridView);
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        tvSubtitle = findViewById(R.id.tvSubtitle);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        applyHeaderGradient(header);
        applyInsets(root, header, gridView);

        // Adapter + favorit
        adapter = new ImageAdapter(this);
        adapter.setOnChangeListener(this::updateSubtitle);
        gridView.setAdapter(adapter);
        updateSubtitle();

        // Filter chip
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if (id == R.id.chipAll) {
                adapter.setFilter(0, 14);
            } else if (id == R.id.chip1) {
                adapter.setFilter(0, 4);
            } else if (id == R.id.chip2) {
                adapter.setFilter(5, 9);
            } else if (id == R.id.chip3) {
                adapter.setFilter(10, 14);
            }
            gridView.smoothScrollToPosition(0);
            updateSubtitle();
        });

        // Klik gambar -> Snackbar dengan aksi menuju SecondActivity
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            int nomor = (int) id + 1;
            int resId = (Integer) adapter.getItem(position);

            Snackbar.make(root, getString(R.string.snackbar_foto, nomor), Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_action, v -> {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra(SecondActivity.EXTRA_SUMBER, getString(R.string.label_foto, nomor));
                        intent.putExtra(SecondActivity.EXTRA_GAMBAR, resId);
                        startActivity(intent);
                    })
                    .show();
        });
    }

    private void updateSubtitle() {
        tvSubtitle.setText(getString(R.string.subtitle_count,
                adapter.getCount(), adapter.getFavoriteCount()));
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

    private void applyInsets(View root, View header, GridView gridView) {
        final int headerBottom = header.getPaddingBottom();
        final int gridLeft = gridView.getPaddingLeft();
        final int gridTop = gridView.getPaddingTop();
        final int gridRight = gridView.getPaddingRight();
        final int gridBottom = gridView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            header.setPadding(bars.left, bars.top, bars.right, headerBottom);
            gridView.setPadding(gridLeft + bars.left, gridTop,
                    gridRight + bars.right, gridBottom + bars.bottom);
            return insets;
        });
    }

    // ===================== Siklus hidup activity =====================

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart dipanggil");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume dipanggil");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause dipanggil");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop dipanggil");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart dipanggil");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy dipanggil");
    }

    // ============================== Menu ==============================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_second) {
            // Intent eksplisit: pindah ke SecondActivity
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(SecondActivity.EXTRA_SUMBER, getString(R.string.menu_second));
            startActivity(intent);
            return true;

        } else if (id == R.id.action_share) {
            // Intent implisit: bagikan teks lewat aplikasi lain
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setType("text/plain");
            send.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
            startActivity(Intent.createChooser(send, getString(R.string.share_chooser)));
            return true;

        } else if (id == R.id.action_about) {
            Toast.makeText(this, R.string.toast_about, Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}