package com.codescannerqr.generator.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.StyleItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StyleMainRecyclerAdapter extends
        RecyclerView.Adapter<StyleMainRecyclerAdapter.ViewHolder> {

    private final List<StyleItem> items;
    private final Activity activity;

    public StyleMainRecyclerAdapter(List<StyleItem> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_style_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.textViewTitleStyle.setText(items.get(position).getArgs());
        holder.imageViewIconStyle.setImageResource(items.get(position).getIcon());
        StyleBitmapRecyclerAdapter bitmapRecyclerAdapter = new StyleBitmapRecyclerAdapter(
                items.get(position).getBitmapList(),
                activity
        );
        holder.recyclerViewImageStyle.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerViewImageStyle.setAdapter(bitmapRecyclerAdapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitleStyle;
        private final ImageView imageViewIconStyle;
        private final RecyclerView recyclerViewImageStyle;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            textViewTitleStyle = itemView.findViewById(R.id.textViewTitleStyle);
            imageViewIconStyle = itemView.findViewById(R.id.imageViewIconStyle);
            recyclerViewImageStyle = itemView.findViewById(R.id.recyclerViewImageStyle);

        }
    }

}
