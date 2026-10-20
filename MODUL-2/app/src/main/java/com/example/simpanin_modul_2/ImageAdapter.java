package com.example.simpanin_modul_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class ImageAdapter extends BaseAdapter {

    private final Context context;

    // Gambar dari res/drawable: pic_1 sampai pic_15
    private final int[] images = {
            R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3,
            R.drawable.pic_4, R.drawable.pic_5, R.drawable.pic_6,
            R.drawable.pic_7, R.drawable.pic_8, R.drawable.pic_9,
            R.drawable.pic_10, R.drawable.pic_11, R.drawable.pic_12,
            R.drawable.pic_13, R.drawable.pic_14, R.drawable.pic_15
    };

    public ImageAdapter(Context context) {
        this.context = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean isNew = (convertView == null);
        if (isNew) {
            // Memakai relative_layout.xml sebagai tampilan satu item
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.relative_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.img_item);
        TextView title = convertView.findViewById(R.id.txt_item);
        TextView number = convertView.findViewById(R.id.txt_number);

        imageView.setImageResource(images[position]);
        title.setText("Bagian " + (position + 1));
        number.setText(String.format(Locale.getDefault(), "%02d", position + 1));

        // Animasi muncul (hanya saat item pertama kali dibuat)
        if (isNew) {
            convertView.setAlpha(0f);
            convertView.setTranslationY(48f);
            convertView.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(350)
                    .setStartDelay(Math.min(position, 8) * 50L)
                    .start();
        }

        return convertView;
    }
}