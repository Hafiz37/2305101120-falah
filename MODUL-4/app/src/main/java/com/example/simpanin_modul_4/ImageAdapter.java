package com.example.simpanin_modul_4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    // Gambar dari res/drawable: pic_1 sampai pic_15
    private static final int[] IMAGES = {
            R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3,
            R.drawable.pic_4, R.drawable.pic_5, R.drawable.pic_6,
            R.drawable.pic_7, R.drawable.pic_8, R.drawable.pic_9,
            R.drawable.pic_10, R.drawable.pic_11, R.drawable.pic_12,
            R.drawable.pic_13, R.drawable.pic_14, R.drawable.pic_15
    };

    private final Context context;
    private final boolean[] favorites = new boolean[IMAGES.length];
    private final List<Integer> visible = new ArrayList<>(); // indeks gambar yang tampil
    private Runnable onChangeListener;

    public ImageAdapter(Context context) {
        this.context = context;
        setFilter(0, IMAGES.length - 1);
    }

    /** Menampilkan gambar dari indeks 'from' sampai 'to' (inklusif, mulai dari 0). */
    public void setFilter(int from, int to) {
        visible.clear();
        for (int i = from; i <= to && i < IMAGES.length; i++) {
            visible.add(i);
        }
        notifyDataSetChanged();
    }

    public int getFavoriteCount() {
        int count = 0;
        for (boolean fav : favorites) {
            if (fav) count++;
        }
        return count;
    }

    public void setOnChangeListener(Runnable listener) {
        this.onChangeListener = listener;
    }

    @Override
    public int getCount() {
        return visible.size();
    }

    @Override
    public Object getItem(int position) {
        return IMAGES[visible.get(position)]; // id drawable
    }

    @Override
    public long getItemId(int position) {
        return visible.get(position); // indeks asli (0-14)
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        boolean isNew = false;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.relative_layout, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.imageItem);
            holder.label = convertView.findViewById(R.id.labelItem);
            holder.favButton = convertView.findViewById(R.id.btnFav);
            holder.heart = convertView.findViewById(R.id.tvHeart);
            convertView.setTag(holder);
            isNew = true;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final int index = visible.get(position);

        holder.image.setImageResource(IMAGES[index]);
        holder.label.setText(context.getString(R.string.label_foto, index + 1));
        updateHeart(holder.heart, favorites[index]);

        holder.favButton.setOnClickListener(v -> {
            favorites[index] = !favorites[index];
            updateHeart(holder.heart, favorites[index]);
            if (onChangeListener != null) onChangeListener.run();
        });

        // Animasi muncul untuk item yang baru dibuat
        if (isNew) {
            convertView.setAlpha(0f);
            convertView.setTranslationY(48f);
            convertView.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(Math.min(position, 10) * 40L)
                    .setDuration(300)
                    .start();
        }

        return convertView;
    }

    private void updateHeart(TextView heart, boolean isFavorite) {
        heart.setText(isFavorite ? R.string.heart_on : R.string.heart_off);
        heart.setTextColor(ContextCompat.getColor(context,
                isFavorite ? R.color.simpanin_heart : R.color.white));
    }

    private static class ViewHolder {
        ShapeableImageView image;
        TextView label;
        MaterialCardView favButton;
        TextView heart;
    }
}