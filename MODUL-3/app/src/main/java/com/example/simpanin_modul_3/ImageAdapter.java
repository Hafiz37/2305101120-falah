package com.example.simpanin_modul_3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class ImageAdapter extends BaseAdapter {

    public interface OnImageClickListener {
        void onImageClick(int position, int imageRes);
    }

    private final Context context;
    private OnImageClickListener listener;

    // Gambar dari res/drawable: pic_1 sampai pic_15
    private final int[] images = {
            R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3,
            R.drawable.pic_4, R.drawable.pic_5, R.drawable.pic_6,
            R.drawable.pic_7, R.drawable.pic_8, R.drawable.pic_9,
            R.drawable.pic_10, R.drawable.pic_11, R.drawable.pic_12,
            R.drawable.pic_13, R.drawable.pic_14, R.drawable.pic_15
    };

    // Penanda agar animasi masuk hanya berjalan sekali per item
    private final boolean[] animated = new boolean[images.length];

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.relative_layout, parent, false);
        }

        // Reset state akibat daur ulang view
        convertView.animate().cancel();
        convertView.setAlpha(1f);
        convertView.setTranslationY(0f);

        View card = convertView.findViewById(R.id.cardImage);
        ImageView imageView = convertView.findViewById(R.id.imageItem);
        TextView label = convertView.findViewById(R.id.tvLabel);
        TextView badge = convertView.findViewById(R.id.tvBadge);

        int number = position + 1;
        imageView.setImageResource(images[position]);
        imageView.setContentDescription(context.getString(R.string.desc_image, number));
        label.setText(context.getString(R.string.label_image, number));
        badge.setText(String.format(Locale.getDefault(), "%02d", number));

        // Klik kartu
        card.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageClick(position, images[position]);
            }
        });

        // Efek mengecil saat ditekan
        card.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(120).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
                    break;
            }
            return false;
        });

        // Animasi masuk bertahap (fade + naik)
        if (!animated[position]) {
            animated[position] = true;
            convertView.setAlpha(0f);
            convertView.setTranslationY(60f);
            convertView.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(Math.min(position, 8) * 60L)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        return convertView;
    }
}