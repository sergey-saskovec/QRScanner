package com.codescannerqr.generator.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.view.activity.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StyleBitmapRecyclerAdapter extends
        RecyclerView.Adapter<StyleBitmapRecyclerAdapter.ViewHolder> {

    private final List<Bitmap> items;
    private final Activity activity;

    public StyleBitmapRecyclerAdapter(List<Bitmap> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_bitmap_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.imageViewBitmapStyle.setImageBitmap(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewBitmapStyle;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageViewBitmapStyle = itemView.findViewById(R.id.imageViewBitmapStyle);
            imageViewBitmapStyle.setOnClickListener(v -> {
                int position  = ViewHolder.super.getAdapterPosition();
                Bundle bundle = new Bundle();
                bundle.putParcelable("imageBitmap", items.get(position));

                ((MainActivity)activity).navController.navigate(
                        R.id.action_navigation_style_to_stylePreviewFragment ,bundle);
            });

        }
    }
}
